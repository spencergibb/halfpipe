package halfpipe.properties;

/**
 * User: spencergibb
 * Date: 12/13/13
 * Time: 5:26 PM
 */
public interface DynaProp<V> {
    public V get();
    public V getValue();
    public String getName();
    //TODO: integrate hibernate validator and DynamicProperty.validate
}
