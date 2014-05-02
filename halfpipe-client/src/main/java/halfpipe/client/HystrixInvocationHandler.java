package halfpipe.client;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixExecutable;
import feign.InvocationHandlerFactory;
import feign.MethodHandler;
import feign.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.Future;

import static feign.Util.*;
import static halfpipe.util.BeanUtils.*;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 10:44 PM
 */
public class HystrixInvocationHandler implements InvocationHandler {
    private static Logger LOG = LoggerFactory.getLogger(HystrixInvocationHandler.class);

    public static class Factory implements InvocationHandlerFactory {
        @Override
        public InvocationHandler create(Target target, Map<Method, MethodHandler> methodToHandler) {
            return new HystrixInvocationHandler(target, methodToHandler);
        }
    }

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

        String fallbackBeanName = getBeanName(method, "fallback");
        String setterBeanName = getBeanName(method, "setter");

        String groupKey = context().getEnvironment().getProperty("application.id"); //.optional().or("default");

        Optional<Setter> setterOptional = getOptionalBean(setterBeanName, Setter.class);

        Setter setter = setterOptional.or(Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                        .andCommandKey(HystrixCommandKey.Factory.asKey(method.getName()))
        );

        //TODO: support for java 8 Supplier
        Optional<Supplier> fallback = getOptionalBean(fallbackBeanName, Supplier.class);

        MethodHandler methodHandler = methodToHandler.get(method);

        String name = groupKey + ":" + method.getName();
        ProxiedCommand command = new ProxiedCommand(name, setter, methodHandler, args, fallback);

        Class<?> returnType = method.getReturnType();

        if (Future.class.isAssignableFrom(returnType)) {
            return command.queue();
        } else if (Observable.class.isAssignableFrom(returnType)) {
            return command.observe();
        } else if (HystrixExecutable.class.isAssignableFrom(returnType)) {
            return command;
        }

        return command.execute();
    }

    private String getBeanName(Method method, String suffix) {
        String typeName = target.type().getSimpleName();
        return typeName + "." + method.getName() + "."+ suffix;
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

        private final String name;
        private final MethodHandler methodHandler;
        private final Object[] args;
        private final Optional<Supplier> fallback;

        protected ProxiedCommand(String name, Setter setter, MethodHandler methodHandler, Object[] args, Optional<Supplier> fallback) {
            super(setter);
            this.name = name;
            this.methodHandler = methodHandler;
            this.args = args;
            this.fallback = fallback;
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

        @Override
        protected Object getFallback() {
            if (fallback.isPresent()) {
                Supplier supplier = fallback.get();
                return supplier.get();
            }
            Throwable e = getFailedExecutionException();
            LOG.debug("No fallback with exception", e);
            throw new UnsupportedOperationException("No fallback available for "+name, e);
        }
    }
}
