package thirtytwo.degrees.halfpipe.cli;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.netflix.config.DynamicStringProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.shell.support.util.VersionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;

import static thirtytwo.degrees.halfpipe.Halfpipe.PROP_APP_NAME;
import static thirtytwo.degrees.halfpipe.Halfpipe.PROP_BANNER_TEXT_FILE;

/**
 * User: spencergibb
 * Date: 10/3/12
 * Time: 4:09 PM
 */
@Order(Ordered.LOWEST_PRECEDENCE-1)
public class HalfpipeBannerProvider implements BannerProvider {

    @Inject @Named(PROP_BANNER_TEXT_FILE)
    DynamicStringProperty bannerTextFile;

    @Inject @Named(PROP_APP_NAME)
    DynamicStringProperty appName;

    public String getBanner() {
        StringBuilder sb = new StringBuilder();
        try {
            URL resource = Resources.getResource(bannerTextFile.get());
            final String banner = Resources.toString(resource, Charsets.UTF_8);
            sb.append(banner);
        } catch (Exception e) {
            e.printStackTrace();  //TODO: handle catch
            throw new IllegalStateException("File not fount "+bannerTextFile.get());
        }
        //sb.append(FileUtils.readBanner(HalfpipeBannerProvider.class, bannerTextFile.get()));
        //sb.append(getVersion()).append(OsUtils.LINE_SEPARATOR);
        sb.append(OsUtils.LINE_SEPARATOR);

        return sb.toString();
    }

    public String getVersion() {
        return versionInfo();
    }

    public String getWelcomeMessage() {
        return "Welcome to " + name() + ". For assistance press or type \"help\" then hit ENTER.";
    }

    public String name() {
        return appName.get();
    }

    public static String versionInfo() {
        Package pkg = HalfpipeBannerProvider.class.getPackage();
        String version = null;
        if (pkg != null) {
            version = pkg.getImplementationVersion();
        }
        return (version != null ? version : "Unknown Version");
    }

}