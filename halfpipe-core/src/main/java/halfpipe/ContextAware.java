package halfpipe;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;

/**
 * User: spencergibb
 * Date: 4/3/13
 * Time: 12:03 PM
 */
public class ContextAware<C> {
    protected String findConfig(String[] args) {
        return find(newArrayList(args), containsPattern(".*\\.json$|.*\\.yml|.*\\.yaml$"), null);
    }

    /**
     * @see <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html">Super Type Tokens</a>
     */
    @SuppressWarnings("unchecked")
    protected Class<C> getContextClass() {
        Class<C> contextClass = null;
        Type t = getClass();
        while (t instanceof Class<?>) {
            t = ((Class<?>) t).getGenericSuperclass();
        }
        if (t instanceof ParameterizedType) {
            // should typically have one of type parameters (first one) that matches:
            ParameterizedType parameterizedType = (ParameterizedType) t;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Assert.isTrue(typeArguments != null && typeArguments.length == 1, getClass().getName() +
                    " does not have one Context types as a type parameter");

            contextClass = (Class<C>) getWiringClass(typeArguments[0]);
        }
        if (contextClass == null)
            throw new IllegalStateException("Can not figure out Context types parameterization for "+getClass().getName());
        return contextClass;
    }

    @SuppressWarnings("unchecked")
    protected Class<?> getWiringClass(Type typeArgument) {
        if (typeArgument instanceof Class<?>) {
            Class klass = Class.class.cast(typeArgument);
            if (klass.isAnnotationPresent(Configuration.class))
               return klass;
        }
        return null;
    }

    protected Class<?> getViewContext() {
        return null;
    }
}
