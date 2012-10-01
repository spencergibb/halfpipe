package thirtytwo.degrees.halfpipe.cli;

import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.shell.SimpleShellCommandLineOptions;
import org.springframework.shell.converters.*;
import org.springframework.shell.core.*;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 9/30/12
 * Time: 6:52 PM
 *
 * see org.springframework.shell.Bootstrap
 */
public class ShellCommand extends Command implements ApplicationContextAware {

    AnnotationConfigApplicationContext ctxt;
    private ConfigurableApplicationContext ctx;
    private JLineShellComponent shell;
    private static StopWatch sw = new StopWatch("Spring Shell");
    private static SimpleShellCommandLineOptions options;

    public void setApplicationContext(ApplicationContext ctxt) {
        if (ctxt instanceof AnnotationConfigApplicationContext)
            this.ctxt = (AnnotationConfigApplicationContext) ctxt;
        else
            throw new IllegalArgumentException("Not a GenericApplicationContext: "+ctxt.getClass());
    }

    @PostConstruct
    public void init() {
        createApplicationContext();
        shell = ctx.getBean("shell", JLineShellComponent.class);
        shell.setApplicationContext(ctx);
        /*shell.setHistorySize(options.historySize);
        if (options.executeThenQuit != null) {
            shell.setPrintBanner(false);
        }*/

        Map<String, CommandMarker> commands = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, CommandMarker.class);
        for (CommandMarker command : commands.values()) {
            shell.getSimpleParser().add(command);
        }

        Map<String, Converter> converters = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, Converter.class);
        for (Converter converter : converters.values()) {
            shell.getSimpleParser().add(converter);
        }
    }

    protected ShellCommand() {
        super("shell", "run the halfpipe command shell");
    }

    @Override
    public void run(CommandLine commandLine) throws Exception {
        shell.start();
        shell.promptLoop();
        ExitShellRequest exitShellRequest = shell.getExitShellRequest();
        if (exitShellRequest == null) {
            // shouldn't really happen, but we'll fallback to this anyway
            exitShellRequest = ExitShellRequest.NORMAL_EXIT;
        }
        shell.waitForComplete();

        ctx.close();

        System.exit(exitShellRequest.getExitCode());
    }


    protected void createApplicationContext() {
        // create parent/base ctx
        createAndRegister(ctxt, StringConverter.class);
        createAndRegister(ctxt, AvailableCommandsConverter.class);
        createAndRegister(ctxt, BigDecimalConverter.class);
        createAndRegister(ctxt, BigIntegerConverter.class);
        createAndRegister(ctxt, BooleanConverter.class);
        createAndRegister(ctxt, CharacterConverter.class);
        createAndRegister(ctxt, DateConverter.class);
        createAndRegister(ctxt, DoubleConverter.class);
        createAndRegister(ctxt, EnumConverter.class);
        createAndRegister(ctxt, FloatConverter.class);
        createAndRegister(ctxt, IntegerConverter.class);
        createAndRegister(ctxt, LocaleConverter.class);
        createAndRegister(ctxt, LongConverter.class);
        createAndRegister(ctxt, ShortConverter.class);
        createAndRegister(ctxt, StaticFieldConverterImpl.class);
        createAndRegisterBeanDefinition(ctxt, JLineShellComponent.class, "shell");
        createAndRegister(ctxt, SimpleFileConverter.class);

        ctxt.scan("org.springframework.shell.commands");
        ctxt.scan("org.springframework.shell.converters");
        ctxt.scan("org.springframework.shell.plugin.support");
        ctxt.refresh();

        ctx = initPluginApplicationContext(ctxt);
        ctx.refresh();
    }

    /**
     * Init plugin ApplicationContext
     *
     * @param annctx parent ApplicationContext in core spring shell
     * @return new ApplicationContext in the plugin with core spring shell's context as parent
     */
    private ConfigurableApplicationContext initPluginApplicationContext(AnnotationConfigApplicationContext annctx) {
        return new ClassPathXmlApplicationContext(
                new String[] { "classpath*:/META-INF/spring/spring-shell-plugin.xml" }, true, annctx);
    }

    protected void createAndRegister(GenericApplicationContext ctxt, Class clazz) {
        createAndRegisterBeanDefinition(ctxt, clazz, null);
    }

    protected void createAndRegisterBeanDefinition(GenericApplicationContext ctxt, Class clazz, String name) {
        RootBeanDefinition rbd = new RootBeanDefinition();
        rbd.setBeanClass(clazz);
        if (name != null) {
            ctxt.registerBeanDefinition(name, rbd);
        }
        else {
            ctxt.registerBeanDefinition(clazz.getSimpleName(), rbd);
        }
    }

}
