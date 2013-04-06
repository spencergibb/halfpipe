package halfpipe;

import static halfpipe.HalfpipeConfiguration.*;

import com.google.common.base.Throwables;
import halfpipe.cli.Shell;
import halfpipe.configuration.Configuration;
import halfpipe.logging.Log;
import halfpipe.logging.LoggingUtils;
import halfpipe.web.DefualtServletContextHandler;
import halfpipe.web.ServletContextInitializer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 * http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/
 */
public abstract class Application<C> extends ContextAware<C>
        implements WebApplicationInitializer {

    private static final Log LOG = Log.forThisClass();

    static {
        LoggingUtils.bootstrap();
    }

    /**
     * Run from the command line
     * @param args
     */
    public void run(String[] args) {
        try {
            Class<C> contextClass = getContextClass();
            Class<?> serverViewContextClass = getViewContext();

            String configFile = findConfig(args);
            startApplication(configFile, contextClass, serverViewContextClass, "CLI");

            Shell shell = getShell(rootContext);
            shell.start(args);

        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    /**
     * Start from a war
     * @param sc
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        try {
            String configFile = System.getProperty("halfpipe.config.file");
            startApplication(configFile, getContextClass(), getViewContext(), "WAR");

            ServletContextInitializer initializer = rootContext.getBean(ServletContextInitializer.class);

            initializer.init(new DefualtServletContextHandler(sc), false);
        } catch (Exception e) {
            e.printStackTrace();
            sc.log("Unable to initialize Halfpipe web application", e);
            throw new ServletException("Unable to initialize Halfpipe web application", e);
        }
    }

    protected void startApplication(String configFile, Class<?> contextClass, Class<?> serverViewContextClass, String appType) throws Exception {
        createConfig(configFile);

        registerRootContext(contextClass);

        rootContext.refresh();

        if (serverViewContextClass != null) {
            registerCtx(rootContext).registerSingleton("viewContextClass", serverViewContextClass);
        }

        LoggingUtils.configure(config(rootContext).logging);

        LOG.info("Starting {} via {}", config(rootContext).appName.get(), appType);
    }


    protected halfpipe.configuration.Configuration config(AnnotationConfigWebApplicationContext rootContext) {
        return rootContext.getBean(halfpipe.configuration.Configuration.class);
    }

    protected ConfigurableBeanFactory registerCtx(AnnotationConfigWebApplicationContext context) {
        return (ConfigurableBeanFactory) context.getAutowireCapableBeanFactory();
    }

    protected Shell getShell(AnnotationConfigWebApplicationContext rootContext) throws Exception {
        AnnotationConfigApplicationContext shellContext = new AnnotationConfigApplicationContext();
        shellContext.setParent(rootContext);

        Shell shell = new Shell(shellContext);

        return shell;
    }

}
