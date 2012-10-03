#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<html>
<body>
<h2>${symbol_dollar}{hello}</h2>
<ul>
    <li><a href="/ws/hello">/ws/hello</a></li>
    <li><a href="/hello">/hello</a></li>
    <li><a href="/mgmt/ping">/mgmt/ping</a></li>
    <li><a href="/mgmt/healthcheck">/mgmt/healthcheck</a></li>
    <li><a href="/mgmt/metrics?pretty=true">/mgmt/metrics</a></li>
    <li><a href="/mgmt/threads">/mgmt/threads</a></li>
</ul>
</body>
</html>
