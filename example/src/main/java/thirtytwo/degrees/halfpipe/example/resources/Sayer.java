package thirtytwo.degrees.halfpipe.example.resources;

import com.netflix.config.DynamicStringProperty;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 5:52 PM
 */
@Service
public class Sayer {

    @Inject @Named("helloText")
    DynamicStringProperty helloText;

    public String hello() {
        return helloText.get();
    }
}
