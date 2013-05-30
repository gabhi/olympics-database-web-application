<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
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

		<title>My JSP 'index.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="js/AjaxRequest.js"></script>
		<script type="text/javascript">
	window.onload = function() {
		alert('111');
		getID();
		initialization();
	};

	window.setInterval("updateMoney();", 1000);

	function getID() {
		var loader = new net.AjaxRequest("servlet/mono?action=getID&nocache="
				+ new Date().getTime(), deal_id, onerror, "GET");
	}

	function updateMoney() {
		var loader = new net.AjaxRequest(
				"servlet/mono?action=getMessages&nocache="
						+ new Date().getTime(), deal_roll, onerror, "GET");
	}

	function gets() {
		var dice = Math.floor((Math.random() * 6) + 1);
		alert("You rolled " + dice + "!");
		var loader = new net.AjaxRequest("servlet/mono?action=addMoney&roll="
				+ dice + "&nocache=" + new Date().getTime(), deal_roll,
				onerror, "GET");
	}

	function deal_roll() {
		Money.innerText = this.req.responseText;
	}

	function deal_id() {
		player.innerText = this.req.responseText;
	}
</script>
	</head>


	<body>
		This is my JSP page.
		<br>
		<div id="player"></div>
		<div id="Money">
			100
		</div>
		<button id="roll" onclick="gets()">
			Roll!
		</button>
	</body>
</html>
