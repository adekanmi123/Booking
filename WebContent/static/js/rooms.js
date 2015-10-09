//正在修改的房间id
var modifyingRid;

$(document).ready(function() {
	$.messager.model = {
			ok:{ text: "确定", classed: 'btn-danger' },
			cancel: { text: "取消", classed: 'btn-default' }
		};
	
	checkAdminSession(function() {
		loadRooms();
	})
	
	//添加房间
	$("#add-room-submit").click(function() {
		var rname=$("#add-room-rname").val();
		var number=$("#add-room-number").val();
		var location=$("#add-room-location").val();
		var area=$("#add-room-area").val();
		var price=$("#add-room-price").val();
		var available=$("#add-room-available").val();
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
		if(available==""||available==null||!isInteger(available)) {
			validate=false;
			$("#add-room-available").parent().addClass("has-error");
		} else {
			$("#add-room-available").parent().removeClass("has-error");
		}
		if(descriptor==""||descriptor==null) {
			validate=false;
			$("#add-room-descriptor").parent().addClass("has-error");
		} else {
			$("#add-room-descriptor").parent().removeClass("has-error");
		}
		if(validate) {
			RoomManager.addRoom(rname, number, location, area, price, available, descriptor, function(rid){
				$("#add-room-modal").modal("hide");
				loadRooms();
			});
		}
	});
	
	//清除新增房间表单
	$("#add-room-modal").on("hide.bs.modal", function (e) {
		$("#add-room-form .input-group input").val("");
		$("#add-room-form .input-group textarea").val("");
		$("#add-room-form .input-group").removeClass("has-error");
	});

	//修改房间
	$("#modify-room-submit").click(function() {
		var rname=$("#modify-room-rname").val();
		var number=$("#modify-room-number").val();
		var location=$("#modify-room-location").val();
		var area=$("#modify-room-area").val();
		var price=$("#modify-room-price").val();
		var available=$("#modify-room-available").val();
		var descriptor=$("#modify-room-descriptor").val();
		var validate=true;
		if(rname==""||rname==null) {
			validate=false;
			$("#modify-room-rname").parent().addClass("has-error");
		} else {
			$("#modify-room-rname").parent().removeClass("has-error");
		}
		if(location==""||location==null) {
			validate=false;
			$("#modify-room-location").parent().addClass("has-error");
		} else {
			$("#modify-room-location").parent().removeClass("has-error");
		}
		if(area==""||area==null||!isNum(area)) {
			validate=false;
			$("#modify-room-area").parent().addClass("has-error");
		} else {
			$("#modify-room-area").parent().removeClass("has-error");
		}
		if(price==""||price==null||!isNum(price)) {
			validate=false;
			$("#modify-room-price").parent().addClass("has-error");
		} else {
			$("#modify-room-price").parent().removeClass("has-error");
		}
		if(available==""||available==null||!isInteger(available)) {
			validate=false;
			$("#modify-room-available").parent().addClass("has-error");
		} else {
			$("#modify-room-available").parent().removeClass("has-error");
		}
		if(descriptor==""||descriptor==null) {
			validate=false;
			$("#modify-room-descriptor").parent().addClass("has-error");
		} else {
			$("#modify-room-descriptor").parent().removeClass("has-error");
		}
		if(validate) {
			RoomManager.modifyRoom(modifyingRid , rname, number, location, area, price, available, descriptor, function(rid){
				$.messager.popup("修改成功！");
				loadRooms();
			});
		}
	});
	
	//清除修改房间表单
	$("#modify-room-modal").on("hide.bs.modal", function (e) {
		$("#modify-room-form .input-group input").val("");
		$("#modify-room-form .input-group textarea").val("");
		$("#modify-room-form .input-group").removeClass("has-error");
	});
});

function loadRooms() {
	RoomManager.searchRoomForAdmin(getThisYearStart(), getThisYearEnd(), -1, null, null , function(rooms) {
		$("#room-list tbody").mengularClear();
		for(var i in rooms) {
			$("#room-list tbody").mengular(".room-list-template", {
				rid: rooms[i].rid,
				createDate: rooms[i].createDate.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
				rname: rooms[i].rname,
				number: rooms[i].number,
				sold: rooms[i].sold,
				available: rooms[i].available
			});

			//房间可用状态
			$("#"+rooms[i].rid+" .room-list-enable input").bootstrapSwitch({
				state: rooms[i].enable
			}).on('switchChange.bootstrapSwitch', function(event, state) {
				var rid=$(this).parent().parent().parent().parent().attr("id");
				RoomManager.enableRoom(rid, state);
			});
			
			//管理房间
			$("#"+rooms[i].rid+" .room-list-manage").click(function() {
				modifyingRid=$(this).parent().attr("id");
				RoomManager.getRoom(modifyingRid, function(room){
					fillValue({
						"modify-room-rname": room.rname,
						"modify-room-number": room.number,
						"modify-room-location": room.location,
						"modify-room-area": room.area,
						"modify-room-price": room.price,
						"modify-room-available": room.available,
						"modify-room-descriptor": room.descriptor
					});
					//加载照片
					$("#room-photo-list").mengularClear();
					PhotoManager.getPhotosByRid(modifyingRid, function(photos) {
						for(var i in photos) {
							$("#room-photo-list").mengular(".room-photo-template", {
								pid: photos[i].pid,
				    			src: "upload/"+modifyingRid+"/"+photos[i].filename
				    		});						
							//设定封面图片
							if(room.cover!=null) {
								if(room.cover.pid==photos[i].pid) {
									$("#"+photos[i].pid+" .room-photo-cover")
				    					.removeClass("button-action")
				    					.addClass("button-primary")
				    					.text("封面图片");
								}
							}
							//绑定删除图片按钮点击事件
			    			$("#"+photos[i].pid+" .room-photo-delete").click(function() {
			    				var pid=$(this).parent().attr("id");
			    				PhotoManager.deletePhoto(pid, function() {
			    					$("#"+pid).remove();
			    				});
			    			});
			    			//绑定设定图片封面按钮点击事件
			    			$("#"+photos[i].pid+" .room-photo-cover").click(function() {
			    				var pid=$(this).parent().attr("id");
			    				PhotoManager.setAsRoomCover(pid, function() {
			    					$(".room-photo-cover").removeClass("button-primary")
				    					.removeClass("button-action")
				    					.addClass("button-action")
				    					.text("设为封面");		
			    					$("#"+pid+" .room-photo-cover")	
			    						.removeClass("button-action")
				    					.addClass("button-primary")
				    					.text("封面图片");
			    				});
			    			});
						}
					});
				})
				
				//上传照片
				$("#room-upload-photo").fileupload({
			    	autoUpload:true,
			    	url:"PhotoServlet?task=uploadRoomrPhoto&rid="+modifyingRid,
			    	dataType:"json",
			    	acceptFileTypes: /^image\/(gif|jpeg|png)$/,
			    	done:function(e,data){
			    		$("#room-photo-list").mengular(".room-photo-template", {
			    			pid: data.result.pid,
			    			src: "upload/"+modifyingRid+"/"+data.result.filename
			    		});
						//绑定删除图片按钮点击事件
		    			$("#"+data.result.pid+" .room-photo-delete").click(function() {
		    				var pid=$(this).parent().attr("id");
		    				PhotoManager.deletePhoto(pid, function() {
		    					$("#"+pid).remove();
		    				});
		    			});
		    			//绑定设定图片封面按钮点击事件
		    			$("#"+data.result.pid+" .room-photo-cover").click(function() {
		    				var pid=$(this).parent().attr("id");
		    				PhotoManager.setAsRoomCover(pid, function() {
		    					$(".room-photo-cover").removeClass("button-primary")
			    					.removeClass("button-action")
			    					.addClass("button-action")
			    					.text("设为封面");		
		    					$("#"+pid+" .room-photo-cover")	
		    						.removeClass("button-action")
			    					.addClass("button-primary")
			    					.text("封面图片");
		    				});
		    			});
			    		setTimeout(function(){
							$("#upload-room-photo-progress").hide(1500);
						},2000);
			    	},
			    	progressall:function(e,data){
						$("#upload-room-photo-progress").show();
					    var progress=parseInt(data.loaded/data.total*100, 10);
					    $("#upload-room-photo-progress .progress-bar").css("width",progress+"%");
					    $("#upload-room-photo-progress .progress-bar").text(progress+"%");
			    	}
				});
				$("#modify-room-modal").modal("show");
			});
			
			//删除房间
			 $("#"+rooms[i].rid+" .room-list-delete").click(function() {
				var rid=$(this).parent().attr("id");
				$.messager.confirm("提示","确定要删除房间："+$("#"+rid+" .room-list-rname").text()+"吗？", function() {
					RoomManager.removeRoom(rid, function(success) {
						if(success) 
							$("#"+rid).remove();
						else 
							$.messager.popup("该房间已被售出，不允许删除！");
					});
				});
			});
		}
	});
}