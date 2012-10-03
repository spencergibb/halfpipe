package thirtytwo.degrees.halfpipe.jersey;

import com.google.common.collect.ImmutableList;
import thirtytwo.degrees.halfpipe.validation.InvalidEntityException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

// see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/jersey
@Provider
public class InvalidEntityExceptionMapper implements ExceptionMapper<InvalidEntityException> {
    //private static final Log LOG = Log.forClass(InvalidEntityExceptionMapper.class);
    private static final int UNPROCESSABLE_ENTITY = 422;

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(InvalidEntityException exception) {
        final StringWriter writer = new StringWriter(4096);
        try {
            writer.write("<html>\n<head>\n");
            writeErrorPageHead(request, writer, 422, "Unprocessable Entity");
            writer.write("</head>\n<body>");
            writeInvalidationErrorPageBody(request,
                    writer,
                    exception.getMessage(),
                    exception.getErrors());
            writer.write("\n</body>\n</html>\n");
        } catch (IOException e) {
            e.printStackTrace();
            //LOG.warn(e, "Unable to generate error page");
        }

        return Response.status(UNPROCESSABLE_ENTITY)
                       .type(MediaType.TEXT_HTML_TYPE)
                       .entity(writer.toString())
                       .build();
    }

    private void writeInvalidationErrorPageBody(HttpServletRequest request, StringWriter writer, String message, ImmutableList<String> errors) throws IOException {
        final String uri = request.getRequestURI();
        writeErrorPageMessage(request, writer, 422, "Unprocessable Entity", uri);

        writer.write("<h2>");
        write(writer, message);
        writer.write("</h2>");

        writer.write("<ul>");
        for (String error : errors) {
            writer.write("<li>");
            write(writer, error);
            writer.write("</li>");
        }
        writer.write("</ul>");
    }

    /* ------------------------------------------------------------ */
    protected void writeErrorPageHead(HttpServletRequest request, Writer writer, int code, String message)
            throws IOException
    {
        writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\n");
        writer.write("<title>Error ");
        writer.write(Integer.toString(code));
        writer.write(' ');
        write(writer,message);
        writer.write("</title>\n");
    }

    /* ------------------------------------------------------------ */
    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks)
            throws IOException
    {
        String uri= request.getRequestURI();

        writeErrorPageMessage(request,writer,code,message,uri);
        if (showStacks)
            writeErrorPageStacks(request,writer);
        writer.write("<hr /><i><small>Powered by Jetty://</small></i>");
        for (int i= 0; i < 20; i++)
            writer.write("<br/>                                                \n");
    }

    /* ------------------------------------------------------------ */
    protected void writeErrorPageMessage(HttpServletRequest request, Writer writer, int code, String message,String uri)
            throws IOException
    {
        writer.write("<h2>HTTP ERROR ");
        writer.write(Integer.toString(code));
        writer.write("</h2>\n<p>Problem accessing ");
        write(writer,uri);
        writer.write(". Reason:\n<pre>    ");
        write(writer,message);
        writer.write("</pre></p>");
    }
    protected void writeErrorPageStacks(HttpServletRequest request, Writer writer)
            throws IOException
    {
        Throwable th = (Throwable)request.getAttribute("javax.servlet.error.exception");
        while(th!=null)
        {
            writer.write("<h3>Caused by:</h3><pre>");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            pw.flush();
            write(writer,sw.getBuffer().toString());
            writer.write("</pre>\n");

            th =th.getCause();
        }
    }

    protected void write(Writer writer,String string)
            throws IOException
    {
        if (string==null)
            return;

        for (int i=0;i<string.length();i++)
        {
            char c=string.charAt(i);

            switch(c)
            {
                case '&' :
                    writer.write("&amp;");
                    break;
                case '<' :
                    writer.write("&lt;");
                    break;
                case '>' :
                    writer.write("&gt;");
                    break;

                default:
                    if (Character.isISOControl(c) && !Character.isWhitespace(c))
                        writer.write('?');
                    else
                        writer.write(c);
            }
        }
    }

}
