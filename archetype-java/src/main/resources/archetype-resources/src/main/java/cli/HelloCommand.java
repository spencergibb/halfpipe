#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.*;
import org.springframework.stereotype.Component;
import ${package}.resources.Sayer;

import javax.inject.Inject;

@Component
public class HelloCommand implements CommandMarker {

    @Inject
    Sayer sayer;

    @CliAvailabilityIndicator({"hello"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "hello", help = "Say hello to the shell")
    public String hello(
            @CliOption(key = "message", mandatory = true, help = "The hello world message")
            String message,

            @CliOption(key = "more", mandatory = false,
                    help = "Have some more to say while saying hello",
                    specifiedDefaultValue="")
            String more)
    {
        StringBuilder hello = new StringBuilder(sayer.hello());
        hello.append(" with this message, "+message);
        if (!StringUtils.isBlank(more)) {
            hello.append(" "+more);
        }
        System.out.println("${symbol_escape}n${symbol_escape}n${symbol_escape}n${symbol_escape}n"+hello+"${symbol_escape}n${symbol_escape}n${symbol_escape}n${symbol_escape}n");
        return null;//hello.toString();
    }
}
