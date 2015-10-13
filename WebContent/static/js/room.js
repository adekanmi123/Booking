var rid=request("rid");
var checkin=request("checkin");
var checkout=request("checkout");

var _room;
var amount;

$(document).ready(function() {
	$.messager.model = {
			ok:{ text: "确定", classed: 'btn-danger' },
			cancel: { text: "取消", classed: 'btn-default' }
		};

	$("#head").load("head.html");
	$("#foot").load("foot.html");   

	$("#booking-room-checkin, #booking-room-checkout").datetimepicker({
        format: 'yyyy-mm-dd',
        weekcheckin: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		checkinView: 2,
		minView: 2,
		forceParse: 0,
        showMeridian: 1,
        language: 'zh-CN'
    });
	
	//当日日期值发生变化时更改订单总价
	$("#booking-room-checkin, #booking-room-checkout").change(function() {
		checkin=$("#booking-room-checkin").val();
		checkout=$("#booking-room-checkout").val();
		if(checkin!=""&&checkin!=null&&checkout!=""&&checkout!=null) {
			if(getDaysBetweenDates(new Date((new Date().format(YEAR_MONTH_DATE_FORMAT))), new Date(checkin))<0) {
				$("#booking-room-checkin").parent().addClass("has-error");
				$("#booking-room-submit").attr("disabled","disabled");
				$.messager.popup("入住日期必须在今日或今日以后！");
				$("#booking-room-checkin").val("");
				return;
			}
			var days=getDaysBetweenDates(new Date(checkin), new Date(checkout));
			if(days<=0) {
				$("#booking-room-checkin, #booking-room-checkout").parent().addClass("has-error");
				$("#booking-room-submit").attr("disabled","disabled");
				$.messager.popup("退房日期必须在入住日期之后！");
			} else {
				$("#booking-room-checkin, #booking-room-checkout").parent().removeClass("has-error");
				$("#booking-room-submit").removeAttr("disabled");
				amount=days*_room.price;
				fillText({
					"booking-room-days": days,
					"booking-room-money": amount
				})
			}
		}
	});
	
	//加载房间信息
	RoomManager.getRoom(rid, function(room) {
		if(room==null||room.enable==false) {
			location.href="urlError.html";
			return;
		}

		_room=room;

		//如果传入入住时间和退房时间，就填充其表单并计算价格
		if(checkin!=""&&checkout!="") {
			if(getDaysBetweenDates(new Date((new Date().format(YEAR_MONTH_DATE_FORMAT))), new Date(checkin))<0) {
				$.messager.popup("入住日期必须在今日或今日以后！");
				fillValue({
			    	"booking-room-checkout": checkout
			    });
			} else {
				var days=getDaysBetweenDates(new Date(checkin), new Date(checkout));
			    if(days<=0) {
			    	$.messager.popup("退房日期必须在入住日期之后！");
			    } else {
			    	$("#booking-room-submit").removeAttr("disabled");
			    	fillValue({
				    	"booking-room-checkin": checkin,
				    	"booking-room-checkout": checkout
				    });
				   	amount=days*_room.price;
				    fillText({
						"booking-room-days": days,
						"booking-room-money": amount
					});
			    }
			}
		}

		//加载房间信息
		fillText({
			"room-rname": room.rname,
			"room-number": room.number,
			"room-area": room.area,
			"room-descriptor": room.descriptor,
			"room-price": room.price,
			"room-location": room.location
		});

		//加载房间图片
		PhotoManager.getPhotosByRid(rid, function(photos) {
			if(photos.length==0) {
				$("#room-photo-list").mengular(".room-photo-list-template", {
					src: "static/images/noImage.jpg"
				});
			}
			for(var i in photos) {
				$("#room-photo-list").mengular(".room-photo-list-template", {
					pid: photos[i].pid,
					src: "upload/"+rid+"/"+photos[i].filename,
					rname: room.rname
				});
			}
		});

		//加载房间评论
		CommentManager.getCommentsByRid(rid, function(comments) {
			$("#room-comment-list").mengularClear();
			if(comments.length>0)
				$("#no-room-comment").hide();
			for(var i in comments) {
				$("#room-comment-list").mengular(".room-comment-list-template", {
					cid: comments[i].cid,
					uname: comments[i].booking.user.uname,
					commentDate: comments[i].commentDate.format(DATE_HOUR_MINUTE_FORMAT_CN),
					content: comments[i].content
				});

				//加载评分星级
				$("#"+comments[i].cid+" .room-comment-stars i").each(function(index) {
					if(index+1<=comments[i].stars)
						$(this).addClass("fa-star");
					else
						$(this).addClass("fa-star-o");
				});
			}
		});

	});
	

	//预定房间
	$("#booking-room-submit").click(function() {
		checkSession(function(user) {
			if(user==null)
				$.messager.popup("请先登录后再订房！");
			else {
				var message="房间信息：<br>"+
					"房间名称："+_room.rname+"<br>"+
					"房间类型："+_room.number+"人房<br>"+
					"房间地址："+_room.location+"<br><br>"+
					"联系人信息：<br>"+
					"联系人姓名："+user.uname+"<br>"+
					"联系人电话："+user.telephone+"<br><br>"+
					"订单信息：<br>"+
					"订单金额：￥"+amount+"元<br>"+
					"入住时间："+checkin+"<br>"+
					"退房时间："+checkout;
				$.messager.confirm("您等订单信息如下，请确认订房信息：", message, function() { 
					BookingManager.reserve(checkin, checkout, rid, function(data) {
						if(data.success==false) {
							if(data.unavailableDates!=null) {
								var message="";
								for(var i in data.unavailableDates)
									message+="<h3 class='text-center text-danger'>"+data.unavailableDates[i].format("yyyy年MM月dd日")+"<h3>";
								$.messager.alert("该客房在以下日期已满房，请变更日期重试！", message);
							} else {
								$.messager.popup(data.reason);
							}
						} else {
							location.href="pay.html?bno="+data.bno;
						}
					});
				});	
			}
		});
	});
});