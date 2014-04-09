package crash.commands.base

import org.crsh.cli.Usage
import org.crsh.cli.Command
import org.crsh.cli.Argument

import org.crsh.cli.Required
import org.crsh.text.ui.UIBuilder

@Usage("Spring commands")
class spring {

  @Usage("list the beans")
  @Command
  public void ls() {
    UIBuilder ui = new UIBuilder()
    ui.table() {
      row(decoration: bold, foreground: black, background: white) {
        label("BEAN"); label("TYPE"); label("VALUE")
      }
      //context.attributes.beans.each { key, value ->
        def attributes = context.attributes
        def springCtxt = context.attributes['spring.beanfactory']
      springCtxt.getBeanDefinitionNames().each { name ->
        beanName = name
        value = springCtxt.getBean(name)
        row() {
          //label(value: beanName, foreground: red); label(value?.getClass().simpleName); label("" + value)
          label(beanName); label(value?.getClass().simpleName); label("" + value)
        }
      }
    }
    context.writer.print(ui);
  }

  @Usage("determines if the specified bean is a singleton or not")
  @Command
  public String singleton(@Usage("the bean name") @Argument(name = 'bean name') @Required String name) {
    return "Bean $name is ${context.attributes['spring.beanfactory'].isSingleton(name) ? '' : 'not '}a singleton";
  }
}
