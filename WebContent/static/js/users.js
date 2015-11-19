//默认一页显示15个
var pageSize=15;

$(document).ready(function() {
	
	checkAdminSession(function() {
		searchUsers("", "", 1);
	})

	//加载每页显示条数的下拉菜单
	for(var i=1;i<=100;i++) {
		var option=$("<option>").val(i).text(i);
		if(pageSize==i)
			option.attr("selected","selected");
		$("#page-size").append(option);
	}
	
	//更改每页显示的条数
	$("#page-size").change(function() {
		pageSize=$(this).val();
		var telephone=$("#search-user-telephone").val();
		var uname=$("#search-user-uname").val();
		searchUsers(telephone, uname, 1);
	});

	//搜索用户
	$("#search-user-submit").click(function() {
		var telephone=$("#search-user-telephone").val();
		var uname=$("#search-user-uname").val();
		searchUsers(telephone, uname, 1);
	});

	//还原搜索
	$("#search-user-reset").click(function() {
		$("#search-user-telephone").val("");
		$("#search-user-uname").val("");
		searchUsers("", "", 1);
	});

});

/**
 * 搜索用户
 * @param uname 用户名
 * @param telephone 手机号码
 * @param page 页码
 */
function searchUsers(uname, telephone, page) {
	//加载页码
	UserManager.getUsersCount(uname, telephone, function(count) {
		$("#users-count").text(count);
		$("#user-page-nav ul").empty();
		for(var i=1; i<Math.ceil(count/pageSize+1);i++) {
			var li='<li><a href="javascript:void(0)">'+i+'</a></li>';
			if(page==i)
				li='<li class="active"><a href="javascript:void(0)">'+i+'</a></li>';
			$("#user-page-nav ul").append(li);
		}
		$("#user-page-nav ul li").each(function(index) {
			$(this).click(function() {
				searchUsers(uname, telephone, index+1);
			});
		});
	});

	//加载用户信息
	UserManager.searchUsers(uname, telephone, page, pageSize, function(users) {
		$("#user-list tbody").mengularClear();
		for(var i in users) {
			$("#user-list tbody").mengular(".user-list-template", {
				uid: users[i].uid,
				registerDate: users[i].registerDate.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
				telephone: users[i].telephone,
				uname: users[i].uname
			});
		}
	});
}