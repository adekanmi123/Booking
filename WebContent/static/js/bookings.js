$(document).ready(function() {
	checkAdminSession(function() {
		searchBookings(getThisMonthStart(), getThisMonthEnd(), null, null);
	});

	$("#search-booking-start, #search-booking-end, #search-booking-checkin").datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0,
        showMeridian: 1,
        language: 'zh-CN'
    });

    $("#search-booking-this-month").click(function() {
    	$("#search-booking-start").val(getThisMonthStart());
    	$("#search-booking-end").val(getThisMonthEnd());
    });

    //搜索订单
    $("#search-booking-submit").click(function() {
    	var start=$("#search-booking-start").val();
    	var end=$("#search-booking-end").val();
    	var checkin=$("#search-booking-checkin").val();
    	var bno=$("#search-booking-bno").val();
    	if(start==""&&end==""&&checkin==""&&bno=="")
    		$.messager.popup("至少填写一个搜索条件！");
    	else
    		searchBookings(start, end, checkin, bno);
    });

    //还原搜索
    $("#search-booking-reset").click(function() {
    	$("#search-booking-start").val("");
    	$("#search-booking-end").val("");
    	$("#search-booking-checkin").val("");
    	$("#search-booking-bno").val("");
    	searchBookings(getThisMonthStart(), getThisMonthEnd(), null, null);
    });
});

function searchBookings(start, end, checkin, bno) {
	BookingManager.searchBookingsForAdmin(start, end, checkin, bno, function(bookings) {
		$("#booking-list tbody").mengularClear();
		for(var i in bookings) {
			$("#booking-list tbody").mengular(".booking-list-template", {
				bid: bookings[i].bid,
				bno: bookings[i].bno,
				createDate: bookings[i].createDate.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
				checkin: bookings[i].checkin.format(YEAR_MONTH_DATE_FORMAT),
				rname: bookings[i].room.rname,
				uname: bookings[i].user.uname,
				telephone: bookings[i].user.telephone
			});
		}
	});
}