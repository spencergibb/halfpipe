package thirtytwo.degrees.halfpipe.web.admin;

import com.yammer.metrics.reporting.PingServlet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: spencer
 * Date: 9/21/12
 * Time: 6:48 PM
 */
@Controller
public class PingController extends PingServlet {

    @RequestMapping("/admin/ping")
    public void admin(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doGet(req, res);
    }

}
