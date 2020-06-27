<%@ page import="java.util.List" %>
<%@ page import="java.util.TreeMap" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String msg = (String) request.getAttribute("msg");
%>
<html>
  <head>
    <title>微博</title>
  </head>
  <body>
  <form action="home?do=login" method="post">
      <p>登录ID</p>
      <p><input name="id" type="text"/></p>
      <button type="submit">登录</button>
  </form>
  <div>
      <p><%if(msg!=null) out.print(msg);%></p>
  </div>
  </body>
</html>
