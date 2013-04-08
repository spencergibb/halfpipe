package halfpipe.configuration;

import com.netflix.config.*;

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 11:44 AM
 */
public class Defaults {
    public static final String DUMMY_PROP_NAME = "____dummy__****__prop____";

    //TODO: support collection properties

    public static DynamicIntProperty prop(int prop) {
        return new DynamicIntProperty(DUMMY_PROP_NAME, prop);
    }

    public static DynamicLongProperty prop(long prop) {
        return new DynamicLongProperty(DUMMY_PROP_NAME, prop);
    }

    public static DynamicFloatProperty prop(float prop) {
        return new DynamicFloatProperty(DUMMY_PROP_NAME, prop);
    }

    public static DynamicDoubleProperty prop(double prop) {
        return new DynamicDoubleProperty(DUMMY_PROP_NAME, prop);
    }

    public static DynamicBooleanProperty prop(boolean prop) {
        return new DynamicBooleanProperty(DUMMY_PROP_NAME, prop);
    }

    public static DynamicStringProperty prop(String prop) {
        return new DynamicStringProperty(DUMMY_PROP_NAME, prop);
    }

    public static <V> DynamicProp<V> prop(V val) {
        return new DynamicProp<V>(DUMMY_PROP_NAME, val, val.getClass());
    }
}
