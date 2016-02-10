$(document).ready(function($) {
	$("#head").load("head.html");
	$("#foot").load("foot.html");
	
	$("#search-room-checkin, #search-room-checkout").datetimepicker({
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
	
	//入住日期变更时判断其是否在今日之后
	$("#search-room-checkin").change(function() {
		var checkin=$(this).val();
    	if(getDaysBetweenDates(new Date((new Date().format(YEAR_MONTH_DATE_FORMAT))), new Date(checkin))<0) {
    		$.messager.popup("入住日期必须在今日或今日以后！");
			$(this).val("");
    	}
	});

    //搜索房间
    $("#search-room-submit").click(function() {
    	var checkin=$("#search-room-checkin").val();
    	var checkout=$("#search-room-checkout").val();
    	var number=$("#search-room-number").val();
    	var _location=$("#search-room-location").val();
		if(checkin!=""&&checkin!=null&&checkout!=""&&checkout!=null) {
			var days=getDaysBetweenDates(new Date(checkin), new Date(checkout));
			if(days<=0) {
				$("#search-room-checkin, #search-room-checkout").parent().addClass("has-error");
				$.messager.popup("退房日期必须在入住日期之后！");
			} else {
				$("#search-room-checkin, #search-room-checkout").parent().removeClass("has-error");
				location.href="search.html?checkin="+checkin+"&checkout="+checkout+"&number="+number+"&location="+encodeURIComponent(_location);
			}
		} else {
			$.messager.popup("请选定一个入住时间和退房时间！");
		}
    });
	
	//加载最新的12个房间
	RoomManager.getNewestRooms(4, function(rooms) {
		$("#newest-room-list").mengularClear();
		for(var i in rooms) {
			var src="static/images/noImage.jpg";
			if(rooms[i].cover!=null)
				src="upload/"+rooms[i].rid+"/"+rooms[i].cover.filename;
			$("#newest-room-list").mengular(".newest-room-template", {
				rid: rooms[i].rid,
				src: src,
				rname: rooms[i].rname,
				location: rooms[i].location,
				price: rooms[i].price,
				number: rooms[i].number,
				sold: rooms[i].sold
			});
		}
	});

});