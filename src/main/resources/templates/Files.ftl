<!DOCTYPE html>
<html>
<body>

<p>  file system</p>

<#list folder as key>

<a href="/getFiles?path=${key.path}" rel="link">${key.name}</a> <br>

</#list>

</body>
</html>
