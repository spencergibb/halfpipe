package halfpipe.util;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: spencergibb
 * Date: 5/2/14
 * Time: 11:00 AM
 */
public abstract class RunOnceApplicationListener<E extends ApplicationEvent> implements ApplicationListener<E> {

    protected AtomicBoolean run = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(E event) {
        if (run.compareAndSet(false, true)) {
            onApplicationEventInternal(event);
        }
    }

    public abstract void onApplicationEventInternal(E event);
}
