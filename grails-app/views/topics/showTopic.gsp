<%--
  Created by IntelliJ IDEA.
  User: benno
  Date: 4/9/13
  Time: 9:05 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
    <h1>${topic.title}</h1>

    <g:if test="${topic.collectFromSender}">
        <p>Collect from sender: ${topic.collectFromSender}</p>
    </g:if>

    <g:each in="${topic.emails}" var="email">
        <p>
            <g:link controller="emails" action="show" id="${email.id}">${email.sender.contact.name}:</g:link>
            <br/>
            <g:text value="${email.text}" />
            <g:each in="${email.attachments}" var="attachment">
                <p>
                    <g:link controller="attachment" action="download" id="${attachment.id}">${attachment.name}</g:link>
                </p>
            </g:each>
        </p>
    </g:each>
</body>
</html>