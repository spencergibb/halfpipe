package halfpipe.swagger;

import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import lombok.Data;

import static halfpipe.properties.DynamicProp.*;

/**
 * User: spencergibb
 * Date: 4/21/14
 * Time: 4:02 PM
 */
@ArchaiusProperties("halfpipe.swagger")
@Data
public class SwaggerProperties {
    private DynaProp<String> apiUrl = prop("http://localhost:8080/api-docs");
    private DynaProp<String> apiVersion = prop("0");
    private DynaProp<String> basePath;
}
