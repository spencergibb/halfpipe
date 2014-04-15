package halfpipe.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixExecutable;
import feign.InvocationHandlerFactory;
import feign.MethodHandler;
import feign.Target;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.Future;

import static feign.Util.checkNotNull;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 10:44 PM
 * TODO: use spring and archaius to wire up setter and getfallback using HalfpipeClientConfigurer?
 */
public class HystrixInvocationHandler implements InvocationHandler {

    public static class Factory implements InvocationHandlerFactory {
        @Override
        public InvocationHandler create(Target target, Map<Method, MethodHandler> methodToHandler) {
            return new HystrixInvocationHandler(target, methodToHandler);
        }
    }

    @Inject
    ApplicationContext context;

    private final Target target;
    private final Map<Method, MethodHandler> methodToHandler;

    public HystrixInvocationHandler(Target target, Map<Method, MethodHandler> methodToHandler) {
        this.target = checkNotNull(target, "target");
        this.methodToHandler = checkNotNull(methodToHandler, "methodToHandler for %s", target);
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        if ("hashCode".equals(method.getName())) {
            return hashCode();
        }

        //TODO: get setter from context
        //TODO: get group from config? Use app name as default
        HystrixCommand.Setter setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("default"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(method.getName()))
                ;

        MethodHandler methodHandler = methodToHandler.get(method);

        ProxiedCommand command = new ProxiedCommand(setter, methodHandler, args);

        Class<?> returnType = method.getReturnType();

        if (Future.class.isAssignableFrom(returnType)) {
            return command.queue();
        } else if (HystrixExecutable.class.isAssignableFrom(returnType)) {
            return command;
        }
        //TODO: add command.observe() support

        //TODO: add getFallback support through bean lookup

        return command.execute();
    }

    @Override public int hashCode() {
        return target.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (HystrixInvocationHandler.class != obj.getClass()) {
            return false;
        }
        HystrixInvocationHandler that = HystrixInvocationHandler.class.cast(obj);
        return this.target.equals(that.target);
    }

    @Override public String toString() {
        return "target(" + target + ")";
    }

    public class ProxiedCommand extends HystrixCommand<Object> {

        private final MethodHandler methodHandler;
        private final Object[] args;

        protected ProxiedCommand(Setter setter, MethodHandler methodHandler, Object[] args) {
            super(setter);
            this.methodHandler = methodHandler;
            this.args = args;
        }

        @Override
        protected Object run() throws Exception {
            try {
                return methodHandler.invoke(args);
            } catch (Exception e) {
                throw e;
            } catch (Throwable t) {
                throw new Exception(t);
            }
        }
    }
}
