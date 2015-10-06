var verificationGetting=false;

$(document).ready(function() {
	$("span.menu").click(function(){
		$(" ul.navig").slideToggle("slow" , function(){});
	});

	$("#user-register-modal").on("hidden.bs.modal", function() {
		$("#user-register-modal .input-group input").val("");
	});

	$("#user-login-submit").on("hidden.bs.modal", function() {
		$("#user-login-modal .input-group input").val("");
	});

	//获取安全码
	$("#user-register-security-code").focus(function() {
		$("#user-register-security-code-img").attr("src","ValidateCodeServlet?"+Math.random());
		$("#user-register-security-code-img").show();
	});
	
	//获取短信验证码
	$("#get-verification-code").click(function(){
		if(!verificationGetting) {
			//发送短信验证码
			var telephone=$("#user-register-telephone").val();
			var code=$("#user-register-security-code").val();
			if(isNum(telephone)&&code!="") {
				//设置短信验证码按钮
				verificationGetting=true;
				$("#get-verification-code").text("正在发送短信");
				UserManager.getVerificationCode(telephone, code, function(data){
					if(data.hasTelephone==true) {
						$.messager.popup("该手机号码已经被注册，请更换其他手机号码！");
						$("#get-verification-code").text("获取验证码");
						verificationGetting=false;
					} else {
						if(data.send==true) {
							$("#get-verification-code").text("短信发送成功");
							var time=data.verificationTimeout;
							var getVerificationCode=setInterval(function() {
								$("#get-verification-code").text(time+"秒后重新获取");
								if(time==0) {
									clearInterval(getVerificationCode);
									$("#get-verification-code").text("获取验证码");
									verificationGetting=false;
								}
								time--;	
							}, 1000);
						} else {
							$.messager.popup("短信发送失败，错误原因："+data.reason+"。请检查手机号码和安全码是否正确后重试！");
							$("#get-verification-code").text("获取验证码");
							verificationGetting=false;
						}	
					}
				});
			} else 
				$.messager.popup("请填写正确的手机号码，并输入安全码！");
		}
	});

	//提交注册信息
	$("#user-register-submit").click(function() {
		var telephone=$("#user-register-telephone").val();
		var uname=$("#user-register-uname").val();
		var password=$("#user-register-password").val();
		console.log(password);
		var repeatPassword=$("#user-register-repeat-password").val();
		var verificationCode=$("#user-register-verification-code").val();
		var validate=true;
		if(!isNum(telephone)) {
			$("#user-register-telephone").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-register-telephone").parent().removeClass("has-error");
		}
		if(uname==""||uname==null) {
			$("#user-register-uname").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-register-uname").parent().removeClass("has-error");
		}
		if(password==""||password==null) {
			$("#user-register-password").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-register-password").parent().removeClass("has-error");
		}
		if(repeatPassword==""||repeatPassword==null) {
			$("#user-register-repeat-password").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-register-repeat-password").parent().removeClass("has-error");
			if(password!=repeatPassword) {
				$("#user-register-password").parent().addClass("has-error");
				$("#user-register-repeat-password").parent().addClass("has-error");
				$.messager.popup("两次密码不一致！");
				validate=false;
			} else {
				$("#user-register-password").parent().removeClass("has-error");
				$("#user-register-repeat-password").parent().removeClass("has-error");
			}
		}
		if(verificationCode==""||verificationCode==null) {
			$("#user-register-verification-code").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-register-verification-code").parent().removeClass("has-error");
		}
		if(validate) {
			UserManager.register(telephone, uname, password, verificationCode, function(data) {
				if(data.success==false) 
					$.messager.popup(data.reason);
				else {
					$("#user-register-modal").modal("hide");
					$.messager.popup("注册成功，请登录！");
				}
			})
		}
	});

	//用户登录
	$("#user-login-submit").click(function() {
		var telephone=$("#user-login-telephone").val();
		var password=$("#user-login-password").val();
		var validate=true;
		if(telephone==""||telephone==null) {
			$("#user-login-telephone").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-login-telephone").parent().removeClass("has-error");
		}
		if(password==""||password==null) {
			$("#user-login-password").parent().addClass("has-error");
			validate=false;
		} else {
			$("#user-login-password").parent().removeClass("has-error");
		}
		if(validate) {
			UserManager.login(telephone, password, function(success) {
				if(success) {
					$("#user-login-modal").modal("hide");
					$.messager.popup("登录成功！");
					checkSession(function(user) {
						$("#nav-login, #nav-register").css("display","none");
						$("#nav-user a").text(user.uname);
						$("#nav-user, #nav-logout").css("display","inline");
					})
				} else {
					$("#user-login-telephone").parent().addClass("has-error");
					$("#user-login-password").parent().addClass("has-error");
				}
			});
		}
	});

});