<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>反射Demo</title>
</head>
<body>
<h2>方法反射Demo</h2>
<form action="reflect" method="post">
    <label>方法名:</label>
    <input type="text" name="methodName" required><br><br>

    <label>参数值:</label>
    <input type="text" name="paramValue"><br><br>

    <button type="submit">调用该方法</button>
</form>

<% if(request.getAttribute("result") != null) { %>
<h3>结果:</h3>
<p><%= request.getAttribute("result") %></p>
<% } %>

<h3>可用方法列表:</h3>
<ul>
    <li>sayHello()</li>
    <li>greet(String)</li>
    <li>add(int, int)</li>
</ul>
</body>
</html>