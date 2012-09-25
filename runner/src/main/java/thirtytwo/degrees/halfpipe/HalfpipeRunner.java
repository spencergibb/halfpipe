package thirtytwo.degrees.halfpipe;

import static org.apache.tomcat.maven.runner.Tomcat7RunnerCli.STAND_ALONE_PROPERTIES_FILENAME;

import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.*;
import org.apache.tomcat.maven.runner.Tomcat7Runner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: spencergibb
 * Date: 9/25/12
 * Time: 4:50 AM
 */
public class HalfpipeRunner {

    static Option server = OptionBuilder.withArgName("server")
            .withDescription("run halfpipe server from executable war")
            .create("server");

    static Option help = OptionBuilder.withLongOpt("help")
            .withDescription("help").create('h');

    static Options options = new Options();

    static {
        options.addOption(server).addOption(help);
    }

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new GnuParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            printError("Parsing failed.  Reason: " + e.getMessage());
        }

        if (line.hasOption(help.getOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(getCmdLineSyntax(), options);
            System.exit(0);
        }

        if (line.hasOption(server.getOpt())) {
            Properties properties = buildStandaloneProperties();
            if (properties != null) {
                Tomcat7Runner runner = new Tomcat7Runner();
                runner.runtimeProperties = properties;
                runner.run();
            } else {
                Tomcat tomcat = new Tomcat();
                tomcat.addWebapp("", "/home/gibbsb/workspace/32degrees/halfpipe/example/target/halfpipe-example");
                tomcat.start();
                waitIndefinitely();
            }
        }

        printError("No valid option given");
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


    private static void printError(String error) {
        System.err.println(error);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getCmdLineSyntax(), options);
        System.exit(1);
    }

    public static String getCmdLineSyntax() {
        return "java -jar [path to your exec war jar]";
    }


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

}
