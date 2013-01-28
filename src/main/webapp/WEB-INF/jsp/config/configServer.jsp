<!-- 
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configurar Servidor</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
	rel="stylesheet">

</head>
<body>

	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<c:if test="${not empty errors}">
						<div class="alert">
							<button type="button" class="close" data-dismiss="alert">×</button>
							<c:forEach var="error" items="${errors}">
		   						 ${error.category} - ${error.message}<br />
							</c:forEach>
						</div>
					</c:if>
					<table class="table table-striped">
						<thead>
							<tr>
								<td>Parâmetro</td>
								<td>Valor</td>
								<td>Ação</td>
							</tr>
						</thead>
						<tbody>
							<tr class="info">
								<td>Diferença de Tempo (segundos)</td>
								<td>${difference}</td>
								<td><a
									href="javascript:setParameterForUpdate('${difference}','Editar Parametro Diferença de Tempo (segundos)','difference');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>
							<tr class="info">
								<td>Remetente do E-mail</td>
								<td>${mailFrom}</td>
								<td><a
									href="javascript:setParameterForUpdate('${mailFrom}','Editar Parametro Remetente do E-mail','mailFrom');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Assunto</td>
								<td>${subject}</td>
								<td><a
									href="javascript:setParameterForUpdate('${subject}','Editar Parametro Assunto','subject');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Destinatários</td>
								<td>${dests}</td>
								<td><a
									href="javascript:setParameterForUpdate('${dests}','Editar Parametro Destinatários','dests');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Jndi mail lookup</td>
								<td>${jndiMail}</td>
								<td><a
									href="javascript:setParameterForUpdate('${jndiMail}','Editar Parametro Jndi mail lookup','jndiMail');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>


	<form method="GET" action="<c:url value='/updateConfig'/>"
		onload="javascript:setParameterForUpdate()">
		<div class="modal" id="ConfigurationTab" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true"
			style="display: none">
			<div id="cab" class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel"></h3>
			</div>
			<div id="edit-modal" class="modal-body">
				<input type="hidden" id="campo" name="campo">
				<p align="center"></p>
				<div align="center">
					Novo valor: <input type="text" name="new_value">
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button type="submit" class="btn btn-primary" value="Atualizar">Atualizar</button>
			</div>
		</div>
	</form>

</body>
</html>