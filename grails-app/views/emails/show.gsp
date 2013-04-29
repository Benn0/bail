<%--
  Created by IntelliJ IDEA.
  User: benno
  Date: 4/9/13
  Time: 9:13 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
    <table>
        <tr>
            <th>Header Name</th>
            <th>Header Value</th>
        </tr>
        <g:each in="${headers}" var="header">
            <tr>
                <td>${header.key.encodeAsHTML()}</td>
                <td>${header.value.encodeAsHTML()}</td>
            </tr>
        </g:each>
    </table>

    <table>
        <tr>
            <th>Content Type</th>
            <th>Headers</th>
            <th>Content</th>
        </tr>
        <g:each in="${parts}" var="part">
            <tr>
                <td>${part.contentType}</td>
                <td>${part.headers*.encodeAsHTML().join("<br/>")}</td>
                <td>${part.content}</td>
            </tr>
        </g:each>
    </table>

</body>
</html>