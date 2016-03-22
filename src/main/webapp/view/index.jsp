<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/layer/layer/layer.js"></script>
<title>index</title>
<!-- Javascript goes in the document HEAD -->
<script type="text/javascript">
	var list = new Array();
	var data = new Array();
	var index = 0;
	$(function(){
		altRows('alternatecolor');
		$("#ok").click(function(){
			if($("#tablecol option").length == 0) {
				return;
			}
			var col = $("#tablecol").val();
			$("#titleid"+col).html($("#tabletitle option:selected").text());
			var obj = new Object();
			obj.tablecol = $("#tablecol option:selected");
			obj.tabletitle = $("#tabletitle option:selected");
			list[index] = obj;
			var dataobj = new Object();
			dataobj.col = obj.tablecol.text();
			dataobj.title = obj.tabletitle.text();
			data[index] = dataobj;
			$("#tablecol option:selected").remove();
			$("#tabletitle option:selected").remove();
			index++;
		});
		
		$("#back").click(function(){
			if(index == 0) {
				return;
			}
			index--;
			var obj = list[index];
			//修改表头名称为原来的A、B之类
			$("#titleid"+obj.tablecol.val()).html(obj.tablecol.text());
			//将删掉的option添加到对应的位置
			if($("#tablecol option").length > 0) {
				$("#tablecol option").each(function() {
					if($(this).val()>obj.tablecol.val()) {
						$("#tablecol option[value='"+$(this).val()+"']").before(obj.tablecol);
						return false;
					}
					if($(this).val()==obj.tablecol.val()-1) {
						$("#tablecol option[value='"+$(this).val()+"']").after(obj.tablecol);
						return false;
					}
				});
			} else {
				$("#tablecol").append(obj.tablecol);
			}
			
			if($("#tabletitle option").length > 0) {
				$("#tabletitle option").each(function() {
					if($(this).val()>obj.tabletitle.val()) {
						$("#tabletitle option[value='"+$(this).val()+"']").before(obj.tabletitle);
						return false;
					}
					if($(this).val()==obj.tabletitle.val()-1) {
						$("#tabletitle option[value='"+$(this).val()+"']").after(obj.tabletitle);
						return false;
					}
				});
			} else {
				$("#tabletitle").append(obj.tabletitle);
			}
			
		});
		
		$("#sub").click(function(){
			if($("#tabletitle option").length > 0) {
				layer.msg('请选择');
				return;
			}
			$("[name=data]").val(JSON.stringify(data));
			document.subForm.submit();
		});
		
	});
	function altRows(id) {
		if (document.getElementsByTagName) {

			var table = document.getElementById(id);
			var rows = table.getElementsByTagName("tr");

			for (i = 0; i < rows.length; i++) {
				if (i % 2 == 0) {
					rows[i].className = "evenrowcolor";
				} else {
					rows[i].className = "oddrowcolor";
				}
			}
		}
	}
</script>


<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<style type="text/css">
table.altrowstable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}

table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

.oddrowcolor {
	background-color: #d4e3e5;
}

.evenrowcolor {
	background-color: #c3dde0;
}
</style>
</head>
<body>
	<center>
		<select id="tablecol">
			<c:forEach items="${col }" var="t" varStatus="s">
				<option value="${s.index }">${t }</option>
			</c:forEach>
		</select>
		<select id="tabletitle">
			<c:forEach items="${title }" var="t" varStatus="s">
				<option value="${s.index }">${t }</option>
			</c:forEach>
		</select>
		<input type="button" id="ok" value="确定"/>
		<input type="button" id="back" value="回退"/>
		<input type="button" id="sub" value="提交"/>
		<table id="alternatecolor" width="50%">
			<tbody>
				<tr>
				<c:forEach items="${col }" var="t" varStatus="s">
					<th id="titleid${s.index }">${t }</th>
				</c:forEach>
				</tr>
				<c:forEach items="${content }" var="rows">
					<tr>
						<c:forEach items="${rows }" var="col">
							<td width="10%">${col }</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<form action="sub" method="post" name="subForm">
			<input name="data" type="hidden" />
		</form>
	</center>
</body>
</html>