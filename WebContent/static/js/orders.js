var uid;
var orderby="createDate";
var desc=true;

$(document).ready(function() {

	$("#head").load("head.html");
	$("#foot").load("foot.html");  
	$("[data-toggle='tooltip']").tooltip();
	
	checkSession(function(user) {
		if(user==null) {
			$("#no-order").show();
			return;
		}
		uid=user.uid;
		loadBookings();
	});
	
	//选择排序字段
	$(".order-sort-orderby").click(function() {
		var id=$(this).attr("id");
		$(".order-sort-orderby").removeClass("active");
		$(this).addClass("active");
		switch (id) {
		case "order-sort-createdate":
			orderby="createDate";
			break;
		case "order-sort-checkin":
			orderby="checkin";
			break;
		case "order-sort-checkout":
			orderby="checkout";
			break;
		default:
			break;
		}
		loadBookings();
	});

	//选择排序顺序
	$(".order-sort-type").click(function() {
		var id=$(this).attr("id");
		$(".order-sort-type").removeClass("active");
		$(this).addClass("active");
		switch (id) {
		case "order-sort-asc":
			desc=false;
			break;
		case "order-sort-desc":
			desc=true;
			break;
		default:
			break;
		}
		loadBookings();
	});
});

/**
 * 加载订单
 */
function loadBookings() {
	BookingManager.getBookingsByUid(uid, orderby, desc, function(bookings) {
		$("#order-list").mengularClear();
		for(var i in bookings) {
			$("#order-list").mengular(".order-list-template", {
				bid: bookings[i].bid,
				src: "upload"+"/"+bookings[i].room.rid+"/"+bookings[i].room.cover.filename,
				bno: bookings[i].bno,
				rid: bookings[i].room.rid,
				rname: bookings[i].room.rname,
				number: bookings[i].room.number,
				location: bookings[i].room.location,
				createDate: bookings[i].createDate.format(DATE_HOUR_MINUTE_FORMAT_CN),
				checkin: bookings[i].checkin.format(YEAR_MONTH_DATE_FORMAT_CN),
				checkout: bookings[i].checkout.format(YEAR_MONTH_DATE_FORMAT_CN),
				days: bookings[i].days,
				amount: bookings[i].amount
			});
		}
	});
}