var pageSize=15;

$(document).ready(function() {
	$.messager.model = {
		ok:{ 
			text: "确定", 
			classed: "btn-danger" 
		},
		cancel: { 
			text: "取消", 
			classed: "btn-default" 
		}
	};

	checkAdminSession(function() {
		searchBookings("", "", null, null, -1, 1);
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
	
	//加载每页显示条数的下拉菜单
	for(var i=0;i<=100;i++) {
		var option=$("<option>").val(i).text(i);
		if(pageSize==i)
			option.attr("selected","selected");
		$("#page-size").append(option);
	}
	//更改每页显示的条数
	$("#page-size").change(function() {
		pageSize=$(this).val();
		var start=$("#search-booking-start").val();
    	var end=$("#search-booking-end").val();
    	var checkin=$("#search-booking-checkin").val();
    	var bno=$("#search-booking-bno").val();
    	var type=$("#search-booking-type").val();
    	searchBookings(start, end, checkin, bno, type, 1);
	});

    //搜索订单
    $("#search-booking-submit").click(function() {
    	var start=$("#search-booking-start").val();
    	var end=$("#search-booking-end").val();
    	var checkin=$("#search-booking-checkin").val();
    	var bno=$("#search-booking-bno").val();
    	var type=$("#search-booking-type").val();
    	searchBookings(start, end, checkin, bno, type, 1);
    });

    //还原搜索
    $("#search-booking-reset").click(function() {
    	$("#search-bookings-form input").val("");
    	$("#search-booking-type").val(-1);
    	searchBookings("", "", null, null, -1, 1);
    });
    
    //显示今日入住的用户
    $("#today-stay").click(function() {
    	var today=(new Date()).format(YEAR_MONTH_DATE_FORMAT);
    	$("#search-booking-start").val("");
    	$("#search-booking-end").val("");
    	$("#search-booking-checkin").val(today);
    	$("#search-booking-bno").val("");
    	$("#search-booking-type").val(2);
    	searchBookings("", "", today, null, 2, 1);
	});
});

/**
 * 搜索订单
 * @param start
 * @param end
 * @param checkin
 * @param bno
 * @param type
 */
function searchBookings(start, end, checkin, bno, type, page) {
	//加载页码
	BookingManager.getBookingsCountForAdmin(start, end, checkin, bno, type, function(count) {
		$("#page-count").text(count);
		$("#page-nav ul").empty();
		for(var i=1; i<Math.ceil(count/pageSize+1);i++) {
			var li='<li><a href="javascript:void(0)">'+i+'</a></li>';
			if(page==i)
				li='<li class="active"><a href="javascript:void(0)">'+i+'</a></li>';
			$("#page-nav ul").append(li);
		}
		$("#page-nav ul li").each(function(index) {
			$(this).click(function() {
				searchBookings(start, end, checkin, bno, type, index+1); 
			});
		});
	});
	
	//加载订单
	BookingManager.searchBookingsForAdmin(start, end, checkin, bno, type, page, pageSize, function(bookings) {
		$("#booking-list tbody").mengularClear();
		for(var i in bookings) {
            var payState;
          //1、订单支付超时
			if(bookings[i].timeout) {
				payState='<span class="text-danger">支付超时</span>';
			} else {
			//2、订单已支付
				if(bookings[i].pay) {
					payState='<span class="text-success">已支付</span>';
				} else {
			//3、订单未支付，等待用户支付
					payState='<span class="text-warning">待支付</span>';
				}				
			}

			$("#booking-list tbody").mengular(".booking-list-template", {
				bid: bookings[i].bid,
				bno: bookings[i].bno,
				createDate: bookings[i].createDate.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
				checkin: bookings[i].checkin.format(YEAR_MONTH_DATE_FORMAT),
				rname: bookings[i].room.rname,
				uname: bookings[i].user.uname,
				telephone: bookings[i].user.telephone,
				pay: payState
			});

			//订单详情
			$("#"+bookings[i].bid+" .booking-list-show").click(function() {
				var bid=$(this).parent().attr("id");
				BookingManager.getBooking(bid, function(booking) {
					if(booking.stayed)
						booking.stayed="已入住";
					else
						booking.stayed="未入住";
					fillText({
						"show-booking-bno": booking.bno,
						"show-booking-createDate": booking.createDate.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
						"show-booking-rname": booking.room.rname,
						"show-booking-uname": booking.user.uname,
						"show-booking-telephone": booking.user.telephone,
						"show-booking-checkin": booking.checkin.format(YEAR_MONTH_DATE_FORMAT),
						"show-booking-checkout": booking.checkout.format(YEAR_MONTH_DATE_FORMAT),
						"show-booking-days": booking.days,
						"show-booking-price": booking.room.price,
						"show-booking-amount": booking.amount,
						"show-booking-stayed": booking.stayed
					});
				})
				$("#show-booking-modal").modal("show");
			});

			//删除订单
			$("#"+bookings[i].bid+" .booking-list-delete").click(function() {
				var bid=$(this).parent().attr("id");
				var bno=$("#"+bid+" .booking-list-bno").text();
				$.messager.confirm("提示", "确认删除订单："+bno+"吗？", function() {
					BookingManager.deleteBooking(bid, function(success) {
						if(success)
							$("#"+bid).remove();
						else
							$.messager.popup("该订单已支付，无法删除！");
					});
				});
			});

		}
	});
}