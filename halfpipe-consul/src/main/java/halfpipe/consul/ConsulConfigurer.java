package halfpipe.consul;

import feign.ribbon.LoadBalancingTarget;
import halfpipe.client.ClientConfigurer;

import java.net.URI;

/**
 * User: spencergibb
 * Date: 4/21/14
 * Time: 10:14 PM
 */
public class ConsulConfigurer extends ClientConfigurer {

    protected <T> LoadBalancingTarget<T> loadBalance(Class<T> type, String schemeName) {
//        Target.HardCodedTarget<PostClient> target = new Target.HardCodedTarget<>(PostClient.class, "http://postServer");
//        PostClient postClient = ObjectGraph.create(/*new Feign.Defaults(), */client, new RibbonModule()).get(Feign.class).newInstance(target);
        //return client.target(PostClient.class, "http://localhost:8080");
        String name = URI.create(schemeName).getHost();
        //TODO: figure out how to set this property dynamically
        //System.setProperty(name + ".ribbon.NIWSServerListClassName", ConsulServerList.class.getName());
        LoadBalancingTarget<T> target = LoadBalancingTarget.create(type, schemeName);
        return target;
    }
}
