package halfpipe.example;

import halfpipe.HalfpipeWebAppInitializer;
import halfpipe.example.view.ViewContext;

/**
 * User: spencergibb
 * Date: 4/3/13
 * Time: 11:19 AM
 */
public class ExampleWebApp extends HalfpipeWebAppInitializer<Context> {

    @Override
    protected Class<?> getViewContext() {
        return ViewContext.class;
    }
}
