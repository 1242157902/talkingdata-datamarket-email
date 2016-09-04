<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
body {
	font: normal 12px auto "Trebuchet MS", Verdana, Arial, Helvetica,
		sans-serif;
	color: #4f6b72;
	background: #E6EAE9;
}

a {
	color: #c75f3e;
}

#mytable {
	padding: 0;
	margin: 0;
}

caption {
	padding: 0 0 5px 0;
	width: 850px;
	font: italic 12px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
	text-align: right;
}


th.nobg {
	border-top: 0;
	border-left: 0;
	border-right: 1px solid #C1DAD7;
	background: none;
}

th {
    font: bold 12px, Verdana, Arial, Helvetica, sans-serif;
    color: #4f6b72;
    border-right: 1px solid #C1DAD7;
    border-bottom: 1px solid #C1DAD7;
    border-top: 1px solid #C1DAD7;
    letter-spacing: 2px;
    text-align: left;
    padding: 6px 6px 6px 12px;
    background: #CAE8EA;
}

th.nobg {
    border-top: 0;
    border-left: 0;
    border-right: 1px solid #C1DAD7;
    background: none;
}

td {
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	background: #fff;
	font-size: 12px;
	padding: 6px 6px 6px 12px;
	color: #4f6b72;
}

td.alt {
	background: #F5FAFA;
	color: #797268;
}

th.spec {
    border-left: 1px solid #C1DAD7;
    border-top: 0;
    background: #fff;
    font: bold 12px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
}

th.specalt {
    border-left: 1px solid #C1DAD7;
    border-top: 0;
    background: #f5fafa;
    font: bold 12px, Verdana, Arial, Helvetica, sans-serif;
    color: #797268;
}

td {
	font-size: 12px;
}
</style>
</head>
<body>


 <table align="center">
	<tr>
		<td height="30" colspan="2" align="center" style="font-size: 14px;font-weight: bold">DataMarket 每天业务数据${start}~${end}</td>
    </tr>
	<tr>
		<table width=100%>

			<tr>
				<th width=30%>服务Id</th>
                <th width=14%>请求次数</th>
				<th width=16%>成功请求次数</th>
				<th width=20%>输入记录数</th>
				<th width=20%>输出记录数</th>
			</tr>

				<#if weekReportList??>
					<#list weekReportList?sort_by("successRequest") as weekReport>
						<tr>
							<td>${weekReport.serviceId}</td>
							<td>${weekReport.accessRequest}</td>
							<td> ${weekReport.successRequest}</td>
							<td> ${weekReport.inputCount}</td>
							<td>${weekReport.oututCount}</td>
						</tr>
					</#list>
				</#if>

		</table>
		</td>
	</tr>

		<tr>
			<td width=100%>
				暂时为空！
			</td>
		</tr>

</table>
</body>
</html>
