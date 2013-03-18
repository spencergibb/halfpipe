package halfpipe.example;

import halfpipe.Application;
import halfpipe.example.view.ViewContext;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 1:39 AM
 */
public class ExampleApp extends Application<Context> {

    public static void main(String args[]) {
        new ExampleApp().run(args);
    }

    @Override
    protected Class<?> getViewContext() {
        return ViewContext.class;
    }
}
