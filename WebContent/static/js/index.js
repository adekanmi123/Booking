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
	
	RoomManager.searchRoom(null, -1, null, -1, -1, -1, -1, getThisYearStart(), getThisYearEnd(), function(rooms) {
		$("#newest-room-list").mengularClear();
		for(var i in rooms) {
			$("#newest-room-list").mengular(".newest-room-template", {
				src: "upload/"+rooms[i].rid+"/"+rooms[i].cover.filename,
				rname: rooms[i].rname,
				location: rooms[i].location
			});
		}
	});
});