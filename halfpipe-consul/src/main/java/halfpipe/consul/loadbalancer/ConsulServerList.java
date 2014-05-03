package halfpipe.consul.loadbalancer;

import com.google.common.base.Function;
import com.netflix.client.config.IClientConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.AbstractServerList;
import com.netflix.loadbalancer.Server;
import halfpipe.consul.client.CatalogClient;
import halfpipe.consul.model.ServiceNode;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.*;
import static halfpipe.util.BeanUtils.*;

/**
 * User: spencergibb
 * Date: 4/21/14
 * Time: 10:08 PM
 */
public class ConsulServerList extends AbstractServerList<Server> {

    private CatalogClient client;
    private String serviceId;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        serviceId = clientConfig.getClientName();
        client = getBean(CatalogClient.class);
    }

    @Override
    public List<Server> getInitialListOfServers() {
        return getServers();
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        return getServers();
    }

    private List<Server> getServers() {
        List<ServiceNode> nodes = client.getServiceNodes(serviceId);
        if (nodes == null || nodes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Collection<Server> servers = transform(nodes, new Function<ServiceNode, Server>() {
            @Nullable
            @Override
            public Server apply(@Nullable ServiceNode node) {
                return new Server(node.getAddress(), node.getServicePort());
            }
        });

        return newArrayList(servers);
    }

    public static void setServiceListClass(String serviceId) {
        ConfigurationManager.getConfigInstance().setProperty(serviceId+".ribbon.NIWSServerListClassName",
                ConsulServerList.class.getName());
    }
}
