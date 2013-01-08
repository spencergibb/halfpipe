package thirtytwo.degrees.halfpipe.configuration;

import com.netflix.config.PropertyWrapper;

/**
 * User: spencergibb
 * Date: 10/17/12
 * Time: 2:00 PM
 */
public abstract class AbstractCallback<C, V> implements Runnable {
    protected C config;
    protected PropertyWrapper<V> prop;

    public void setConfig(C config) {
        this.config = config;
    }

    public void setProp(PropertyWrapper<V> prop) {
        this.prop = prop;
    }
}
