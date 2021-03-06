var pageSize=15;

var replyingMid;

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
		searchMessages("", "", null, null, null, -1, 1);
	})
	
	$("#search-message-start, #search-message-end").datetimepicker({
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
	for(var i=1; i<=100; i++) {
		var option=$("<option>").val(i).text(i);
		if(pageSize==i)
			option.attr("selected","selected");
		$("#page-size").append(option);
	}
	 
	//更改每页显示的条数
	$("#page-size").change(function() {
		pageSize=$(this).val();
		var start=$("#search-message-start").val();
    	var end=$("#search-message-end").val();
    	var name=$("#search-message-name").val();
    	var email=$("#search-message-email").val();
    	var telephone=$("#search-message-telephone").val();
    	var looked=$("#search-message-looked").val();
    	searchMessages(start, end, name, email, telephone, looked, 1);
	});

    //搜索留言
    $("#search-message-submit").click(function() {
    	var start=$("#search-message-start").val();
    	var end=$("#search-message-end").val();
    	var name=$("#search-message-name").val();
    	var email=$("#search-message-email").val();
    	var telephone=$("#search-message-telephone").val();
    	var looked=$("#search-message-looked").val();
    	searchMessages(start, end, name, email, telephone, looked, 1);
    });

    //还原搜索
    $("#search-message-reset").click(function() {
    	$("#search-message-form input").val("");
    	$("#search-message-looked").val(-1);
    	searchMessages("", "", null, null, null, -1, 1);
    });

    //邮件回复
    $("#reply-message-email").click(function() {
    	var reply=$("#show-message-reply").val();
    	$(this).text("正在发送邮件..").attr("disabled", "disabled");
    	MessageManager.replyByEmail(replyingMid, reply, function(success) {
    		$("#reply-message-email").text("邮件回复留言者").removeAttr("disabled");
    		if(success)
    			$.messager.popup("邮件回复成功！");
    		else
    			$.messager.popup("邮件回复失败！");
    	});
    });

    //短信回复
    $("#reply-message-sms").click(function() {
    	var reply=$("#show-message-reply").val();
    	$(this).text("正在发送短信..").attr("disabled", "disabled");
    	MessageManager.replyBySMS(replyingMid, reply, function(success) {
    		$("#reply-message-sms").text("短信回复留言者").removeAttr("disabled");
    		if(success)
    			$.messager.popup("短信回复成功！");
    		else
    			$.messager.popup("短信回复失败！这可能是短信过长或者短信中关键词被运营商屏蔽，请修改后再试！");
    	});
    });
});

function searchMessages(start, end, name, email, telephone, looked, page) {
	//加载页码
	MessageManager.getMessagesCount(start, end, name, email, telephone, looked, function(count) {
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
				searchMessages(start, end, name, telephone, email, looked, index+1);
			});
		});
	});

	MessageManager.searchMessages(start, end, name, email, telephone, looked, page, pageSize, function(messages) {
		$("#message-list tbody").mengularClear();
		for(var i in messages) {
			$("#message-list tbody").mengular(".message-list-template", {
				mid: messages[i].mid,
				date: messages[i].date.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
				name: messages[i].name,
				email: messages[i].email,
				telephone: messages[i].telephone
			});

			//标记已读和未读
			$("#"+messages[i].mid+" .message-list-looked input").bootstrapSwitch({
				state: messages[i].looked
			}).on('switchChange.bootstrapSwitch', function(event, state) {
				var mid=$(this).parent().parent().parent().parent().attr("id");
				MessageManager.looked(mid, state);
			});

			//显示留言内容
			$("#"+messages[i].mid+" .message-list-show").click(function() {
				replyingMid=$(this).parent().attr("id");
				MessageManager.getMessage(replyingMid, function(message) {
					fillText({
						"show-message-name": message.name,
						"show-message-email": message.email,
						"show-message-telephone": message.telephone,
						"show-message-content": message.content
					});
					$("#show-message-modal").modal("show");
				});
			});

			//删除留言
			$("#"+messages[i].mid+" .message-list-delete").click(function() {
				var mid=$(this).parent().attr("id");
				var name=$("#"+mid+" .message-list-name").text();
				$.messager.confirm("提示", "确认删除"+name+"的留言", function() { 
					MessageManager.removeMessage(mid, function() {
						$("#"+mid).remove();
					});
				});
			});
		}
	});
}