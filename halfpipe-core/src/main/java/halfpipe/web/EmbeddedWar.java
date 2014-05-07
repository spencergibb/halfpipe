package halfpipe.web;

import lombok.Data;

/**
 * User: spencergibb
 * Date: 5/5/14
 * Time: 4:42 PM
 */
@Data
public class EmbeddedWar {
    private String path;
    private String location;
    private boolean enable = false;
}
