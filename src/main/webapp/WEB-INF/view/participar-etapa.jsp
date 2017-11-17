<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="n2s">
        <link rel="icon" href="favicon.ico">
        <title>Darwin - Sistema de Gerenciamento de Seleções</title>


        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/design.css" />
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-datepicker.css" />
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-datepicker.standalone.css" />
    </head>
    <body>
    <c:import url="elements/menu-superior.jsp" charEncoding="UTF-8"></c:import>
    <div class="container-fluid">
        <div class="row row-offcanvas row-offcanvas-right">
            <c:import url="elements/menu-lateral-esquerdo.jsp" charEncoding="UTF-8"></c:import>
            <div class="col-sm-8">
                <nav class="breadcrumb">
                    <span class="breadcrumb-item">Você está em:</span> 
                    <a class="breadcrumb-item" href="/Darwin">Início</a>
                    <a class="breadcrumb-item" href="/Darwin/selecao/${selecao.codSelecao}">${selecao.titulo}</a>
                	<a class="breadcrumb-item active" href="/Darwin/selecao/${selecao.codSelecao}">${etapa.titulo}</a>
                </nav>

                <h1>Inscrição na etapa</h1>
                <p>Atenção: Os campos abaixo (*) são de preenchimento obrigatório</p>
                <br>
                <div class="form-group">
                    <form method="POST" action="participa">
                    <c:forEach var="documento" items="${etapa.documentacaoExigida}">
                        <label for="${documento}Input">${documento}*</label>
                        <input type="file" name="${documento}" class="form-control" id="${documento}Input" aria-describedby="${documento}Help" placeholder="Anexe o ${fn:toLowerCase(documento)}" onblur="Validate(this.form)" required>
                        <small id="tituloHelp" class="form-text text-muted">Tipo de arquivo .PDF</small>
                        <div class="invalid-feedback">Envie o documento exigido em formato .PDF</div>
                        <br>
                    </c:forEach>
                        <a href="/Darwin/selecao/${selecao.codSelecao}" type="button" class="btn btn-secondary">
                            Cancelar
                        </a>
                        <input type="submit" value="Confirmar Inscrição" class="btn btn-primary" >
                    </form>
                </div>
            </div>
        </div>
    </div>

    <c:import url="elements/rodape.jsp" charEncoding="UTF-8"></c:import>  
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.js" ></script>
    <script type="text/javascript">
    // Example starter JavaScript for disabling form submissions if there are invalid fields
     $('#sandbox-container .input-daterange').datepicker({
        todayHighlight: true
        
     });
    (function() {
      'use strict';
      window.addEventListener('load', function() {
        var form = document.getElementById('needs-validation');
        form.addEventListener('submit', function(event) {
          if ((form.checkValidity() === false)) {
            event.preventDefault();
            event.stopPropagation();
          }
          form.classList.add('was-validated');
        }, false);
      }, false);
    })();
    (  function Validate(oForm) {
		var _validFileExtensions = [".pdf"];  
		var arrInputs = oForm.getElementsByTagName("input");
		for (var i = 0; i < arrInputs.length; i++) {
		    var oInput = arrInputs[i];
		    if (oInput.type == "file") {
		        var sFileName = oInput.value;
		        if (sFileName.length > 0) {
		            var blnValid = false;
		            for (var j = 0; j < _validFileExtensions.length; j++) {
		                var sCurExtension = _validFileExtensions[j];
		                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		                    blnValid = true;
		                    break;
		                }
		            }
		            
		            if (!blnValid) {
		                alert("Sorry, " + sFileName + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
		                return false;
		            }
		        }
		    }
		}

		return true;
})();

    </script>
</body>
</html>