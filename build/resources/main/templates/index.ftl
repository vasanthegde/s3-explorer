<!DOCTYPE html>
<html>
<body>

<#list folder as key>

<a href="/getFiles?path=${key.path}" rel="link">${key.name}</a> <br>

</#list>



</body>
</html>
