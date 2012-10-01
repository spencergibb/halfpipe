package thirtytwo.degrees.halfpipe.cli;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;
import static thirtytwo.degrees.halfpipe.HalfpipeConfiguration.*;
import static thirtytwo.degrees.halfpipe.cli.HelpUtils.*;

import com.google.common.collect.Maps;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 */
public class CommandRunner {

    public static void main(String[] args) throws Exception {
        if (isHelp(args)) {
            Map<String, Command> byName = setup();
            printRootHelp(byName.values(), getProjectConfigClass());
        } else if (isServer(args)) {
            ServerCommand command = new ServerCommand();
            CommandLine commandLine = getCmdLine(args, command);
            command.run(commandLine);
        } else {
            Map<String, Command> byName = setup();
            Command command = byName.get(args[0]);

            if (command == null) {
                printRootHelp(byName.values(), getProjectConfigClass());
                System.exit(1);
            }

            CommandLine commandLine = getCmdLine(args, command);
            command.run(commandLine);
        }
    }

    private static boolean isServer(String[] args) {
        return args.length == 1 && args[0].equals("server");
    }

    private static CommandLine getCmdLine(String[] args, Command command) throws ParseException {
        Options options = command.getOptionsWithHelp();
        return new GnuParser().parse(options, Arrays.copyOfRange(args, 1, args.length));
    }

    private static Map<String, Command> setup() throws ClassNotFoundException {
        createConfig(false);
        AnnotationConfigApplicationContext rootContext = createContext(PROP_CONFIG_CLASS, false);
        rootContext.registerBeanDefinition(ServerCommand.class.getSimpleName(), new RootBeanDefinition(ServerCommand.class));
        ShellCommand shell = new ShellCommand();
        shell.setApplicationContext(rootContext);
        shell.init();
        //rootContext.registerBeanDefinition(ShellCommand.class.getSimpleName(), new RootBeanDefinition(ShellCommand.class));

        Map<String,Command> commands = rootContext.getBeansOfType(Command.class);

        Map<String, Command> byName = Maps.newLinkedHashMap();
        byName.put("server", commands.get(ServerCommand.class.getSimpleName()));
        byName.put("shell", shell);
        for (Command command: commands.values()) {
            if (!byName.containsKey(command.getName()))
                byName.put(command.getName(), command);
        }
        return byName;
    }

    private static boolean isHelp(String[] args) throws ParseException {
        Options helpOptions = new Options();
        addHelp(helpOptions);
        CommandLine helpLine = new HelpParser().parse(helpOptions, args);

        return args == null || args.length == 0 || helpLine.hasOption("help");
    }

    private static Class<?> getProjectConfigClass() throws ClassNotFoundException {
        return getConfigClass(PROP_CONFIG_CLASS);
    }

}
