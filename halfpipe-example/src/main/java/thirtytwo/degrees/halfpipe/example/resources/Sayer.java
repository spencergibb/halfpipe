package thirtytwo.degrees.halfpipe.example.resources;

import com.netflix.config.DynamicStringProperty;
import com.yammer.metrics.annotation.Timed;
import org.springframework.stereotype.Service;
import thirtytwo.degrees.halfpipe.example.ExampleConfiguration;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 5:52 PM
 */
@Service
public class Sayer {

    @Inject
    ExampleConfiguration config;

    @Timed
    public String hello() {
        return config.helloText.get();
    }
}
