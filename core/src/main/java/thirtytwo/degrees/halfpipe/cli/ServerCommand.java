package thirtytwo.degrees.halfpipe.cli;

import static org.apache.tomcat.maven.runner.Tomcat7RunnerCli.STAND_ALONE_PROPERTIES_FILENAME;

import static com.google.common.collect.Iterators.*;
import static com.google.common.base.Predicates.*;

import com.google.common.base.Optional;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.maven.runner.Tomcat7Runner;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 11:44 PM
 */
public class ServerCommand extends Command {

    protected ServerCommand() {
        super("server", "run halfpipe in tomcat http server");
    }

    @Override
    public void run(CommandLine commandLine) throws Exception {
        System.out.println("Starting Server");

        Properties sysprops = System.getProperties();
        Optional<String> propname = tryFind(forEnumeration((Enumeration<String>) sysprops.propertyNames()), containsPattern("one-jar"));

        if (propname.isPresent()) {
            System.out.println("in one-jar");
            Tomcat tomcat = new Tomcat();
            String userDir = System.getProperty("user.dir");
            String baseDir = userDir+File.separator+".halfpipe/binlibs/halfpipe-example.war";
            Context context = tomcat.addWebapp("", baseDir);
            System.setProperty("tomcat.util.scan.DefaultJarScanner.jarsToSkip", "*.jar");
            StandardJarScanner jarScanner = new StandardJarScanner(){
                @Override
                public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback, Set<String> jarsToSkip) {
                    System.out.println("scanning classloader: "+classloader);
                    super.scan(context, classloader, callback, jarsToSkip); //TODO: implement .scan
                }
            };
            jarScanner.setScanClassPath(false);
            /*JarScanner jarScanner = new JarScanner(){
                @Override
                public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback, Set<String> jarsToSkip) {
                    //TODO: implement .scan
                }
            };*/
            context.setJarScanner(jarScanner);
            context.setReloadable(false);
            //https://github.com/grails/grails-core/blob/master/grails-plugin-tomcat/src/main/groovy/org/grails/plugins/tomcat/InlineExplodedTomcatServer.groovy
            ClassLoader classLoader = getClass().getClassLoader();
            System.out.println("setting classloader: "+classLoader);
            TomcatLoader loader = new TomcatLoader(classLoader);
            loader.setContainer(context);
            context.setLoader(loader);
            tomcat.start();
            waitIndefinitely();

        /*Properties properties = buildStandaloneProperties();
        if (properties != null) {
            System.out.println("starting exec jar");
            Tomcat7Runner runner = new Tomcat7Runner();
            runner.runtimeProperties = properties;
            runner.run();*/
        } else {
            System.out.println("starting dev");
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
                System.err.println("More than one file! "+files);
            }
            Tomcat tomcat = new Tomcat();
            File file = files.iterator().next();
            String baseDir = file.getParentFile().getParentFile().getAbsolutePath();
            //String baseDir = userDir+File.separator+"src"+File.separator+"main"+File.separator+"webapp";
            tomcat.addWebapp("", baseDir);
            tomcat.start();
            waitIndefinitely();

        }
    }


    private static void waitIndefinitely()
    {
        Object lock = new Object();

        synchronized ( lock )
        {
            try
            {
                lock.wait();
            }
            catch ( InterruptedException exception )
            {
                throw new Error( "InterruptedException on wait Indefinitely lock:" + exception.getMessage(),
                        exception );
            }
        }
    }

/*
    private static Properties buildStandaloneProperties()
            throws IOException {
        InputStream is =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(STAND_ALONE_PROPERTIES_FILENAME);
        if (is == null)
            return null;
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }
*/


}
