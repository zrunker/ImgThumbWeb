<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>压缩图片测试</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<body>
	<center>
		<a href="javascript:;" onclick="startCompress()">开始压缩</a>
	</center>
</body>
<script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
<%-- function startCompress() {
	alert("开始");
	$.get("<%=basePath%>image/quality/compress", 
		{
			imagePath : "F:\\efd.png",
			quality : 0.4,
			outputPath : "F:\\bcdde"
		}, function (result) {
			console.log(result);
		});
} --%>
function startCompress() {
	alert("开始");
	$.get("<%=basePath%>image/quality/compress/tojpg", 
		{
			imagePath : "F:\\efd.png",
			quality : 0.4,
			outputPath : "F:\\bcdde"
		}, function (result) {
			console.log(result);
		});
};
</script>
</html>
