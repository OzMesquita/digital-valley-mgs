<%-- 
    Document   : menu-lateral
    Created on : 05/10/2017, 11:50:30
    Author     : Alex Felipe
--%>
<div class="col-12 col-md-3 col-xl-2 bd-sidebar" style="margin-top: 5px;">
    <div class="btn-group-vertical d-flex flex-column border" role="group">
    <strong><p class="btn btn-sm" align="center">Categorias</p></strong>
        <a href="${pageContext.request.contextPath}/categoria/Assist�ncia_Estudantil" class="btn btn-light btn-sm text-left ${categoria == 'Assist�ncia_Estudantil' ? 'active': ''}">Assist�ncia Estudantil</a>
        <a href="${pageContext.request.contextPath}/categoria/Sele��o_para_Discentes" class="btn btn-light btn-sm text-left ${categoria == 'Sele��o_Para_Discentes' ? 'active': ''}">Sele��es para Discentes</a>  
        <a href="${pageContext.request.contextPath}/categoria/Cargos_de_Docente" class="btn btn-light btn-sm text-left ${categoria == 'Cargos_de_Docente' ? 'active': ''}">Cargos de Docente</a>
        <a href="${pageContext.request.contextPath}/categoria/Cargos_de_T�cnicos_Admin" class="btn btn-light btn-sm text-left ${categoria == 'Cargos_de_T�cnicos_Admin' ? 'active': ''}">Cargos de T�cnicos Admin.</a>
        <a href="${pageContext.request.contextPath}/categoria/Professores_Substitutos" class="btn btn-light btn-sm text-left ${categoria == 'Professores_Substitutos' ? 'active': ''}">Professores Substitutos</a>      
    </div>
    <br>
</div>