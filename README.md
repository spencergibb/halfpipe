32º Halfpipe
====================

HTTP+JSON Services using industry best Java libraries.
Spring enabled, with embedded tomcat.  Inspired by [dropwizard](http://dropwizard.codahale.com/).

Features
-----
- Maven
- [Embedded tomcat](http://tomcat.apache.org/maven-plugin-2/executable-war-jar.html)
- Spring enabled (no or little XML)
    - mvc [no xml] (http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/)
    - [java security, not xml](http://blog.springsource.org/2011/08/01/spring-security-configuration-with-scala/), see [scala example](https://github.com/32degrees/halfpipe/tree/master/scala-example)
    - data
    - integration
    - TODO: quiet spring logging?
- Multilingual java/Scala
    - [scala-spring](https://github.com/ewolff/scala-spring), see [scala example](https://github.com/32degrees/halfpipe/tree/master/scala-example)
- Guava integration
- Validation
- Commands
- Jersey Metrics
    - Health Checks
    - web metrics
    - TODO: tomcat metrics
    - TODO: spring metrics, including forking metrics-spring which is no longer maintained
- Spring command line [spring shell](http://www.springsource.org/spring-shell/)
    - TODO: Scala repl [via scala maven plugin](http://davidb.github.com/scala-maven-plugin/example_console.html)
    - TODO: Yeoman integration?
- Dynamic Config [Archaius](https://github.com/Netflix/archaius)
    - TODO: config classes
- Java [maven archetype](http://maven.apache.org/archetype/maven-archetype-plugin/advanced-usage.html)
- TODO: Scala [maven archetype](http://maven.apache.org/archetype/maven-archetype-plugin/advanced-usage.html)
- TODO: Service registry and loadbalancer [Eureka](https://github.com/Netflix/eureka)
- TODO: upload to [sonatype](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)

Examples
-----
To run the examples the main class is thirtytwo.degrees.halfpipe.cli.HalfpipeRunner

- 'server' runs the tomcat server
- empty args runs the interactive shell with custom commands loaded
- in the examples try with 'hello' as the argument
- you can also run the examples using your ide as a normal web project
- mvn install, the try mvn archetype:generate -Dfilter=halfpipe-archetype-java
