package thirtytwo.degrees.halfpipe.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 12:56 AM
 */
public class ConfigurationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (Configuration.class.isAssignableFrom(bean.getClass())) {
            try {
                ConfigurationFactory.build(bean);
            } catch (Exception e) {
                e.printStackTrace();  //TODO: handle catch
                throw new BeanInitializationException("Unable to build configuration for class: "+bean.getClass(), e);
            }
        }
        return bean;
    }
}
