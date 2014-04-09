32º Halfpipe 2
====================

HTTP+JSON Services using industry best Java libraries.
Based on [Spring Boot](http://projects.spring.io/spring-boot/) with Netflix OSS integration.
Originally inspired by [dropwizard](http://dropwizard.io)

Features
-----
- Maven
- Spring 4 enabled (no XML)
    - TODO: data
    - TODO: integration
- Spring Boot
    - Spring Boot Auto Configuration to automatically configure halfpipe/Netflix OSS components
    - TODO: Commands
- Multilingual java TODO: scala/groovy
- TODO: Guava integration
- Validation
- Jersey 2
- Metrics
    - Health Checks
    - web metrics
    - spring metrics, TODO: including forking metrics-spring which is no longer maintained
- Dynamic Config [Archaius](https://github.com/Netflix/archaius)
    - config classes
    - dynamic reload of config files
    - TODO: yaml or property config files
    - TODO: callbacks when config property changes
    - TODO: validate config
- configure logback like dropwizard, in config file

- Model example after RSS recipe and flux capacitor see [32degrees/recipes-rss](https://github.com/32degrees/recipes-rss)
- TODO: Netflix OSS Platform [flux capacitor example app](https://github.com/cfregly/fluxcapacitor)
    - TODO: Service registry and loadbalancer [Eureka](https://github.com/Netflix/eureka)
- TODO: upload to [sonatype](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)
- TODO: Feign
- TODO: Feign/Hysterix
- TODO: coda hale metrics to hysterix?
- TODO: Cloud formation templates?
- TODO: Admin like karyon
- TODO: metrics aws cloudwatch
- TODO: metrics graphite
- TODO: Spring command line? [spring shell](http://www.springsource.org/spring-shell/)

