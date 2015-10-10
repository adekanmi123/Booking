$(document).ready(function() {
	$.messager.model = {
			ok:{ text: "确定", classed: 'btn-danger' },
			cancel: { text: "取消", classed: 'btn-default' }
		};

	$("#head").load("head.html");
	$("#foot").load("foot.html");

	//提交留言
	$("#message-submit").click(function() {
		var name=$("#message-name").val();
		var email=$("#message-email").val();
		var telephone=$("#message-telephone").val();
		var content=$("#message-content").val();
		var validate=true;
		if(name==null||name=="") {
			$("#message-name").parent().addClass("has-error");
			validate=false;
		} else {
			$("#message-name").parent().removeClass("has-error");
		}
		if(email==null||email==""||!isEmailAddress(email)) {
			$("#message-email").parent().addClass("has-error");
			validate=false;
		} else {
			$("#message-email").parent().removeClass("has-error");
		}
		if(telephone==null||telephone=="") {
			$("#message-telephone").parent().addClass("has-error");
			validate=false;
		} else {
			$("#message-telephone").parent().removeClass("has-error");
		}
		if(content==null||content=="") {
			$("#message-content").parent().addClass("has-error");
			validate=false;
		} else {
			$("#message-content").parent().removeClass("has-error");
		}
		if(!validate) {
			$.messager.popup("请正确填写所有留言项目！");
		} else {
			var message="成功留言后管理员会通过邮箱和电话与您联系，请您再次确认联系方式无误！<br>"+
				"您的姓名："+name+"<br>"+
				"您的邮箱："+email+"人房<br>"+
				"您的电话："+telephone;
			$.messager.confirm("确认留言信息", message, function() {
				MessageManager.addMessage(name, email, telephone, content, function(mid) {
					if(mid) {
						$("#message-name").val("");
						$("#message-email").val("");
						$("#message-telephone").val("");
						$("#message-content").val("");
						$.messager.popup("留言成功！");
					}
				});
			});
		}
	});
});