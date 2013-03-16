<html>
<body>
<h2>Hello Webapp freemarker!</h2>
<ul>
    <li><a href="/ws/hello">/ws/hello</a></li>
    <li><a href="/hello">/hello</a></li>
    <li><a href="/hellows">/hellows (Spring MVC Web Service)</a></li>
    <li><a href="/mgmt/ping">/mgmt/ping</a></li>
    <li><a href="/mgmt/healthcheck">/mgmt/healthcheck</a></li>
    <li><a href="/mgmt/metrics?pretty=true">/mgmt/metrics</a></li>
    <li><a href="/mgmt/threads">/mgmt/threads</a></li>

<#list ["winter", "spring", "summer", "autumn"] as x>
<li>${x}</li>
</#list>
</ul>
</body>
</html>
