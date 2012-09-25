package thirtytwo.degrees.halfpipe;

import static org.apache.tomcat.maven.runner.Tomcat7RunnerCli.STAND_ALONE_PROPERTIES_FILENAME;
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

    public static void main(String[] args) throws Exception {
        Tomcat7Runner runner = new Tomcat7Runner();
        runner.runtimeProperties = buildStandaloneProperties();
        runner.run();
    }


    private static Properties buildStandaloneProperties()
            throws IOException
    {
        InputStream is =
                Thread.currentThread().getContextClassLoader().getResourceAsStream( STAND_ALONE_PROPERTIES_FILENAME );
        Properties properties = new Properties();
        properties.load( is );
        return properties;
    }

}
