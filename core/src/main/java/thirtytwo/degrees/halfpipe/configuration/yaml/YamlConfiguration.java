package thirtytwo.degrees.halfpipe.configuration.yaml;

import com.google.common.collect.Lists;
import org.apache.commons.configuration.AbstractHierarchicalFileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.NotImplementedException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.List;

/**
 * User: spencergibb
 * Date: 10/13/12
 * Time: 7:31 PM
 */
public class YamlConfiguration extends AbstractHierarchicalFileConfiguration {

    /**
     * Creates an empty YamlConfiguration object which can be
     * used to synthesize a new json file by adding values and
     * then saving().
     */
    public YamlConfiguration()
    {
    }

    /**
     * Creates a new instance of <code>YamlConfiguration</code> and
     * copies the content of the specified configuration into this object.
     *
     * @param c the configuration to copy
     */
    /*public YamlConfiguration(AbstractHierarchicalConfiguration<? extends ConfigurationNode> c)
    {
        super(c);
    }*/

    /**
     * Creates and loads the configuration from the specified file.
     *
     * @param fileName The name of the json file to load.
     * @throws ConfigurationException Error while loading the json file
     */
    public YamlConfiguration(String fileName) throws ConfigurationException
    {
        super(fileName);
    }

    /**
     * Creates and loads the configuration from the specified file.
     *
     * @param file The json file to load.
     * @throws ConfigurationException Error while loading the json file
     */
    public YamlConfiguration(File file) throws ConfigurationException
    {
        super(file);
    }

    /**
     * Creates and loads the configuration from the specified URL.
     *
     * @param url The location of the json file to load.
     * @throws ConfigurationException Error while loading the json file
     */
    public YamlConfiguration(URL url) throws ConfigurationException
    {
        super(url);
    }


    @Override
    public void load(Reader in) throws ConfigurationException {
        Yaml yaml = new Yaml();
        org.yaml.snakeyaml.nodes.Node node = yaml.compose(in);
        Node root = new Node();
        build(node, root);
        setRootNode(root);
    }

    private void build(org.yaml.snakeyaml.nodes.Node yaml, Node parent) {
        if (yaml instanceof MappingNode) {
            final MappingNode mappingNode = (MappingNode) yaml;
            //System.out.println("writing map with size: "+mappingNode.getValue().size());
            for (NodeTuple tuple : mappingNode.getValue()) {
                Node node = new Node();
                if (tuple.getKeyNode() instanceof ScalarNode) {
                    ScalarNode scalarNode = (ScalarNode) tuple.getKeyNode();
                    String keyValue = scalarNode.getValue();
                    //System.out.println("keyValue: "+keyValue);
                    node.setName(keyValue);
                }
                parent.addChild(node);

                build(tuple.getValueNode(), node);
            }
        } else if (yaml instanceof SequenceNode) {
            SequenceNode sequenceNode = (SequenceNode) yaml;
            //System.out.println("writing sequence with size: "+sequenceNode.getValue().size());
            List<Object> list = Lists.newArrayList();
            for (org.yaml.snakeyaml.nodes.Node node : sequenceNode.getValue()) {
                if (node instanceof ScalarNode) {
                    list.add(getScalarValue((ScalarNode) node));
                } else {
                    YamlConfiguration conf = new YamlConfiguration();
                    Node root = new Node();
                    conf.setRootNode(root);
                    build(node, root);
                    list.add(conf);
                }
            }
            parent.setValue(list);
        } else if (yaml instanceof ScalarNode) {
            final ScalarNode scalarNode = (ScalarNode) yaml;
            parent.setValue(getScalarValue(scalarNode));
        }
    }

    private Object getScalarValue(ScalarNode scalarNode) {
        final String className = scalarNode.getTag().getClassName();
        //System.out.println("writing scalar with tag: "+className+", value: "+scalarNode.getValue());
        if ("bool".equals(className)) {
            return Boolean.parseBoolean(scalarNode.getValue());
        } else if ("int".equals(className)) {
            return Long.parseLong(scalarNode.getValue());
        } else if ("float".equals(className)) {
            return Double.parseDouble(scalarNode.getValue());
        } else if ("null".equals(className)) {
            return null;
        } else {
            return scalarNode.getValue();
        }
    }

    @Override
    public void save(Writer out) throws ConfigurationException {
        throw new NotImplementedException("save is not implemented");
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T> List<T> getList(Class<T> klass, String key) {
        return (List<T>)getList(key);
    }

}
