package halfpipe;

import static halfpipe.HalfpipeConfiguration.*;

import com.google.common.base.Throwables;
import halfpipe.cli.Shell;
import halfpipe.logging.Log;
import halfpipe.logging.LoggingUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 */
public abstract class Application<C> extends ContextAware<C> {
    private static final Log LOG = Log.forThisClass();

    static {
        LoggingUtils.bootstrap();
    }

    public void run(String[] args) {
        try {
            Class<C> contextClass = getContextClass();
            Class<?> serverViewContextClass = getViewContext();

            String configFile = findConfig(args);
            startApplication(configFile, contextClass, serverViewContextClass, LOG);

            Shell shell = getShell(rootContext);
            shell.start(args);

        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    public static void startApplication(String configFile, Class<?> contextClass, Class<?> serverViewContextClass, Log log) throws Exception {
        createConfig(configFile);

        registerRootContext(contextClass);

        rootContext.refresh();

        if (serverViewContextClass != null) {
            registerCtx(rootContext).registerSingleton("viewContextClass", serverViewContextClass);
        }

        LoggingUtils.configure(config(rootContext).logging);

        log.info("Starting {}", config(rootContext).appName.get());
    }

    public static halfpipe.configuration.Configuration config(AnnotationConfigWebApplicationContext rootContext) {
        return rootContext.getBean(halfpipe.configuration.Configuration.class);
    }

    public static ConfigurableBeanFactory registerCtx(AnnotationConfigWebApplicationContext context) {
        return (ConfigurableBeanFactory) context.getAutowireCapableBeanFactory();
    }

    protected Shell getShell(AnnotationConfigWebApplicationContext rootContext) throws Exception {
        AnnotationConfigApplicationContext shellContext = new AnnotationConfigApplicationContext();
        shellContext.setParent(rootContext);

        Shell shell = new Shell(shellContext);

        return shell;
    }

}
