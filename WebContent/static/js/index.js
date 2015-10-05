$(document).ready(function($) {
	$(".scroll").click(function(event){		
		event.preventDefault();
		$("html, body").animate({
			scrollTop:$(this.hash).offset().top
		},1000);

		$().UItoTop({ 
			easingType:"easeOutQuart"
		});
	});
	
	//script-for-menu
	$("span.menu").click(function(){
		$(" ul.navig").slideToggle("slow" , function(){});
	});
	
	$("#search-room-start, #search-room-end").datetimepicker({
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
	
	RoomManager.searchRoom(null, -1, null, -1, -1, -1, -1, getThisYearStart(), getThisYearEnd(), function(rooms) {
		$("#newest-room-list").mengularClear();
		for(var i in rooms) {
			var src="static/images/noImage.jpg";
			if(rooms[i].cover!=null)
				src="upload/"+rooms[i].rid+"/"+rooms[i].cover.filename;
			$("#newest-room-list").mengular(".newest-room-template", {
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