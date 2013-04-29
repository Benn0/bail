<%--
  Created by IntelliJ IDEA.
  User: benno
  Date: 4/9/13
  Time: 8:42 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
    <g:form controller="topics">
        <g:actionSubmit value="Download E-Mails" action="downloadEmails" />
    </g:form>

    <g:each in="${list}" var="topic">
        <p>
            <g:link action="showTopic" id="${topic.id}">${topic.title}</g:link> (${topic.emails.size()}) <br/>
            Letzte Aktivität: <g:formatDate date="${topic.lastActivity.toDate()}" style="SHORT" />
        </p>
    </g:each>
</body>
</html>