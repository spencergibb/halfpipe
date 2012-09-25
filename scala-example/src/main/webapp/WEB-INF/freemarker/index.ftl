<html>
<body>
<h2>Hello Webapp freemarker!</h2>
<ul>
    <li><a href="/ws/hello">/ws/hello</a></li>
    <li><a href="/hello">/hello</a></li>
    <li><a href="/admin/ping">/admin/ping</a></li>
    <li><a href="/admin/healthcheck">/admin/healthcheck</a></li>
    <li><a href="/admin/metrics?pretty=true">/admin/metrics</a></li>
    <li><a href="/admin/threads">/admin/threads</a></li>

<#list ["winter", "spring", "summer", "autumn"] as x>
<li>${x}</li>
</#list>
</ul>
</body>
</html>
