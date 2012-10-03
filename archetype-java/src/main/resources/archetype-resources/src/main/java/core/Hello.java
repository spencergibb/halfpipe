#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.core;

public class Hello {
    String hello;
    String to;

    public Hello(String hello, String to) {
        this.hello = hello;
        this.to = to;
    }

    public String getHello() {
        return hello;
    }

    public String getTo() {
        return to;
    }
}
