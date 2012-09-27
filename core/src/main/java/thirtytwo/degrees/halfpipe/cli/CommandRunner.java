package thirtytwo.degrees.halfpipe.cli;

import static thirtytwo.degrees.halfpipe.Halfpipe.PROP_CONFIG_CLASS;
import static thirtytwo.degrees.halfpipe.HalfpipeConfiguration.*;

import com.google.common.collect.Maps;
import org.apache.commons.cli.*;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 10:41 PM
 */
public class CommandRunner {

    public static void main(String[] args) throws Exception {
        createConfig(false);
        ApplicationContext rootContext = createContext(PROP_CONFIG_CLASS);
        Map<String,Command> commands = rootContext.getBeansOfType(Command.class);
        Map<String, Command> byName = Maps.newHashMap();
        for (Command command: commands.values()) {
            byName.put(command.getName(), command);
        }

        if (isHelp(args)) {
            UsagePrinter.printRootHelp(commands.values(), getProjectConfigClass());
        } else {
            Command command = byName.get(args[0]);

            if (command == null) {
                UsagePrinter.printRootHelp(commands.values(), getProjectConfigClass());
                System.exit(1);
            }

            Options options = command.getOptionsWithHelp();
            CommandLine commandLine = new GnuParser().parse(options, Arrays.copyOfRange(args, 1, args.length));
            command.run(commandLine);
        }
    }

    private static Class<?> getProjectConfigClass() throws ClassNotFoundException {
        return getConfigClass(PROP_CONFIG_CLASS);
    }

    private static boolean isHelp(String[] arguments) {
        return (arguments.length == 0) ||
                ((arguments.length == 1) &&
                        ("-h".equals(arguments[0]) ||
                                "--help".equals(arguments[0])));
    }
}
