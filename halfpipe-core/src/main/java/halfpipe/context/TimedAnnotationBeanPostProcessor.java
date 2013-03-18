package halfpipe.context;

import com.yammer.metrics.annotation.Timed;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.spring.AbstractProxyingBeanPostProcessor;
import com.yammer.metrics.spring.TimedMethodInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyConfig;

import javax.ws.rs.Path;

/**
 * // don't proxy jax-rs resources, currently annotated with @Path
 * User: spencergibb
 * Date: 10/10/12
 * Time: 3:00 PM
 */
public class TimedAnnotationBeanPostProcessor extends AbstractProxyingBeanPostProcessor {

    private static final long serialVersionUID = -1589475386869891203L;

    private final Pointcut pointcut = new NotClassAnnotationMatchingPointcut(Path.class, Timed.class);
    private final MetricsRegistry metrics;
    private final String scope;

    public TimedAnnotationBeanPostProcessor(final MetricsRegistry metrics, final ProxyConfig config, final String scope) {
        this.metrics = metrics;
        this.scope = scope;

        this.copyFrom(config);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public MethodInterceptor getMethodInterceptor(Class<?> targetClass) {
        return new TimedMethodInterceptor(metrics, targetClass, scope);
    }

}
