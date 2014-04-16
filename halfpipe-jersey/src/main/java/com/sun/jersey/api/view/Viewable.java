package com.sun.jersey.api.view;

/**
 * User: spencergibb
 * Date: 4/12/14
 * Time: 10:02 PM
 * Karyon backwards compatibility
 */
public class Viewable extends org.glassfish.jersey.server.mvc.Viewable {

    public Viewable(String templateName) throws IllegalArgumentException {
        super(templateName);
    }

    public Viewable(String templateName, Object model) throws IllegalArgumentException {
        super(templateName, model);
    }
}
