package halfpipe.properties;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 2:11 PM
 */
@Data
public abstract class AbstractCallback<C, V> implements Runnable {
    protected C properties;
    protected DynaProp<V> prop;

    //@Log TODO: avoiding circular dependency
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
}

