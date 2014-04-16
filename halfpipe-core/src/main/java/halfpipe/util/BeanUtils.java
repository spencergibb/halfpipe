package halfpipe.util;

import com.google.common.base.Optional;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * User: spencergibb
 * Date: 4/16/14
 * Time: 10:37 AM
 */
public class BeanUtils {

    private static ApplicationContext CONTEXT;

    public BeanUtils(ApplicationContext context) {
        CONTEXT = context;
    }

    public static ApplicationContext context() {
        return CONTEXT;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return CONTEXT.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return CONTEXT.getBean(name, requiredType);
    }

    public static <T> Optional<T> getOptionalBean(String name, Class<T> requiredType) {
        try {
            T bean = CONTEXT.getBean(name, requiredType);
            return Optional.fromNullable(bean);
        } catch (NoSuchBeanDefinitionException e) {
            return Optional.absent();
        }
    }
}
