package halfpipe.properties;

import halfpipe.logging.Log;
import lombok.Data;
import org.slf4j.Logger;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 2:11 PM
 */
@Data
public abstract class AbstractCallback<C, V> implements Runnable {
    protected C properties;
    protected DynaProp<V> prop;

    @Log
    protected Logger logger;
}

