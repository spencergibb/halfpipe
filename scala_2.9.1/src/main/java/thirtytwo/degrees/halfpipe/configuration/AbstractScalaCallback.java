package thirtytwo.degrees.halfpipe.configuration;

/**
 * User: spencergibb
 * Date: 10/17/12
 * Time: 2:00 PM
 */
@SuppressWarnings("unchecked")
public abstract class AbstractScalaCallback<C, V> implements Runnable {
    protected C config;
    protected ScalaPropertyWrapper<V> prop;

    public void setConfig(Object config) {
        this.config = (C) config;
    }

    public void setProp(Object prop) {
        if (prop instanceof ScalaPropertyWrapper) {
            this.prop = (ScalaPropertyWrapper<V>) prop;
        } else {
            System.err.println("prop is not ScalaPropertyWrapper: "+prop.getClass());
        }
    }
}
