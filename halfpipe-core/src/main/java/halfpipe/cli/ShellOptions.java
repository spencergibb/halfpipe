package halfpipe.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.springframework.shell.support.logging.HandlerUtils;

/**
 * TODO: move to config
 */
public class ShellOptions {

    private static final Logger LOGGER = HandlerUtils.getLogger(ShellOptions.class);
    private static final int DEFAULT_HISTORY_SIZE = 3000;
    protected String[] executeThenQuit = null;
    protected Map<String, String> extraSystemProperties = new HashMap<String, String>();
    protected int historySize = DEFAULT_HISTORY_SIZE;

    public ShellOptions(String[] args) throws IOException {
        List<String> commands = new ArrayList<String>();
        int i = 0;
        while (i < args.length) {
            String arg = args[i++];
            if (arg.equals("--profiles")) {
                try {
                    String profiles = args[i++];
                    extraSystemProperties.put("spring.profiles.active", profiles);
                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.warning("No value specified for --profiles option");
                }
            }
            else if (arg.equals("--cmdfile")) {
                try {
                    File f = new File(args[i++]);
                    commands.addAll(FileUtils.readLines(f));
                } catch (IOException e) {
                    LOGGER.warning("Could not read lines from command file: " +  e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.warning("No value specified for --cmdfile option");
                }
            }
            else if (arg.equals("--histsize")) {
                try {
                    String histSizeArg = args[i++];
                    int histSize = Integer.parseInt(histSizeArg);
                    if (histSize <= 0) {
                        LOGGER.warning("histsize option must be > 0, using default value of " + DEFAULT_HISTORY_SIZE);
                    } else {
                        historySize = histSize;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warning("Unable to parse histsize value to an integer ");
                } catch (ArrayIndexOutOfBoundsException ae) {
                    LOGGER.warning("No value specified for --histsize option");
                }
            }
            else if (arg.equals("--help")) {
                printUsage();
                System.exit(0);
            }
            else {
                i--;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (; i < args.length; i++) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (args[i].matches(".*\\s+.*")) {
                sb.append('"'+args[i]+'"');
            } else {
                sb.append(args[i]);
            }
        }

        if (sb.length() > 0) {
            String[] cmdLineCommands = sb.toString().split(";");
            for (String s : cmdLineCommands) {
                //add any command line commands after the commands loaded from the file
                commands.add(s.trim());
            }
        }

        if (commands.size() > 0)
            executeThenQuit = commands.toArray(new String[commands.size()]);
    }

    public static boolean containsWhiteSpace(final String testCode){
        if(testCode != null){
            for(int i = 0; i < testCode.length(); i++){
                if(Character.isWhitespace(testCode.charAt(i))){
                    return true;
                }
            }
        }
        return false;
    }

    private static void printUsage(){
        System.out.println("Usage:  --help --histsize [size] --cmdfile [file name] --profiles [comma-separated list of profile names]");
    }
}
