package halfpipe.configuration;

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 11:44 AM
 */
public class Defaults {
    public static final String DUMMY_PROP_NAME = "____dummy__****__prop____";

    //TODO: support collection properties

    public static <V> DynaProp<V> prop(V val) {
        return new DynamicProp<V>(DUMMY_PROP_NAME, val);
    }
}
