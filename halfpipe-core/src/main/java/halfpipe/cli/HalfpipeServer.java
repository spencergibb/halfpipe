package halfpipe.cli;

import halfpipe.configuration.Configuration;
import halfpipe.logging.Log;
import halfpipe.web.JettyWebRegistrar;
import halfpipe.web.WebApp;
import org.apache.commons.cli.CommandLine;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 11:44 PM
 */
public class HalfpipeServer implements CommandMarker {
    private static final Log LOG = Log.forThisClass();

    @Inject
    Configuration config;

    @Inject
    WebApp webApp;

    @CliAvailabilityIndicator({"server"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "server", help = "run halfpipe in jetty http server")
    public String server(
            @CliOption(key = {"", "config"}, mandatory = true, help = "config file")
            String config ) throws Exception
    {
        run(null);
        return null;
    }

    public void run(CommandLine commandLine) throws Exception {
        Server server = new Server(config.http.port.get());

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("."); //TODO: has to be set to non null?
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        //context.addServlet(JspServlet.class, "*.jsp");*/

        webApp.configure(context.getServletContext(), context, new JettyWebRegistrar(), true);

        /*Connector connector = new Connector(config.http.protocol.get());
        connector.setPort(config.http.port.get());
        connector.setURIEncoding(config.http.uriEncoding.get());*/

        //TODO https config
        //TODO use naming config
        //TODO ajp config
        //TODO serverXml config?

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);

        LOG.info("staring jetty on port {}", config.http.port.get());
        server.start();
        LOG.info("waiting for connections on port {}", config.http.port.get());
        server.join();
    }

}
