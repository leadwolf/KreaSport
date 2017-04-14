<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="fr">
  <head>
    <jsp:include page="/layout/head.jsp"/>
    <meta charset="utf-8">
    <title>All races</title>
  </head>
  <body>
      <script type='text/javascript'>
        <c:set var="raceListJSTL" value="${it}"/>
        var raceListString = '<c:out value="${raceListJSTL}"/>';
        var jsonObject = JSON.parse(jsonData);
      </script>
    <jsp:include page="/layout/navbar.jsp"/>
    <div class="container">
      <div class="row">
        <div class="col-md-6 col-md-offset-3">
          <h1>List of races</h1>
          <ul id="raceList" class="list-group">
            <c:forEach var="item" items="${it}">
                <li class="list-group-item">
                    <pre>${item}</pre>
                </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </div>
  </body>
</html>
