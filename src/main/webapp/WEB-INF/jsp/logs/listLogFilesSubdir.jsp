<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Configuração Clientes</title>
	<script src="http://code.jquery.com/jquery-latest.js" > </script>
	<link href="${pageContext.request.contextPath}/css/bootstrap.css"
		rel="stylesheet">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link
		href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
		rel="stylesheet">
	<script>
		function callTail(fileName, divName){
			nm = fileName.trim().split(" ");
			dv = "#"+divName;
			valor = $(dv).val();
			if(fileName === null || fileName === '' ||
					valor === null || valor === ''){
				alert("Número de linhas é obrigatório.")
				return; 
			}
			$.get('${pageContext.request.contextPath}/tailFile/${hostname}/'+valor+'/'+nm[1], "", function(retorno) {    
				 var tailContent = $("#tailContent", retorno);
				 $('#pageContent').empty().append(tailContent);
			})
			.fail(function() { alert("Erro ao executar comando."); });
		}

		function findInFile(fileName, divName){
			nm = fileName.trim().split(" ");
			dv = "#"+divName;
			valor = $(dv).val();
			if(fileName == null || fileName === '' ||
					valor == null || valor === ''){
				alert("Palavra da busca é obrigatória.")
				return; 
			}
			$.get('${pageContext.request.contextPath}/findInFile/${hostname}/'+valor+'/'+nm[1], "", function(retorno) {    
				 var tailContent = $("#findContent", retorno);
				 $('#pageContent').empty().append(tailContent);
			})
			.fail(function() { alert("Erro ao executar comando."); });
		}

		function numbersonly(myfield, e) {
			var key;
			var keychar;
			if (window.event)
				key = window.event.keyCode;
			else if (e)
				key = e.which;
			else
				return true;
			keychar = String.fromCharCode(key);
			// control keys
			if ((key == null) || (key == 0) || (key == 8) || (key == 9)
					|| (key == 13) || (key == 27))
				return true;
			// numbers
			else if ((("0123456789").indexOf(keychar) > -1))
				return true;

			return false;
		}
			
		
	</script>

</head>
<body>

	<div class="container">
		<div class="content" id="pageContent">
			<div class="row">
				<div class="span12">
					<table class="table table-striped" id="resultTable">
						<h3>Index of ${logDir}</h3>
						<HR WIDTH="50%" />
						<thead>
							<tr>
								<th>Arquivos encontrados: ${qtdn }</th>
								<th>Visualizar últimas N linhas</th>
								<th>Buscar no arquivo</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach begin="0" var="file" varStatus="status" items="${listOfFiles}">
								<tr>
								<c:choose>
									<c:when test="${(fn:startsWith(file, 'd ')) == false}">
										<td><a href="<c:url value='/downloadFileSubdir/${hostname}/${file}${logDir}' />" value="${file}">${file}</a></td>
									</c:when>
									<c:otherwise>
										<td><a href="<c:url value='/listLogFiles/${hostname}/subdir/${logDir}/${file}' />" value="c/${file}">${file}</a></td>
									</c:otherwise>
								</c:choose>									
								<td>
								<c:choose>
									<c:when test="${(fn:startsWith(file, 'd ')) == false}">
										<div style="display: inline"> 
											<input type="text" name="numLinhas" placeholder="Quantidade de linhas" style="margin-bottom: 0px;" id="file${status.index}" onkeypress="return numbersonly(this,event)"/>
											<input type="image" src="${pageContext.request.contextPath}/img/tail.png" name="image" width="14" height="14" onclick="callTail('${file}${logDir}', 'file${status.index}')"/>
										</div>
									</c:when>
									<c:otherwise>
										<div style="display: inline"> 
											Diretório
										</div>
									</c:otherwise>
									</c:choose>
									</td>
									<td>
									<c:choose>
									<c:when test="${(fn:startsWith(file, 'd ')) == false}">
										<div style="display: inline">
											<input type="text" name="palavraBusca" placeholder="Valor Desejado" style="margin-bottom: 0px;" id="findfile${status.index}"/>
											<input type="image" src="${pageContext.request.contextPath}/img/search.png" name="image" width="14" height="14" onclick="findInFile('${file}${logDir}', 'findfile${status.index}')"/>
										</div>
									</c:when>
									<c:otherwise>
										<div style="display: inline"> 
											---
										</div>
									</c:otherwise>
								</c:choose>
								</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>