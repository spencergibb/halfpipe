package halfpipe.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 12:56 AM
 */
@Component
public class ConfigurationBeanPostProcessor implements BeanPostProcessor {

    @Inject
    ConfigurationBuilder builder;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (Configuration.class.isAssignableFrom(bean.getClass())) {
            try {
                builder.build(bean);
            } catch (Exception e) {
                e.printStackTrace();  //TODO: handle catch
                throw new BeanInitializationException("Unable to build configuration for class: "+bean.getClass(), e);
            }
        }
        return bean;
    }
}
