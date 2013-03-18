package halfpipe;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;
import static halfpipe.HalfpipeConfiguration.*;

import com.google.common.base.Throwables;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import halfpipe.cli.Shell;
import halfpipe.logging.Log;
import halfpipe.logging.LoggingUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 */
public abstract class Application<C> {
    private static final Log LOG = Log.forThisClass();

    static {
        LoggingUtils.bootstrap();
    }

    //TODO: find a better way to pass these to HalfpipeWebAppInitializer, sysprops?

    protected Class<C> contextClass;

    public void run(String[] args) {
        try {
            getContextClasses();
            Class<?> serverViewContextClass = getViewContext();

            createConfig(findConfig(args));

            registerRootContext(contextClass); //TODO: fix shell

            rootContext.refresh();

            if (serverViewContextClass != null) {
                registerCtx(rootContext).registerSingleton("viewContextClass", serverViewContextClass);
            }

            LoggingUtils.configure(config(rootContext).logging);

            LOG.info("Starting {}", config(rootContext).appName.get());

            Shell shell = getShell(rootContext);
            shell.start(args);

        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private halfpipe.configuration.Configuration config(AnnotationConfigWebApplicationContext rootContext) {
        return rootContext.getBean(halfpipe.configuration.Configuration.class);
    }

    protected ConfigurableBeanFactory registerCtx(AnnotationConfigWebApplicationContext context) {
        return (ConfigurableBeanFactory) context.getAutowireCapableBeanFactory();
    }

    protected boolean isServer(String[] args) {
        return args.length >= 1 && args[0].equals("server");
    }

    protected Shell getShell(AnnotationConfigWebApplicationContext rootContext) throws Exception {
        AnnotationConfigApplicationContext shellContext = new AnnotationConfigApplicationContext();
        shellContext.setParent(rootContext);

        Shell shell = new Shell(shellContext);

        return shell;
    }

    private String findConfig(String[] args) {
        return find(newArrayList(args), containsPattern(".*\\.json$|.*\\.yml|.*\\.yaml$"), null);
    }

    /**
     * @see <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html">Super Type Tokens</a>
     */
    @SuppressWarnings("unchecked")
    private void getContextClasses() {
        Type t = getClass();
        while (t instanceof Class<?>) {
            t = ((Class<?>) t).getGenericSuperclass();
        }
        if (t instanceof ParameterizedType) {
            // should typically have one of type parameters (first one) that matches:
            ParameterizedType parameterizedType = (ParameterizedType) t;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Assert.isTrue(typeArguments != null && typeArguments.length == 1, getClass().getName()+
                    " does not have one Context types as a type parameter");

            contextClass = (Class<C>) getWiringClass(typeArguments[0]);
        }
        if (contextClass == null)
            throw new IllegalStateException("Can not figure out Context types parameterization for "+getClass().getName());
    }

    @SuppressWarnings("unchecked")
    private Class<?> getWiringClass(Type typeArgument) {
        if (typeArgument instanceof Class<?>) {
            Class klass = Class.class.cast(typeArgument);
            if (klass.isAnnotationPresent(Configuration.class))
               return klass;
        }
        return null;
    }

    protected Class<?> getViewContext() {
        return null;
    }
}
