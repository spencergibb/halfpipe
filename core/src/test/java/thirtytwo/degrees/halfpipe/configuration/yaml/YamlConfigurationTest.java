package thirtytwo.degrees.halfpipe.configuration.yaml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

/**
 * User: spencergibb
 * Date: 10/13/12
 * Time: 7:44 PM
 */
public class YamlConfigurationTest {
    public static final String YAML =
                    "number: 123456\n"+
                    "boolean: true\n"+
                    "date: 2001-11-23 15:03:17 -5\n"+
                    "user: ed\n"+
                    "fatal:\n"+
                    "  err: Unknown variable 'bar'\n"+
                    "  type: mytype  \n"+
                    "  child: \n"+
                    "    grandchild: mygrandchild\n"+
                    "array: \n" +
                    "  - 1\n" +
                    "  - 2\n"+
                    "stack:\n"+
                    "  - file: TopClass.py\n"+
                    "    line: 23\n"+
                    "    code: |-\n"+
                    "      x = MoreObject('345')\n"+
                    "  - file: MoreClass.py\n"+
                    "    line: 58\n"+
                    "    code: |-\n"+
                    "      foo = bar:\n";

    @Test
    public void testParse() throws ConfigurationException {
        YamlConfiguration config = new YamlConfiguration();
        config.load(new StringReader(YAML));

        String date = config.getString("date");
        assertThat("date prop is bad", date, is("2001-11-23 15:03:17 -5"));

        int number = config.getInt("number");
        assertThat("number prop is bad", number, is(123456));

        boolean bool = config.getBoolean("boolean");
        assertThat("boolean prop is bad", bool, is(true));

        String type = config.getString("fatal.type");
        assertThat("fatal.type prop is bad", type, is("mytype"));

        String grandchild = config.getString("fatal.child.grandchild");
        assertThat("fatal.child.grandchild prop is bad", grandchild, is("mygrandchild"));

        List<Long> array = config.getList(Long.class, "array");
        assertThat("array prop is bad", array, hasItems(1L, 2L));
    }
}
