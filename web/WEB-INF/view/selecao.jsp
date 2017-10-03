<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="description" content="">
        <meta name="author" content="n2s">
        <link rel="icon" href="favicon.ico">
        <title>Darwin - Sistema de Gerenciamento de Seleções</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <link type="text/css" rel="stylesheet" href="css/design.css"/>
        <link type="text/css" rel="stylesheet" href="css/timeline.css"/>
    </head>

    <body>
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>                        
                    </button>
                    <a class="navbar-brand" href="#">Darwin</a>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav navbar-right">           
                      <li><a href="logout.jsp" title="sair do sistema"><span class="glyphicon glyphicon-log-out"></span> Sair</a></li>
                    </ul>
                </div>
            </div>
        </nav>
      
        <div class="container-fluid text-center">    
            <div class="row content">
                <div class="col-sm-2 sidenav">
                </div>
                <!-- Menu lateral esquerdo -->

                <!-- Menu central -->
                <div class="col-sm-8 text-left">
                    <c:if test="${not empty selecao}">
                        <h2>
                            <c:out value="${selecao.titulo}"></c:out><br>
                        </h2>
                        <p>
                            <c:out value="${selecao.descricao}"></c:out>
                            </p>    
                            <ul class="timeline">
                                <li>
                                    <div class="timeline-badge"><i class="glyphicon glyphicon-pencil"></i></div>
                                    <div class="timeline-panel">
                                        <div class="timeline-heading">
                                            <h4 class="timeline-title">
                                                <c:out value="${selecao.inscricao.titulo}"></c:out>
                                            </h4>
                                        </div>
                                        <div class="timeline-body">
                                            <p>
                                                <c:out value="${selecao.inscricao.descricao}"></c:out>
                                            </p>
                                        </div>
                                    </div>
                                </li>
                                <c:forEach var="etapa" varStatus="" items="${selecao.etapas}"> 
                                    <li>
                                        <div class="timeline-badge danger"><i class="glyphicon glyphicon-eye-open"></i></div>
                                        <div class="timeline-panel">
                                            <div class="timeline-heading">
                                                <h4 class="timeline-title">
                                                    <c:out value="${etapa.titulo}"></c:out>
                                                </h4>
                                            </div>
                                            <div class="timeline-body">
                                                <p>
                                                    <c:out value="${etapa.descricao}"></c:out>
                                                </p>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                    </c:if>
                </div>
                <!-- Menu central -->

        </div>
    </div>
    

    <!-- Rodapé -->
    <footer class="text-muted">
      <div class="container">

      </div>
    </footer>
    <!-- Rodapé -->

  </body>
</html>
