package thirtytwo.degrees.halfpipe.cli;

import static com.google.common.collect.Iterables.*;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Collection;
import java.util.Set;

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

        if (isOneJar()) {
            System.out.println("in one-jar");

            File warFile = findWarFile();

            String baseDir = warFile.getAbsolutePath();
            //String baseDir = userDir+File.separator+".halfpipe/binlibs/halfpipe-example.war";
            System.out.println("baseDir: " + baseDir);

            Tomcat tomcat = new Tomcat();
            Context context = tomcat.addWebapp("", baseDir);
            System.setProperty("tomcat.util.scan.DefaultJarScanner.jarsToSkip", "*.jar");
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
            tomcat.start();
            waitIndefinitely();

        } else {
            System.out.println("starting dev");
            String baseDir = getWebappDir();
            Tomcat tomcat = new Tomcat();
            tomcat.addWebapp("", baseDir);
            tomcat.start();
            waitIndefinitely();

        }
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
        String userDir = System.getProperty("user.dir") + File.separator + ".halfpipe";
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
