package halfpipe.config;

/**
 * User: spencergibb
 * Date: 12/12/13
 * Time: 5:26 PM
 */
public class DynaProp<T> {
    private T value;

    public static <T> DynaProp<T> prop(T value) {
        return new DynaProp<T>(value);
    }

    public DynaProp(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public T get() {
        return getValue();
    }
}
