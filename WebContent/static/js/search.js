var _checkin=request("checkin");
var _checkout=request("checkout");
var _number=request("number");
var _location=decodeURIComponent(request("location"));

$(document).ready(function() {

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

    //加载房间信息
    if(_checkin==""||_checkout=="") {
    	RoomManager.getNewestRooms(18, function(rooms) {
    		loadRooms(rooms);
    	});
    } else {
    	fillValue({
    		"search-room-checkin": _checkin,
    		"search-room-checkout": _checkout,
    		"search-room-number": _number,
    		"search-room-location": _location,
    	});
    	RoomManager.searchRoomForUser(_checkin, _checkout, _number, _location, null, -1, -1, function(rooms) {
			loadRooms(rooms);
		});
    }

    $("#search-room-submit").click(function() {
		_checkin=$("#search-room-checkin").val();
		_checkout=$("#search-room-checkout").val();
		_number=$("#search-room-number").val();
		_location=$("#search-room-location").val();
		if(_checkin!=""&&_checkin!=null&&_checkout!=""&&_checkout!=null) {
			var days=getDaysBetweenDates(new Date(_checkin), new Date(_checkout));
			if(days<=0) {
				$("#search-room-checkin, #search-room-checkout").parent().addClass("has-error");
				$.messager.popup("退房日期必须在入住日期之后！");
			} else {
				$("#search-room-checkin, #search-room-checkout").parent().removeClass("has-error");
				RoomManager.searchRoomForUser(_checkin, _checkout, _number, _location, null, -1, -1, function(rooms) {
					loadRooms(rooms);
				});
			}
		} else {
			$.messager.popup("请选定一个入住时间和退房时间！");
		}
    });
});

/**
 * 传入房间的json数组，加载房间信息
 * @param rooms
 */
function loadRooms(rooms) {
	if(rooms.length==0) 
		$("#no-search-result").show();
	else 
		$("#no-search-result").hide();
	$("#room-list").mengularClear();
	for(var i in rooms) {
		var src="static/images/noImage.jpg";
		if(rooms[i].cover!=null)
			src="upload/"+rooms[i].rid+"/"+rooms[i].cover.filename;
		$("#room-list").mengular(".room-template", {
			rid: rooms[i].rid,
			src: src,
			rname: rooms[i].rname,
			location: rooms[i].location,
			price: rooms[i].price,
			number: rooms[i].number,
			sold: rooms[i].sold
		});
	}
}