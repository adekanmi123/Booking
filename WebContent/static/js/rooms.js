$(document).ready(function() {

	//添加房间
	$("#add-room-submit").click(function() {
		var rname=$("#add-room-rname").val();
		var number=$("#add-room-number").val();
		var location=$("#add-room-location").val();
		var area=$("#add-room-area").val();
		var price=$("#add-room-price").val();
		var descriptor=$("#add-room-descriptor").val();
		var validate=true;
		if(rname==""||rname==null) {
			validate=false;
			$("#add-room-rname").parent().addClass("has-error");
		} else {
			$("#add-room-rname").parent().removeClass("has-error");
		}
		if(location==""||location==null) {
			validate=false;
			$("#add-room-location").parent().addClass("has-error");
		} else {
			$("#add-room-location").parent().removeClass("has-error");
		}
		if(area==""||area==null||!isNum(area)) {
			validate=false;
			$("#add-room-area").parent().addClass("has-error");
		} else {
			$("#add-room-area").parent().removeClass("has-error");
		}
		if(price==""||price==null||!isNum(price)) {
			validate=false;
			$("#add-room-price").parent().addClass("has-error");
		} else {
			$("#add-room-price").parent().removeClass("has-error");
		}
		if(descriptor==""||descriptor==null) {
			validate=false;
			$("#add-room-descriptor").parent().addClass("has-error");
		} else {
			$("#add-room-descriptor").parent().removeClass("has-error");
		}
		if(validate) {
			RoomManager.addRoom(rname, number, location, area, price, descriptor, function(rid){
				$("#add-room-modal").modal("hide");
				loadRooms();
			});
		}
	});
	
	//清除新增房间表单
	$("#add-room-modal").on("hide.bs.modal", function (e) {
		$("#add-room-form .input-group input").val("");
		$("#add-room-form .input-group textarea").val("");
	});

});

function loadRooms() {
	
}