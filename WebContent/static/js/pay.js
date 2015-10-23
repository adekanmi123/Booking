var bno=request("bno");

$(document).ready(function() {
	$("#head").load("head.html");
	$("#foot").load("foot.html");

	BookingManager.getBookingByBno(bno, function(booking) {
		if(booking==null) {
			location.href="urlError.html";
		} else {
			//检查订单支付状态
			PayManager.getPayByBno(bno, function(pay) {
				if(pay.payed==true) {
					$("#has-payed").show();
					$("#not-pay").remove();
					$("#pay-date").text(pay.payDate.format(DATE_HOUR_MINUTE_FORMAT_CN));
				} else {
					$("#has-payed").remove();
					$("#not-pay").show();
				}
					
				fillText({
					"pay-rname": booking.room.rname,
					"pay-bno": booking.bno,
					"pay-rname-b": booking.room.rname,
					"pay-location": booking.room.location,
					"pay-number": booking.room.number,
					"pay-createDate": booking.createDate.format(DATE_HOUR_MINUTE_FORMAT_CN),
					"pay-checkin": booking.checkin.format(YEAR_MONTH_DATE_FORMAT_CN),
					"pay-checkout": booking.checkout.format(YEAR_MONTH_DATE_FORMAT_CN),
					"pay-days": booking.days,
					"pay-amount": booking.amount,
					"pay-insurances": booking.insurances
				});
				$("#alipay-submit").attr("href","AlipayServlet?task=pay&bno="+bno);
			})
			
		}
	});
	
	
});