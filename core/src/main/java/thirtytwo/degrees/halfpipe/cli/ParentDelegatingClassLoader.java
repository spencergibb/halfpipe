package thirtytwo.degrees.halfpipe.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * see original https://raw.github.com/grails/grails-core/master/grails-plugin-tomcat/src/main/groovy/org/grails/plugins/tomcat/ParentDelegatingClassLoader.java
 */
public class ParentDelegatingClassLoader extends ClassLoader{

    private Method findClassMethod;

    protected ParentDelegatingClassLoader(ClassLoader parent) {
        super(parent);
        System.out.println("setting parent classloader: "+parent);
        findClassMethod = findMethod(ClassLoader.class,"findClass", String.class);
        findClassMethod.setAccessible(true);
    }

    private Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                if (name.equals(method.getName())
                        && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }
    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            return (Class<?>) findClassMethod.invoke(getParent(), className);
        } catch (IllegalAccessException e) {
            throw new ClassNotFoundException(className);
        } catch (InvocationTargetException e) {
            throw new ClassNotFoundException(className);
        }
    }
}