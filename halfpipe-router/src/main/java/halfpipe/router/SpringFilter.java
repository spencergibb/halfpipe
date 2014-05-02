package halfpipe.router;

import com.netflix.zuul.ZuulFilter;
import halfpipe.util.BeanUtils;

/**
 * User: spencergibb
 * Date: 5/1/14
 * Time: 10:59 PM
 */
public abstract class SpringFilter extends ZuulFilter {

    protected <T> T getBean(Class<T> beanClass) {
        return BeanUtils.getBean(beanClass);
    }
}
