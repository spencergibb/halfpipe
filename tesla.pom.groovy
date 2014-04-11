project {
  modelVersion '4.0.0'
  parent('org.springframework.boot:spring-boot-starter-parent:1.0.1.RELEASE')
  groupId 'net.thirty2'
  artifactId 'halfpipe-parent'
  version '2.0.0-SNAPSHOT'
  name 'Halfpipe'
  url 'https://github.com/32degrees/halfpipe'
  properties {
    'spring.security.version' '3.2.3.RELEASE'
    'lombok.version' '1.12.6'
    'spring-data-rest-version' '2.1.0.M1'
    'jersey.version' '2.7'
    'jetty.version' '9.1.4.v20140401'
    'junit.version' '4.11'
    'jetty-maven-plugin.version' '9.1.4.v20140401'
    'servlet-api.version' '3.1.0'
    'jackson.version' '2.3.3'
    'jetty.port' '8080'
    'joda-time.version' '2.3'
    'spring.version' '4.0.3.RELEASE'
  }
  dependencyManagement {
    dependencies {
      dependency('org.glassfish.jersey:jersey-bom:${jersey.version}') {
        type 'pom'
        scope 'import'
      }
    }
  }
  dependencies {
    dependency('junit:junit:${junit.version}') {
      scope 'test'
    }
    dependency('org.glassfish.jersey.containers:jersey-container-servlet')
    dependency('org.glassfish.jersey.ext:jersey-spring3') {
      exclusions {
        exclusion('org.glassfish.hk2.external:bean-validator')
      }
    }
    dependency('com.fasterxml.jackson.core:jackson-databind:${jackson.version}')
    dependency('com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:${jackson.version}')
    dependency('org.projectlombok:lombok:${lombok.version}') {
      scope 'provided'
    }
    dependency('org.springframework.boot:spring-boot-starter-web') {
      exclusions {
        exclusion('org.springframework.boot:spring-boot-starter-tomcat')
      }
    }
    dependency('org.springframework.boot:spring-boot-starter-jetty')
    dependency('org.springframework.boot:spring-boot-starter-security')
    dependency('org.springframework.boot:spring-boot-starter-actuator')
    dependency('org.springframework.boot:spring-boot-starter-remote-shell') {
      exclusions {
        exclusion('org.codehaus.groovy:groovy')
      }
    }
    dependency('org.codehaus.groovy:groovy-all:2.1.6')
    dependency('joda-time:joda-time:${joda-time.version}')
    dependency('org.hibernate:hibernate-validator')
    dependency('com.netflix.archaius:archaius-core:0.6.0') {
      exclusions {
        exclusion('org.codehaus.jackson:jackson-mapper-asl')
        exclusion('org.codehaus.jackson:jackson-core-asl')
      }
    }
    dependency('org.springframework.boot:spring-boot-starter-data-jpa')
    dependency('org.hsqldb:hsqldb') {
      scope 'runtime'
    }
  }
  repositories {
    repository {
      snapshots {
        enabled 'false'
      }
      id 'spring-milestones'
      name 'Spring Milestones'
      url 'http://repo.spring.io/milestone'
    }
  }
  build {
    finalName 'halfpipe-example'
    plugins {
      plugin('org.springframework.boot:spring-boot-maven-plugin')
      plugin {
        artifactId 'maven-compiler-plugin'
        version '3.1'
        configuration {
          source '1.8'
          target '1.8'
        }
      }
      plugin('org.eclipse.jetty:jetty-maven-plugin:${jetty-maven-plugin.version}') {
        configuration {
          webApp {
            contextPath '/'
          }
          stopKey '1'
          stopPort '9999'
          connectors {
            connector(implementation:'org.eclipse.jetty.server.nio.SelectChannelConnector') {
              forwarded 'true'
              port '${jetty.port}'
            }
          }
        }
      }
    }
  }
}
