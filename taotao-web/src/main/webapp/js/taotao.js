var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_USER_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://sso.taotao.com/service/user/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data){
					//var _data = JSON.parse(data);
					var html =data.username+"，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});