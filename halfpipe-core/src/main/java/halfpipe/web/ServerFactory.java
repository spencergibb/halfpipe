package halfpipe.web;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.yammer.metrics.jetty.*;
import halfpipe.configuration.*;
import halfpipe.jetty.BiDiGzipHandler;
import halfpipe.jetty.UnbrandedErrorHandler;
import halfpipe.util.Duration;
import halfpipe.util.Size;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.AbstractNIOConnector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.net.URI;
import java.security.KeyStore;

/*
 * A factory for creating instances of {@link org.eclipse.jetty.server.Server} and configuring Servlets
 * 
 * Registers {@link com.yammer.metrics.core.HealthCheck}s, both default and user defined
 * 
 * Creates instances of {@link org.eclipse.jetty.server.Connector},
 * configured by {@link com.yammer.dropwizard.config.HttpConfiguration} for external and admin port
 * 
 * Registers {@link org.eclipse.jetty.server.Handler}s for admin and service Servlets.
 * {@link TaskServlet} 
 * {@link AdminServlet}
 * {@link com.sun.jersey.spi.container.servlet.ServletContainer} with all resources in {@link DropwizardResourceConfig} 
 * 
 */
@Service
@Profile("CLI")
public class ServerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerFactory.class);

    private HttpConfiguration config;
    private RequestLogHandlerFactory requestLogHandlerFactory;

    @Inject
    Configuration configuration;

    @Inject
    ServletContextInitializer servletContextInitializer;

    @PostConstruct
    public void init() {
        this.config = configuration.http;
        this.requestLogHandlerFactory = new RequestLogHandlerFactory(config.requestLog,
                configuration.appName.get());
    }

    public Server buildServer(JettyServletEnvironment env) throws ConfigurationException {
        final Server server = createServer();
        server.setHandler(createHandler(env));
        return server;
    }

    private Server createServer() {
        final Server server = new Server();

        server.addConnector(createExternalConnector());

        // if we're dynamically allocating ports, no worries if they are the same (i.e. 0)
        if (config.adminPort.get() == 0 || (config.adminPort.get() != config.port.get()) ) {
            //server.addConnector(createInternalConnector());
            throw new NotImplementedException("admin connector");
        }

        server.addBean(new UnbrandedErrorHandler());

        server.setSendDateHeader(config.useDateHeader.get());
        server.setSendServerVersion(config.useServerHeader.get());

        server.setThreadPool(createThreadPool());

        server.setStopAtShutdown(true);

        server.setGracefulShutdown((int) config.shutdownGracePeriod.get().toMilliseconds());

        return server;
    }

    private Connector createExternalConnector() {
        final AbstractConnector connector = createConnector(config.port.get());

        connector.setHost(config.bindHost.get());

        connector.setAcceptors(config.acceptorThreads.get());

        connector.setForwarded(config.useForwardedHeaders.get());

        connector.setMaxIdleTime((int) config.maxIdleTime.get().toMilliseconds());

        connector.setLowResourcesMaxIdleTime((int) config.lowResourcesMaxIdleTime.get().toMilliseconds());

        connector.setAcceptorPriorityOffset(config.acceptorThreadPriorityOffset.get());

        connector.setAcceptQueueSize(config.acceptQueueSize.get());

        connector.setMaxBuffers(config.maxBufferCount.get());

        connector.setRequestBufferSize((int) config.requestBufferSize.get().toBytes());

        connector.setRequestHeaderSize((int) config.requestHeaderBufferSize.get().toBytes());

        connector.setResponseBufferSize((int) config.responseBufferSize.get().toBytes());

        connector.setResponseHeaderSize((int) config.responseHeaderBufferSize.get().toBytes());

        connector.setReuseAddress(config.reuseAddress.get());
        
        final Optional<Duration> lingerTime = Optional.fromNullable(config.soLingerTime.get());

        if (lingerTime.isPresent()) {
            connector.setSoLingerTime((int) lingerTime.get().toMilliseconds());
        }

        connector.setPort(config.port.get());
        connector.setName("main");

        return connector;
    }

    private AbstractConnector createConnector(int port) {
        final AbstractConnector connector;
        switch (config.connectorType.get()) {
            case BLOCKING:
                connector = new InstrumentedBlockingChannelConnector(port);
                break;
            case LEGACY:
                connector = new InstrumentedSocketConnector(port);
                break;
            case LEGACY_SSL:
                connector = new InstrumentedSslSocketConnector(port);
                break;
            case NONBLOCKING:
                connector = new InstrumentedSelectChannelConnector(port);
                break;
            case NONBLOCKING_SSL:
                connector = new InstrumentedSslSelectChannelConnector(port);
                break;
            default:
                throw new IllegalStateException("Invalid connector type: " + config.connectorType.get());
        }

        if (connector instanceof SslConnector) {
            configureSslContext(((SslConnector) connector).getSslContextFactory());
        }

        if (connector instanceof SelectChannelConnector) {
            ((SelectChannelConnector) connector).setLowResourcesConnections(config.lowResourcesConnectionThreshold.get());
        }

        if (connector instanceof AbstractNIOConnector) {
            ((AbstractNIOConnector) connector).setUseDirectBuffers(config.useDirectBuffers.get());
        }

        return connector;
    }

    private void configureSslContext(SslContextFactory factory) {
        final SslConfiguration sslConfig = config.ssl;

        for (File keyStore : sslConfig.getKeyStore().asSet()) {
            factory.setKeyStorePath(keyStore.getAbsolutePath());
        }

        for (String password : sslConfig.getKeyStorePassword().asSet()) {
            factory.setKeyStorePassword(password);
        }

        for (String password : sslConfig.getKeyManagerPassword().asSet()) {
            factory.setKeyManagerPassword(password);
        }

        for (String certAlias : sslConfig.getCertAlias().asSet()) {
            factory.setCertAlias(certAlias);
        }

        final String keyStoreType = sslConfig.getKeyStoreType();
        if (keyStoreType.startsWith("Windows-")) {
            try {
                final KeyStore keyStore = KeyStore.getInstance(keyStoreType);

                keyStore.load(null, null);
                factory.setKeyStore(keyStore);

            } catch (Exception e) {
                throw new IllegalStateException("Windows key store not supported", e);
            }
        } else {
            factory.setKeyStoreType(keyStoreType);
        }

        for (File trustStore : sslConfig.getTrustStore().asSet()) {
            factory.setTrustStore(trustStore.getAbsolutePath());
        }

        for (String password : sslConfig.getTrustStorePassword().asSet()) {
            factory.setTrustStorePassword(password);
        }

        final String trustStoreType = sslConfig.getTrustStoreType();
        if (trustStoreType.startsWith("Windows-")) {
            try {
                final KeyStore keyStore = KeyStore.getInstance(trustStoreType);

                keyStore.load(null, null);
                factory.setTrustStore(keyStore);

            } catch (Exception e) {
                throw new IllegalStateException("Windows key store not supported", e);
            }
        } else {
            factory.setTrustStoreType(trustStoreType);
        }

        for (Boolean needClientAuth : sslConfig.getNeedClientAuth().asSet()) {
            factory.setNeedClientAuth(needClientAuth);
        }

        for (Boolean wantClientAuth : sslConfig.getWantClientAuth().asSet()) {
            factory.setWantClientAuth(wantClientAuth);
        }

        for (Boolean allowRenegotiate : sslConfig.getAllowRenegotiate().asSet()) {
            factory.setAllowRenegotiate(allowRenegotiate);
        }

        for (File crlPath : sslConfig.getCrlPath().asSet()) {
            factory.setCrlPath(crlPath.getAbsolutePath());
        }

        for (Boolean enable : sslConfig.getCrldpEnabled().asSet()) {
            factory.setEnableCRLDP(enable);
        }

        for (Boolean enable : sslConfig.getOcspEnabled().asSet()) {
            factory.setEnableOCSP(enable);
        }

        for (Integer length : sslConfig.getMaxCertPathLength().asSet()) {
            factory.setMaxCertPathLength(length);
        }

        for (URI uri : sslConfig.getOcspResponderUrl().asSet()) {
            factory.setOcspResponderURL(uri.toASCIIString());
        }

        for (String provider : sslConfig.getJceProvider().asSet()) {
            factory.setProvider(provider);
        }

        for (Boolean validate : sslConfig.getValidatePeers().asSet()) {
            factory.setValidatePeerCerts(validate);
        }

        factory.setIncludeProtocols(Iterables.toArray(sslConfig.getSupportedProtocols(),
                String.class));
    }


    private Handler createHandler(JettyServletEnvironment env) {
        final HandlerCollection collection = new HandlerCollection();

        collection.addHandler(createExternalServlet(env));
        //collection.addHandler(createInternalServlet());

        if (requestLogHandlerFactory.isEnabled()) {
            collection.addHandler(requestLogHandlerFactory.build());
        }

        collection.addHandler(new DefaultHandler());

        return collection;
    }

    /*private Handler createInternalServlet() {
        final ServletEnvironment handler = env.getAdminContext();
        handler.addServlet(new ServletHolder(new AdminServlet()), "*//*");

        if (config.getAdminPort() != 0 && config.getAdminPort() == config.getPort()) {
            handler.setContextPath("/admin");
            handler.setConnectorNames(new String[]{"main"});
        } else {
            handler.setConnectorNames(new String[]{"internal"});
        }

        if (config.getAdminUsername().isPresent() || config.getAdminPassword().isPresent()) {
            handler.setSecurityHandler(basicAuthHandler(config.getAdminUsername().or(""), config.getAdminPassword().or("")));
        }

        return handler;
    }

    private SecurityHandler basicAuthHandler(String username, String password) {

        final HashLoginService loginService = new HashLoginService();
        loginService.putUser(username, Credential.getCredential(password), new String[] {"user"});
        loginService.setName("admin");

        final Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);

        final ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        final ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName("admin");
        csh.addConstraintMapping(constraintMapping);
        csh.setLoginService(loginService);

        return csh;
    }*/

    private Handler createExternalServlet(JettyServletEnvironment env) {
        servletContextInitializer.init(env);

        env.getContext().setConnectorNames(new String[]{"main"});

        return wrapHandler(env.getContext());
    }

    private Handler wrapHandler(ServletContextHandler handler) {
        final InstrumentedHandler instrumented = new InstrumentedHandler(handler);
        final GzipConfiguration gzip = config.gzip;
        if (gzip.enabled.get()) {
            final BiDiGzipHandler gzipHandler = new BiDiGzipHandler(instrumented);

            final Size minEntitySize = gzip.minimumEntitySize.get();
            gzipHandler.setMinGzipSize((int) minEntitySize.toBytes());

            final Size bufferSize = gzip.bufferSize.get();
            gzipHandler.setBufferSize((int) bufferSize.toBytes());

            /* TODO: handle set properties
            final ImmutableSet<String> userAgents = gzip.getExcludedUserAgents();
            if (!userAgents.isEmpty()) {
                gzipHandler.setExcluded(userAgents);
            }

            final ImmutableSet<String> mimeTypes = gzip.getCompressedMimeTypes();
            if (!mimeTypes.isEmpty()) {
                gzipHandler.setMimeTypes(mimeTypes);
            }*/

            return gzipHandler;
        }
        return instrumented;
    }

    private ThreadPool createThreadPool() {
        final InstrumentedQueuedThreadPool pool = new InstrumentedQueuedThreadPool();
        pool.setMinThreads(config.minThreads.get());
        pool.setMaxThreads(config.maxThreads.get());
        return pool;
    }

    /*private Connector createInternalConnector() {
        final SocketConnector connector = new SocketConnector();
        connector.setHost(config.getBindHost().orNull());
        connector.setPort(config.getAdminPort());
        connector.setName("internal");
        connector.setThreadPool(new QueuedThreadPool(8));
        return connector;
    }*/
}
