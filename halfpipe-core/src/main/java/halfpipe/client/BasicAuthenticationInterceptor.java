package halfpipe.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Data;

import java.util.Base64;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 3:03 PM
 */
@Data
public class BasicAuthenticationInterceptor implements RequestInterceptor {

    private final String username;
    private final String password;

    @Override
    public void apply(RequestTemplate template) {
        String input = username + ":" + password;
        String header = "Basic " + Base64.getEncoder().encodeToString(input.getBytes());
        template.header("Authorization", header);
    }
}
