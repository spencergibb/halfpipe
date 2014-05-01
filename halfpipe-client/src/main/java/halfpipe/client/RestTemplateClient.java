package halfpipe.client;

import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/11/14
 * Time: 4:24 PM
 */
public class RestTemplateClient implements Client {
    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpMethod method = HttpMethod.valueOf(request.method());

        HttpHeaders requestHeaders = new HttpHeaders();
        for (String header: request.headers().keySet()) {
            Collection<String> values = request.headers().get(header);
            if (values != null && !values.isEmpty())
                requestHeaders.put(header, new ArrayList<>(values));
        }

        HttpEntity<byte[]> httpEntity = new HttpEntity<>(request.body(), requestHeaders);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(request.url(), method, httpEntity, byte[].class);

        Map<String, Collection<String>> headers = new HashMap<>();

        for (String header: responseEntity.getHeaders().keySet()) {
            headers.put(header, responseEntity.getHeaders().get(header));
        }

        return Response.create(responseEntity.getStatusCode().value(), "", headers, responseEntity.getBody());
    }
}
