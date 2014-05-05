package halfpipe.web;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * {@link HandlerMapping} to map {@link Endpoint}s to URLs via {@link Endpoint#getId()}.
 * The semantics of {@code @RequestMapping} should be identical to a normal
 * {@code @Controller}, but the controllers should not be annotated as {@code @Controller}
 * (otherwise they will be mapped by the normal MVC mechanisms).
 *
 * <p>
 * One of the aims of the mapping is to support controllers that work as HTTP controllers but
 * can still provide useful service interfaces when there is no HTTP server (and no Spring
 * MVC on the classpath). Note that any controllers having method signatures will break in
 * a non-servlet environment.
 */
public class WarHandlerMapping extends RequestMappingHandlerMapping implements
        ApplicationContextAware {

    private final Set<? extends WarController> controllers;

    private String prefix = "";

    private boolean disabled = false;

    /**
     * Create a new {@link WarHandlerMapping} instance. All {@link WarController}s will be
     * detected from the {@link ApplicationContext}.
     * @param controllers
     */
    public WarHandlerMapping(Collection<? extends WarController> controllers) {
        this.controllers = new HashSet<WarController>(controllers);
        // By default the static resource handler mapping is LOWEST_PRECEDENCE - 1
        setOrder(LOWEST_PRECEDENCE - 10);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (!this.disabled) {
            for (WarController controller : this.controllers) {
                detectHandlerMethods(controller);
            }
        }
    }

    /**
     * Since all handler beans are passed into the constructor there is no need to detect
     * anything here
     */
    @Override
    protected boolean isHandler(Class<?> beanType) {
        return false;
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method,
                                         RequestMappingInfo mapping) {

        if (mapping == null) {
            return;
        }

        Set<String> defaultPatterns = mapping.getPatternsCondition().getPatterns();
        String[] patterns = new String[defaultPatterns.isEmpty() ? 1 : defaultPatterns
                .size()];

        String path = "";
        Object bean = handler;
        if (bean instanceof String) {
            bean = getApplicationContext().getBean((String) handler);
        }
        if (bean instanceof WarController) {
            WarController endpoint = (WarController) bean;
            path = endpoint.getPath();
        }

        int i = 0;
        String prefix = StringUtils.hasText(this.prefix) ? this.prefix + path : path;
        if (defaultPatterns.isEmpty()) {
            patterns[0] = prefix;
        }
        else {
            for (String pattern : defaultPatterns) {
                patterns[i] = prefix + pattern;
                i++;
            }
        }
        PatternsRequestCondition patternsInfo = new PatternsRequestCondition(patterns);

        RequestMappingInfo modified = new RequestMappingInfo(patternsInfo,
                mapping.getMethodsCondition(), mapping.getParamsCondition(),
                mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                mapping.getProducesCondition(), mapping.getCustomCondition());

        super.registerHandlerMethod(handler, method, modified);
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        Assert.isTrue("".equals(prefix) || StringUtils.startsWithIgnoreCase(prefix, "/"),
                "prefix must start with '/'");
        this.prefix = prefix;
    }

    /**
     * @return the prefix used in mappings
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Sets if this mapping is disabled.
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Returns if this mapping is disabled.
     */
    public boolean isDisabled() {
        return this.disabled;
    }

    /**
     * Return the controllers
     */
    public Set<? extends WarController> getControllers() {
        return this.controllers;
    }

}
