package halfpipe.configuration;

/**
 * User: spencergibb
 * Date: 4/9/13
 * Time: 8:17 PM
 */
public interface DynaProp<V> {
    public V get();
    public V getValue();
    public String getName();
    //TODO: integrate hibernate validator and DynamicProperty.validate
}
