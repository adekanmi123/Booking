var pageSize=15;

$(document).ready(function() {
	$.messager.model = {
		ok:{ text: "确定", classed: 'btn-danger' },
		cancel: { text: "取消", classed: 'btn-default' }
	};

	checkAdminSession(function() {
		searchComments("", "", null, -1, 1);
	});

	$("#search-comment-start, #search-comment-end").datetimepicker({
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
		var start=$("#search-comment-start").val();
    	var end=$("#search-comment-end").val();
    	var rname=$("#search-comment-rname").val();
    	var enable=$("#search-comment-enable").val();
    	searchComments(start, end, rname, enable, 1);
	});

	//搜索评论
	$("#search-comment-submit").click(function() {
		var start=$("#search-comment-start").val();
    	var end=$("#search-comment-end").val();
    	var rname=$("#search-comment-rname").val();
    	var enable=$("#search-comment-enable").val();
    	searchComments(start, end, rname, enable, 1);
	});

	//重置搜索
	$("#search-comment-reset").click(function() {
		$("#search-comment-form input").val("");
    	$("#search-comment-enable").val(-1);
    	searchComments("", "", null, -1, 1);
	});
});

function searchComments(start, end, rname, enable, page) {
	//加载页码
	CommentManager.getCommentsCount(start, end, rname, enable, function(count) {
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
				searchComments(start, end, rname, enable, index+1);
			});
		});
	});

	//加载评论
	CommentManager.searchComments(start, end, rname, enable, page, pageSize, function(comments) {
		$("#comment-list tbody").mengularClear();
		for(var i in comments) {
			$("#comment-list tbody").mengular(".comment-list-template", {
				cid: comments[i].cid,
				commentDate: comments[i].commentDate.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
				uname: comments[i].booking.user.uname,
				rname: comments[i].booking.room.rname
			});

			//加载评分星级
			$("#"+comments[i].cid+" .comment-list-star i").each(function(index) {
				if(index+1<=comments[i].stars)
					$(this).addClass("fa-star");
				else
					$(this).addClass("fa-star-o");
			});

			//审核评论
			$("#"+comments[i].cid+" .comment-list-enable input").bootstrapSwitch({
				state: comments[i].enable
			}).on('switchChange.bootstrapSwitch', function(event, state) {
				var cid=$(this).parent().parent().parent().parent().attr("id");
				CommentManager.checkComment(cid, state);
			});

			//查看评论
			$("#"+comments[i].cid+" .comment-list-show").click(function() {
				var cid=$(this).parent().attr("id");
				CommentManager.getComment(cid, function(comment) {
					var title=comment.booking.user.uname+"关于房间"+comment.booking.room.rname+"的评论";
					$.messager.alert(title, comment.content);
				});
			});

			//删除评论
			$("#"+comments[i].cid+" .comment-list-delete").click(function() {
				var cid=$(this).parent().attr("id");
				var rname=$("#"+cid+" .comment-list-rname").text();
				$.messager.confirm("提示", "确定删除房间："+rname+"的评论吗？", function() { 
			        CommentManager.deleteComment(cid, function() {
			        	$("#"+cid).remove();
			        });
			    });
			});
		}
	});
}