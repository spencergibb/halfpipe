package halfpipe.jetty;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * A {@link org.eclipse.jetty.servlet.ServletHolder} subclass which removes the synchronization around servlet initialization
 * by requiring a pre-initialized servlet holder.
 */
public class NonblockingServletHolder extends ServletHolder {
    private final Servlet servlet;

    public NonblockingServletHolder(Servlet servlet) {
        super(servlet);
        this.servlet = servlet;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof NonblockingServletHolder) && (this.compareTo(o) == 0);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + ((servlet != null) ? servlet.hashCode() : 0);
        return result;
    }

    @Override
    public Servlet getServlet() throws ServletException {
        return servlet;
    }

    @Override
    public void handle(Request baseRequest,
                       ServletRequest request,
                       ServletResponse response) throws ServletException, IOException {
        final boolean asyncSupported = baseRequest.isAsyncSupported();
        if (!isAsyncSupported()) {
            baseRequest.setAsyncSupported(false);
        }
        try {
            servlet.service(request, response);
        } finally {
            baseRequest.setAsyncSupported(asyncSupported);
        }
    }
}
