var bno=request("bno");

$(document).ready(function() {
	
	$("#head").load("head.html");
	$("#foot").load("foot.html");
	
	fillText({
		"booking-bno": bno,
	});
	

	//加载微信支付code
	WeChatPayManager.createNative(bno, function(data) {
		if(data.codeUrl==null) {
			$.messager.alert("提示", "该订单已超时或已被支付，请求支付二维码失败！");
			return;
		}
		$("#qrcode").html("").qrcode(data.codeUrl);
		
		//加载成功后每隔5秒查询一次支付状态
		var checkState=setInterval(function() {
			BookingManager.checkPayState(bno, function(pay) {
				if(pay) {
					$.messager.popup("支付成功！");
					setTimeout(function() {
						location.href="pay.html?bno="+bno;
					}, 2000);
					clearInterval(checkState);
					return;
				} 
			});
		}, 5000);
	});
});