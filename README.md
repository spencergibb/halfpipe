32º Halfpipe 2
====================

HTTP+JSON Services using industry best Java libraries.
Based on [Spring Boot](http://projects.spring.io/spring-boot/) with Netflix OSS integration.
Originally inspired by [dropwizard](http://dropwizard.io)

Features
-----
- Maven
- Spring 4 enabled (no XML)
    - Spring data
    - Spring data rest
    - TODO: integration
- Spring Boot
    - Spring Boot Auto Configuration to automatically configure halfpipe/Netflix OSS components
    - TODO: Commands
- Multilingual java TODO: scala/groovy
- Validation
- JAX-RS: Choice of providers
    - Guava integration (TODO: jersey)
    - Jersey 2
    - Resteasy
- Metrics
    - Health Checks
    - web metrics
    - spring metrics, TODO: including forking metrics-spring which is no longer maintained
- Dynamic Config [Archaius](https://github.com/Netflix/archaius)
    - config classes
    - dynamic reload of config files
    - yaml or property config files
    - callbacks when config property changes
    - TODO: validate config
    - Custom sources: consul.io kv, Jdbc, DynamoDb, Zookeeper, jclouds, etc...
- Client
    - Service Discovery (consul.io/TODO: Eureka)
    - Feign
    - Hysterix
    - Ribbon
- Logging
    - Logback
    - yaml/properties based configuration
    - dynamically update log levels (based on Archaius)

- TODO: Admin like karyon (In progress)
- Model example after RSS recipe and flux capacitor see [32degrees/recipes-rss](https://github.com/32degrees/recipes-rss)
- TODO: Netflix OSS Platform [flux capacitor example app](https://github.com/cfregly/fluxcapacitor)
    - TODO: Service registry [Eureka](https://github.com/Netflix/eureka) and loadbalancer [Ribbon](https://github.com/Netflix/ribbon)
- TODO: upload to [sonatype](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)
- TODO: coda hale metrics to hysterix?
- TODO: Ansible playbooks
- TODO: metrics aws cloudwatch
- TODO: metrics graphite
- TODO: Spring command line? [spring shell](http://www.springsource.org/spring-shell/)

