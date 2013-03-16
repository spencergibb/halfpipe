package thirtytwo.degrees.halfpipe.example;

import thirtytwo.degrees.halfpipe.Application;
import thirtytwo.degrees.halfpipe.example.view.ViewContext;

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
