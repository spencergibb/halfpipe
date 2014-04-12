package halfpipe.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 11:34 PM
 */
public class DummyFuture<V> implements Future<V> {

    private final V value;

    public DummyFuture(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("DummyFuture.cancel is not supported");
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("DummyFuture.isCancelle is not supported");
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("DummyFuture.isDone is not supported");
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("DummyFuture.get is not supported");
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("DummyFuture.get is not supported");
    }
}
