package halfpipe.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 10:44 PM
 * TODO: use spring and archaius to wire up setter and getfallback using HalfpipeClientConfigurer?
 */
public class HystrixInvocationHandler<T> implements InvocationHandler {

    private final T target;

    public HystrixInvocationHandler(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //TODO: if there is a Setter bean with commandName.Setter, use that
        HystrixCommand.Setter setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("mygroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(method.getName()))
                /*.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().
                        withExecutionIsolationThreadTimeoutInMilliseconds(500))*/
                ;

        ProxiedCommand command = new ProxiedCommand(setter, method, args);

        Class<?> returnType = method.getReturnType();

        if (Future.class.isAssignableFrom(returnType)) {
            return command.queue();
        }
        //TODO: add command.observe() support

        return command.execute();
    }

    public class ProxiedCommand extends HystrixCommand<Object> {

        private final Method method;
        private final Object[] args;

        protected ProxiedCommand(Setter setter, Method method, Object[] args) {
            super(setter);
            this.method = method;
            this.args = args;
        }

        @Override
        protected Object run() throws Exception {
            Object o = method.invoke(target, args);
            if (o instanceof DummyFuture) {
                DummyFuture dummyFuture = (DummyFuture) o;
                return dummyFuture.getValue();
            }
            return o;
        }
    }
}
