package thirtytwo.degrees.halfpipe.cli;

import static com.google.common.collect.Iterables.*;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import thirtytwo.degrees.halfpipe.configuration.Configuration;
import thirtytwo.degrees.halfpipe.configuration.ConfigurationBuilder;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 11:44 PM
 */
public class Server implements CommandMarker {

    @Inject
    Configuration config;

    @CliAvailabilityIndicator({"server"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "server", help = "run halfpipe in tomcat http server")
    public String server() throws Exception {
        //run(null);
        return "currently the server command only works as a command line argument";
    }

    public void run(CommandLine commandLine) throws Exception {
        System.out.println("Starting Server");

        setupTomcatHome();

        Tomcat tomcat = new Tomcat();

        Connector connector = new Connector(config.http.protocol.get());
        connector.setPort(config.http.port.get());
        connector.setURIEncoding(config.http.uriEncoding.get());

        tomcat.getService().addConnector(connector);
        tomcat.setConnector(connector);

        //TODO https config
        //TODO use naming config
        //TODO ajp config
        //TODO serverXml config?

        if (isOneJar()) {
            System.out.println("in one-jar");

            File warFile = findWarFile();

            String baseDir = warFile.getAbsolutePath();
            //String baseDir = userDir+File.separator+".halfpipe/binlibs/halfpipe-example.war";
            System.out.println("baseDir: " + baseDir);

            Context context = tomcat.addWebapp("", baseDir);
            //System.setProperty("tomcat.util.scan.DefaultJarScanner.jarsToSkip", "*.jar");
            StandardJarScanner jarScanner = new StandardJarScanner() {
                @Override
                public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback, Set<String> jarsToSkip) {
                    System.out.println("scanning classloader: " + classloader);
                    super.scan(context, classloader, callback, jarsToSkip); //TODO: implement .scan
                }
            };
            jarScanner.setScanClassPath(false);
            context.setJarScanner(jarScanner);
            context.setReloadable(false);
            //https://github.com/grails/grails-core/blob/master/grails-plugin-tomcat/src/main/groovy/org/grails/plugins/tomcat/InlineExplodedTomcatServer.groovy
            ClassLoader classLoader = getClass().getClassLoader();
            System.out.println("setting classloader: " + classLoader);
            TomcatLoader loader = new TomcatLoader(classLoader);
            loader.setContainer(context);
            context.setLoader(loader);
        } else {
            System.out.println("starting dev");
            String baseDir = getWebappDir();
            tomcat.addWebapp("", baseDir);

        }
        System.out.println("staring tomcat");
        tomcat.start();
        System.out.println("waiting for connections");
        waitIndefinitely();
    }

    private void setupTomcatHome() {
        String basedir = getHalfpipeDir() + File.separator + "tomcat";
        File home = new File(basedir);
        home.mkdirs();
        if (!home.isAbsolute()) {
            try {
                basedir = home.getCanonicalPath();
            } catch (IOException e) {
                basedir = home.getAbsolutePath();
            }
        }
        System.setProperty( "catalina.base", basedir);
    }

    private String getWebappDir() {
        String userDir = System.getProperty("user.dir");
        AndFileFilter filter = new AndFileFilter();
        filter.addFileFilter(new NameFileFilter("web.xml"));
        filter.addFileFilter(new AbstractFileFilter() {
            public boolean accept(File file) {
                String path = file.getAbsolutePath();
                return path.matches(".*target.*WEB-INF.*") && !path.matches(".*war.work.*");
            }
        });
        Collection<File> files = FileUtils.listFiles(new File(userDir), filter, TrueFileFilter.INSTANCE);
        if (files.isEmpty()) {
            System.err.println("No directory!");
            System.exit(1);
        }
        if (files.size() > 1) {
            System.err.println("More than one file! " + files);
        }
        File file = files.iterator().next();
        //String baseDir = userDir+File.separator+"src"+File.separator+"main"+File.separator+"webapp";
        return file.getParentFile().getParentFile().getAbsolutePath();
    }

    private File findWarFile() {
        String userDir = getHalfpipeDir();
        Collection<File> files = FileUtils.listFiles(new File(userDir), new RegexFileFilter(".*\\.war"), TrueFileFilter.INSTANCE);
        if (files.isEmpty()) {
            System.err.println("No directory!");
            System.exit(1);
        }
        if (files.size() > 1) {
            System.err.println("More than one file! " + files);
        }
        return getFirst(files, null);
    }

    private String getHalfpipeDir() {
        return System.getProperty("user.dir") + File.separator + ".halfpipe";
    }

    private boolean isOneJar() {
        Class<?> klass = null;
        try {
            klass = Class.forName("OneJar");
        } catch (ClassNotFoundException e) { /*ignore*/ }

        return klass != null;
    }


    private static void waitIndefinitely() {
        Object lock = new Object();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException exception) {
                throw new Error("InterruptedException on wait Indefinitely lock:" + exception.getMessage(),
                        exception);
            }
        }
    }

}
