package thirtytwo.degrees.halfpipe.cli;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;
import static thirtytwo.degrees.halfpipe.HalfpipeConfiguration.*;

import org.apache.commons.cli.*;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.ListIterator;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 */
public class CommandRunner {

    public static void main(String[] args) throws Exception {
        if (isHelp(args)) {
            Shell shell = getShell();
            shell.start(new String[]{"help"});
        } else if (isServer(args)) {
            ServerCommand command = new ServerCommand();
            command.run(null);
        } else {
            Shell shell = getShell();
            shell.start(args);
        }
    }

    private static boolean isServer(String[] args) {
        return args.length == 1 && args[0].equals("server");
    }

    private static Shell getShell() throws ClassNotFoundException {
        createConfig(false);
        AnnotationConfigApplicationContext rootContext = createContext(PROP_CONFIG_CLASS, false);
        rootContext.registerBeanDefinition(ServerCommand.class.getSimpleName(), new RootBeanDefinition(ServerCommand.class));
        Shell shell = new Shell(rootContext);

        return shell;
    }

    private static boolean isHelp(String[] args) throws ParseException {
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "display help");
        CommandLine helpLine = new HelpParser().parse(helpOptions, args);

        return helpLine.hasOption("help");
    }


    static class HelpParser extends GnuParser {
        @Override
        protected void processOption(String arg, ListIterator iter) throws ParseException {
            try {
                super.processOption(arg, iter);
            } catch (UnrecognizedOptionException e) {
                //ignore so I can parse help options regardless
            }
        }
    }
}
