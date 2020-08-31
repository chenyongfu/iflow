
/**
 * 这里的权限在common_include4easyui.jsp页面接收的
 */
var checkPermissionKey = CHECK_PERMISSION_KEY;
var authorizeOperationList = AUTHORIZE_OPERATION_LIST;
var noAuthorizeOperationList = NO_AUTHORIZE_OPERATION_LIST;

/**
 * 页面渲染完成后，进行权限控制
 */
$(function(){
	
	// 如果不检查权限，直接返回
	if(!checkPermissionKey){
		return;
	}
	
	// 没有权限的全部隐藏
	if(noAuthorizeOperationList){
		var invalidList = $.parseJSON(noAuthorizeOperationList);
		invalidList.forEach(function(operation){
			var op = $('#'+ operation)
			if(op){
				op.hide();
			}
		});
	}
	
	// 有权限的全部显示
	if(authorizeOperationList){
		var validList = $.parseJSON(authorizeOperationList);
		validList.forEach(function(operation){
			var op = $('#'+ operation)
			if(op){
				op.show();
			}
		});
	}
	
});