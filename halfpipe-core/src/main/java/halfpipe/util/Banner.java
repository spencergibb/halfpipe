package halfpipe.util;

import halfpipe.cli.HalfpipeBannerProvider;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * User: gibbsb
 * Date: 5/1/13
 * Time: 12:42 AM
 */
public class Banner {
    public static void logBanner(org.slf4j.Logger log, ConfigurableApplicationContext context) {
        HalfpipeBannerProvider provider = context.getBean(HalfpipeBannerProvider.class);
        log.info("\n" + provider.getBanner());
    }
}
