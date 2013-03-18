package halfpipe.cli;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.shell.converters.*;
import org.springframework.shell.core.*;
import org.springframework.util.StopWatch;
import halfpipe.logging.Log;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import static org.springframework.shell.support.logging.HandlerUtils.*;

/**
 * User: spencergibb
 * Date: 9/30/12
 * Time: 6:52 PM
 * <p/>
 * see org.springframework.shell.Bootstrap
 */
public class Shell {
    private static final Log LOG = Log.forThisClass();

    protected AnnotationConfigApplicationContext ctxt;
    protected ConfigurableApplicationContext ctx;
    protected JLineShellComponent shell;
    protected StopWatch sw = new StopWatch("Spring Shell");
    protected ShellOptions options;

    public Shell(AnnotationConfigApplicationContext context) {
        this.ctxt = context;
        createApplicationContext();
        shell = ctx.getBean("shell", JLineShellComponent.class);
        shell.setApplicationContext(ctx);

        Map<String, CommandMarker> commands = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, CommandMarker.class);
        for (CommandMarker command : commands.values()) {
            shell.getSimpleParser().add(command);
        }

        Map<String, Converter> converters = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, Converter.class);
        for (Converter converter : converters.values()) {
            shell.getSimpleParser().add(converter);
        }
    }

    public void start(String[] args) throws IOException {
        sw.start();
        options = new ShellOptions(args);

        shell.setHistorySize(options.historySize);
        /*if (options.executeThenQuit != null) {
            shell.setPrintBanner(false);
        }*/

        for (Map.Entry<String, String> entry : options.extraSystemProperties.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
        ExitShellRequest exitShellRequest;
        try {
            exitShellRequest = run(options.executeThenQuit);
        } catch (RuntimeException t) {
            throw t;
        } finally {
            flushAllHandlers(Logger.getLogger(""));
        }

        System.out.println();
        System.exit(exitShellRequest.getExitCode());
    }

    protected ExitShellRequest run(String[] executeThenQuit) {

        ExitShellRequest exitShellRequest;

        if (null != executeThenQuit) {
            boolean successful = false;
            exitShellRequest = ExitShellRequest.FATAL_EXIT;

            HalfpipeBannerProvider provider = ctx.getBean(HalfpipeBannerProvider.class);
            LOG.info(provider.getBanner());

            for (String cmd : executeThenQuit) {
                successful = shell.executeCommand(cmd);
                if (!successful)
                    break;
            }

            //if all commands were successful, set the normal exit status
            if (successful) {
                exitShellRequest = ExitShellRequest.NORMAL_EXIT;
            }
        } else {
            shell.start();
            shell.promptLoop();
            exitShellRequest = shell.getExitShellRequest();
            if (exitShellRequest == null) {
                // shouldn't really happen, but we'll fallback to this anyway
                exitShellRequest = ExitShellRequest.NORMAL_EXIT;
            }
            shell.waitForComplete();
        }

        ctx.close();
        sw.stop();

        LOG.debug("Total execution time: {} ms", sw.getLastTaskTimeMillis());
        return exitShellRequest;
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
                new String[]{"classpath*:/META-INF/spring/spring-shell-plugin.xml"}, true, annctx);
    }

    protected void createAndRegister(GenericApplicationContext ctxt, Class clazz) {
        createAndRegisterBeanDefinition(ctxt, clazz, null);
    }

    protected void createAndRegisterBeanDefinition(GenericApplicationContext ctxt, Class clazz, String name) {
        RootBeanDefinition rbd = new RootBeanDefinition();
        rbd.setBeanClass(clazz);
        if (name != null) {
            ctxt.registerBeanDefinition(name, rbd);
        } else {
            ctxt.registerBeanDefinition(clazz.getSimpleName(), rbd);
        }
    }

}
