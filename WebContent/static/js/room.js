var rid=request("rid");
var price;

$(document).ready(function() {
	$("#head").load("head.html");
	$("#foot").load("foot.html");   

	$("#booking-room-start, #booking-room-end").datetimepicker({
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
	
	//当日日期值发生变化时更改订单总价
	$("#booking-room-start, #booking-room-end").change(function() {
		var start=$("#booking-room-start").val();
		var end=$("#booking-room-end").val();
		if(start!=""&&start!=null&&end!=""&&end!=null) {
			var days=getDaysBetweenDates(new Date(start), new Date(end));
			if(days<=0) {
				$("#booking-room-start, #booking-room-end").parent().addClass("has-error");
				$("#booking-room-submit").attr("disabled","disabled");
				$.messager.popup("退房日期必须在入住日期之后！");
			} else {
				$("#booking-room-start, #booking-room-end").parent().removeClass("has-error");
				$("#booking-room-submit").removeAttr("disabled");
				fillText({
					"booking-room-days": days,
					"booking-room-money": days*price
				})
			}
		}
	});
	
	RoomManager.getRoom(rid, function(room) {
		if(room==null) {
			location.href="urlError.html";
			return;
		}

		price=room.price;

		fillText({
			"room-rname": room.rname,
			"room-number": room.number,
			"room-area": room.area,
			"room-descriptor": room.descriptor,
			"room-price": room.price,
			"room-location": room.location
		});

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
	});
});