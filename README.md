32º Halfpipe
====================

HTTP+JSON Services using industry best Java libraries.
Spring enabled, with embedded tomcat.  Inspired by dropwizard.

Features
-----
- Maven
- [Embedded tomcat](http://tomcat.apache.org/maven-plugin-2/executable-war-jar.html)
- Spring enabled (no or little XML)
    - mvc [no xml] (http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/)
    - TODO: [java security, not xml](http://blog.springsource.org/2011/08/01/spring-security-configuration-with-scala/)
    - data
    - integration
- Multilingual java/Scala
    - [scala-spring](https://github.com/ewolff/scala-spring)
- Guava integration
- TODO: Validation
- TODO: Commands
- Jersey Metrics
    - Health Checks
    - web metrics
    - TODO: tomcat metrics
    - TODO: spring metrics
- TODO: Spring command line
    - TODO: Scala repl [via scala maven plugin](http://davidb.github.com/scala-maven-plugin/example_console.html)
    - TODO: Yeoman integration
- Config [Archaius](https://github.com/Netflix/archaius)
    - TODO: proxies for config classes?
- TODO: Service registry and loadbalancer [Eureka](https://github.com/Netflix/eureka)