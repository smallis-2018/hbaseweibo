<%@ page import="java.util.TreeMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    TreeMap<String, String> infoMap = (TreeMap<String, String>) request.getAttribute("infoMap");
    TreeMap<String, String> followMap = (TreeMap<String, String>) request.getAttribute("followMap");
    TreeMap<String, String> fansMap = (TreeMap<String, String>) request.getAttribute("fansMap");
    TreeMap<String, String> strangerMap = (TreeMap<String, String>) request.getAttribute("strangerMap");
%>
<html>
<head>
    <title>主页</title>
</head>
<body>
<div style="float:right;margin-left: 25px;margin-right: 25px;">
    <p>用户名：
    <%
        if (infoMap != null) {
            for (String s : infoMap.keySet()) {
                out.print(infoMap.get(s));
            }
        }
    %>
    </p>
</div>
<div style="float:left;margin-left: 25px;margin-right: 25px;">
    <p>关注列表：</p>
    <ol>
        <%
            if (followMap != null) {
                for (String s : followMap.keySet()) {
        %>
        <li><%out.print(followMap.get(s));%></li>
        <from action="home?do=qxgz" method="post">
            <button name="qxgz" value=<%=s%> type="submit">取消关注</button>
        </from>
        <%
                }
            }
        %>
    </ol>

</div>
<div style="float:left;margin-left: 25px;margin-right: 25px;">
    <p>粉丝列表：</p>
    <ol>
        <%
            if (fansMap != null) {
                for (String s : fansMap.keySet()) {
        %>
        <li><%out.print(fansMap.get(s));%></li>
        <%
                }
            }
        %>
    </ol>
</div>
<div style="float:left;margin-left: 25px;margin-right: 25px;">
    <p>陌生人列表：</p>
    <ol>
        <%
            if (strangerMap != null) {
                for (String s : strangerMap.keySet()) {
        %>
        <li><%out.print(strangerMap.get(s));%></li>
        <%
                }
            }
        %>
    </ol>
</div>
</body>
</html>
