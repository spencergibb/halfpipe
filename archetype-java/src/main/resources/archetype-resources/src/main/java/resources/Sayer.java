#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.resources;

import com.netflix.config.DynamicStringProperty;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;

@Service
public class Sayer {

    @Inject @Named("helloText")
    DynamicStringProperty helloText;

    public String hello() {
        return helloText.get();
    }
}
