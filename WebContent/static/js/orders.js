var uid;
var orderby="createDate";
var desc=true;

var writingCommentBid;
var stars=5;

$(document).ready(function() {

	$("#head").load("head.html");
	$("#foot").load("foot.html");  
	$("[data-toggle='tooltip']").tooltip();
	
	checkSession(function(user) {
		if(user==null) {
			$("#no-order").show();
			return;
		}
		$("#order-sort").show();
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

	//选择星级
	$("#write-comment-stars i").each(function(index) {
		$(this).mouseover(function() {
			for(var i=0; i<5; i++) {
				if(i<=index) 
					$("#write-comment-stars i").eq(i).removeClass("fa-star-o").addClass("fa-star");
				else 
					$("#write-comment-stars i").eq(i).removeClass("fa-star").addClass("fa-star-o");
			}
		});
		$(this).mouseout(function() {
			for(var i=0; i<5; i++) {
				if(i<=stars-1) 
					$("#write-comment-stars i").eq(i).removeClass("fa-star-o").addClass("fa-star");
				else 
					$("#write-comment-stars i").eq(i).removeClass("fa-star").addClass("fa-star-o");
			}
		});
		$(this).click(function() {
			for(var i=0; i<5; i++) {
				if(i<=index) 
					$("#write-comment-stars i").eq(i).removeClass("fa-star-o").addClass("fa-star");
				else 
					$("#write-comment-stars i").eq(i).removeClass("fa-star").addClass("fa-star-o");
			}
			stars=index+1;
		});
	});

	//提交评论内容
	$("#write-comment-submit").click(function() {
		var content=$("#write-comment-content").val();
		if(content==null||content=="") 
			$.messager.popup("客官多少给点评价吧~~");
		else {
			CommentManager.writeComment(writingCommentBid, stars, content, function(data) {
				if(data.commented==true) {
					$.messager.popup("对不起，一个订单只能评论一次！");
					$("#write-comment-modal").modal("hide");
				} else {
					if(data.success==true) {
						$.messager.popup("评论成功！");
						$("#write-comment-modal").modal("hide");
					} else {
						$.messager.popup("评论失败，请重试！");
					}
				}
			});
		}
	});
	
	$("#write-comment-modal").on("hidden.bs.modal", function() {
		$("#write-comment-content").val("");
		stars=5;
		for(var i=0; i<5; i++) {
			if(i<=stars-1) 
				$("#write-comment-stars i").eq(i).removeClass("fa-star-o").addClass("fa-star");
			else 
				$("#write-comment-stars i").eq(i).removeClass("fa-star").addClass("fa-star-o");
		}
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

			//撰写评论
			$("#"+bookings[i].bid+" .order-list-write-comment").click(function() {
				writingCommentBid=$(this).parent().parent().parent().parent().attr("id");
				BookingManager.getBooking(writingCommentBid, function(booking) {
					fillText({
						"write-comment-bno": booking.bno,
						"write-comment-checkin": booking.checkin.format(YEAR_MONTH_DATE_FORMAT_CN),
						"write-comment-checkout": booking.checkout.format(YEAR_MONTH_DATE_FORMAT_CN),
						"write-comment-rname": booking.room.rname,
						"write-comment-days": booking.days
					});
					$("#write-comment-modal").modal("show");
				});
			});
		}
	});
}