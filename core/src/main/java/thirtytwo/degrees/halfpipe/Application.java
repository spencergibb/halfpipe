package thirtytwo.degrees.halfpipe;

import static com.google.common.base.Predicates.*;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;
import static thirtytwo.degrees.halfpipe.HalfpipeConfiguration.*;

import com.google.common.base.Throwables;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import thirtytwo.degrees.halfpipe.cli.Server;
import thirtytwo.degrees.halfpipe.cli.Shell;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 */
public abstract class Application<C, VC> {

    //TODO: find a better way to pass these to HalfpipeWebAppInitializer, sysprops?
    public static Class<?> serverContextClass;
    public static Class<?> serverViewContextClass;
    public static String configFileName;

    protected Class<C> contextClass;
    protected Class<VC> viewContextClass;

    public void run(String[] args) {
        try {
            getContextClasses();
            if (isServer(args)) {
                Application.serverContextClass = this.contextClass;
                Application.serverViewContextClass = this.viewContextClass;

                Application.configFileName = findConfig(args);

                Server command = new Server();
                command.run(null);
            } else {
                Shell shell = getShell(args);
                shell.start(args);
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    protected boolean isServer(String[] args) {
        return args.length >= 1 && args[0].equals("server");
    }

    protected Shell getShell(String[] args) throws Exception {

        String configFile = findConfig(args);
        createConfig(false, configFile);
        AnnotationConfigApplicationContext rootContext = createContext(contextClass, false); //TODO: fix shell
        rootContext.registerBeanDefinition(Server.class.getSimpleName(), new RootBeanDefinition(Server.class));
        Shell shell = new Shell(rootContext);

        return shell;
    }

    private String findConfig(String[] args) {
        String config = find(newArrayList(args), containsPattern(".*\\.json$|.*\\.yml|.*\\.yaml$"), null);
        return config;
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
            Assert.isTrue(typeArguments != null && typeArguments.length == 2, getClass().getName()+
                    " does not have two Context types as type parameters");

            contextClass = (Class<C>) getWiringClass(typeArguments[0]);
            viewContextClass = (Class<VC>) getWiringClass(typeArguments[1]);
        }
        if (contextClass == null || viewContextClass == null)
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
}
