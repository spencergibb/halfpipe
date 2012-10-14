package thirtytwo.degrees.halfpipe.cli;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleBase;
import org.apache.naming.resources.DirContextURLStreamHandler;
import org.apache.naming.resources.DirContextURLStreamHandlerFactory;
import thirtytwo.degrees.halfpipe.logging.Log;

import java.beans.PropertyChangeListener;
import java.net.*;

/**
 * see original https://github.com/grails/grails-core/blob/master/grails-plugin-tomcat/src/main/groovy/org/grails/plugins/tomcat/TomcatLoader.groovy
 * User: spencergibb
 * Date: 9/29/12
 * Time: 11:38 PM
 */
public class TomcatLoader extends LifecycleBase implements Loader {
    private static final Log LOG = Log.forThisClass();

    private static boolean first = true;

    ClassLoader classLoader;
    Container container;
    boolean delegate;
    boolean reloadable;

    public TomcatLoader(ClassLoader classLoader) {
        // Class loader that only searches the parent
        //this.classLoader = new ParentDelegatingClassLoader(classLoader);
        this.classLoader = classLoader;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {}

    public void addRepository(String repository) {
        LOG.warn("Call to addRepository($repository) was ignored.");
    }

    public void backgroundProcess() {}

    public String[] findRepositories() {
        LOG.warn("Call to findRepositories() returned null.");
        return null;
    }

    public String getInfo() { return "MyLoader/1.0"; }

    public boolean modified() { return false; }

    public void removePropertyChangeListener(PropertyChangeListener listener) {}

    @Override protected void initInternal() {
        URLStreamHandlerFactory streamHandlerFactory = new URLStreamHandlerFactory() {
            @Override
            public URLStreamHandler createURLStreamHandler(String protocol) {
                if (protocol.equals("jndi")) {
                    return new DirContextURLStreamHandler();
                }
                return DirContextURLStreamHandlerFactory.getInstance().createURLStreamHandler(protocol);
            }
        };

        if (first) {
            first = false;
            try {
                URL.setURLStreamHandlerFactory(streamHandlerFactory);
            } catch (Exception e) {
                // Log and continue anyway, this is not critical
                LOG.error("Error registering jndi stream handler", e);
            } catch (Throwable t) {
                // This is likely a dual registration
                LOG.info("Dual registration of jndi stream handler: " + t.getMessage());
            }
        }

        LOG.debug("binding classloader: {}", classLoader);
        DirContextURLStreamHandler.bind(classLoader, container.getResources());
    }

    @Override protected void destroyInternal() {
        classLoader = null;
    }

    @Override protected void startInternal() throws LifecycleException {
        fireLifecycleEvent(Lifecycle.START_EVENT, this);
        setState(LifecycleState.STARTING);
    }

    @Override protected void stopInternal() throws LifecycleException {
        fireLifecycleEvent(Lifecycle.STOP_EVENT, this);
        setState(LifecycleState.STOPPING);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public boolean getDelegate() {
        return delegate;
    }

    public void setDelegate(boolean delegate) {
        this.delegate = delegate;
    }

    public boolean getReloadable() {
        return reloadable;
    }

    public void setReloadable(boolean reloadable) {
        this.reloadable = reloadable;
    }
}