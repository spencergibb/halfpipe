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
    - spring metrics, TODO: including forking metrics-spring which is no longer maintained. Needed?
- Dynamic Config [Archaius](https://github.com/Netflix/archaius)
    - config classes
    - dynamic reload of config files
    - yaml or property config files
    - callbacks when config property changes
    - Custom sources: consul.io kv, Jdbc, DynamoDb, Zookeeper, jclouds, etc...
    - TODO: consul deployment context aware configuration
    - TODO: validate archaius config
- Client
    - Service Discovery (consul.io/TODO: Eureka)
    - Feign
    - Hystrix
    - Ribbon
- Logging
    - Logback
    - yaml/properties based configuration
    - dynamically update log levels (based on Archaius)
- Local dev environment
    - docker
    - dns resolution of docker containers (skydns/skydock)
    - TODO: simple auto scaler?
    - TODO: asgard points to simple auto scaler and consul (instead of eureka)?

- TODO: metrics influxdb
- TODO: coda hale metrics to influxdb
- TODO: turbine vagrant deploy
- TODO: hystrix dashboard vagrant deploy
- TODO: influxdb vagrant deploy
- TODO: upload to [sonatype](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)
- TODO: Admin like karyon (Started)
- TODO: research existing docker platforms: http://stackoverflow.com/questions/18285212/how-to-scale-docker-containers-in-production

Old list:

- Model example after RSS recipe and flux capacitor see [32degrees/recipes-rss](https://github.com/32degrees/recipes-rss)
- TODO: Netflix OSS Platform [flux capacitor example app](https://github.com/cfregly/fluxcapacitor)
- TODO: Ansible playbooks
- TODO: metrics aws cloudwatch
- TODO: Spring command line? [spring shell](http://www.springsource.org/spring-shell/)

