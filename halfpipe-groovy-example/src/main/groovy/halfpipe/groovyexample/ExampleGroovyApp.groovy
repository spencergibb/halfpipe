package halfpipe.groovyexample

import halfpipe.Application
import halfpipe.groovyexample.view.ViewContext

class ExampleGroovyApp extends Application<Context> {
    public static void main(String[] args) {
        new ExampleGroovyApp().run(args)
    }

    @Override
    protected Class<?> getViewContext() {
        ViewContext.class
    }
}
