package thirtytwo.degrees.halfpipe.configuration.convert;

import org.springframework.core.convert.converter.Converter;

import java.util.TimeZone;

/**
 * User: spencergibb
 * Date: 10/14/12
 * Time: 9:12 PM
 */
public class StringToTimeZoneConverter implements Converter<String, TimeZone> {
    @Override
    public TimeZone convert(String source) {
        return TimeZone.getTimeZone(source);
    }
}
