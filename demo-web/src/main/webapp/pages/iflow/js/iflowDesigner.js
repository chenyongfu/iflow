
var globalDesigner = null;

$(function(){
	
	// 标题宽度
    $("#designerTitle").css({
        width: cathy.windowWidth - 300 +"px"
    });
	
    // 注册表单值变事件
    regsiterChange();
    
    // 设计器
    globalDesigner = $.createGooFlow({
        dom: $("#designerDiv"),
        width: cathy.windowWidth - 300,
        height: cathy.windowHeight,
        useOperStack: true,
        onNodeFocus: function(e, type, id){
        	if(!type){
        		return;
        	}
        	// 控制表单显示
            $("#taskInfo").hide();
            $("#actionInfo").hide();
            $("#arrowInfo").hide();
            $("#forkInfo").hide();
            
            if(type == 'ico_task'){
            	$("#taskInfo").show();
            }else if(type == 'ico_node'){
                $("#actionInfo").show();
            }else if(type == 'line'){
                $("#arrowInfo").show();
            }else if(type.indexOf('fork') >= 0){
                $("#forkInfo").show();
            }
            // 联动表单数据
            var data = findDataById(id);
            loadFormData(data);
        }
    });
    
    var componentMap = globalComponentMap;
    if(componentMap){
        componentMap = JSON.parse(componentMap);
        var templateId = cathy.getParameter('templateId');
        if(templateId){
            globalDesigner.loadData(componentMap);
        }
    }
    
});

/**
 * 注册表单值变事件
 */
function regsiterChange(){
    // 绑定处理人类型切换事件
    $(".whodoItem").change(function(){
        $(".beanClass").hide();
        $(".whodoClass").hide();
        var whodoWay = $('input:radio[name=whodoWay]:checked').val();
        if(whodoWay == 1){
            $(".whodoClass").show();
        }else{
            $(".beanClass").show();
        }
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.whodoWay = whodoWay;
    });
    // 绑定连接类型切换事件
    $(".arrowItem").change(function(){
        $(".arrowClass").hide();
        var arrowType = $('input:radio[name=arrowType]:checked').val();
        if(arrowType == 2){
            $(".arrowClass").show();
        }
        
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.arrowType = arrowType;
    });
    // 绑定会签类型切换事件
    $(".signItem").change(function(){
        $(".signClass").hide();
        var signWay = $('input:radio[name=signWay]:checked').val();
        if(signWay == 2){
            $(".signClass").show();
        }
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.signWay = signWay;
    });
    $('#whodoId').textbox('textbox').bind('blur', function(e){
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.whodoId = e.target.value;
    });
    $('#email').textbox('textbox').bind('blur', function(e){
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.email = e.target.value;
    });
    $('#whodoBean').textbox('textbox').bind('blur', function(e){
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.whodoBean = e.target.value;
    });
    $('#actionBean').textbox('textbox').bind('blur', function(e){
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.actionBean = e.target.value;
    });
    $('#arrowExpression').on('blur', function(e){
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.arrowExpression = e.target.value;
    });
    $('#needSignCount').textbox('textbox').bind('blur', function(e){
        // 绑定值到组件
        var id = globalDesigner.$focus;
        var data = findDataById(id);
        // 引用传递，直接修改即可
        data.needSignCount = e.target.value;
    });
}

/**
 * 保存模板
 */
function save(){
	if(globalDesigner){
		var nd = globalDesigner.$nodeData;
		var ld = globalDesigner.$lineData;
		var di = globalDesigner.$deletedItem;
		
		var nodeArr = [];
		if(nd){
			for(var key in nd){
				var item = nd[key];
				item.relationId = key;
				if (typeof item != " function " ){
					nodeArr.push(item);
				}
            }
		}
		var lineArr = [];
        if(ld){
            for(var key in ld){
                var item = ld[key];
                item.relationId = key;
                if (typeof item != " function " ){
                    lineArr.push(item);
                }
            }
        }
        var deleteItem = '';
        if(di){
            for(var key in di){
                if (typeof key != " function " ){
                    deleteItem = deleteItem + key +','
                }
            }
            deleteItem = deleteItem.substring(0, deleteItem.length - 1);
        }
        
        var templateId = cathy.getParameter('templateId');
		var param = {
			templateId: templateId,
            nodeData: JSON.stringify(nodeArr),
            lineData: JSON.stringify(lineArr),
            deleteItem: deleteItem
		};
		$.post(cathy.smartPath('/iflow/saveIflowDesigner'), param, function(res){
			if(res){
				var json = JSON.parse(res);
				if(json.success == 'success'){
                    cathy.notify("保存成功");
                    globalDesigner.loadData(json);
                    return;
                }
                cathy.notify('保存失败');
			}
        });
	}
}

/**
 * 加载表单数据
 * @param {} data
 * @param {} type
 * @param {} id
 */
function loadFormData(data){
	if(!data){
		return;
	}
	// 先清空
	$('#propertyForm').form('reset');
	$('#propertyForm').form('load', data);
	
    // 默认显示
    var whodoWay = parseInt(data.whodoWay ? data.whodoWay : 1);
    $(".whodoClass").hide();
    $(".beanClass").hide();
    if(whodoWay == 1){
        $(".whodoClass").show();
    }else{
        $(".beanClass").show();
    }
    var arrowType = parseInt(data.arrowType ? data.arrowType : 1);
    $(".arrowClass").hide();
    if(arrowType == 2){
        $(".arrowClass").show();
    }
    var signWay = parseInt(data.signWay ? data.signWay : 1);
	$(".signClass").hide();
	if(signWay == 2){
        $(".signClass").show();
    }
}

/**
 * 根据组件ID，获取数据
 * @param {} id
 * @return {}
 */
function findDataById(id){
	if(!id || !globalDesigner){
	   return null;
	}
	var nd = globalDesigner.$nodeData;
    var ld = globalDesigner.$lineData;
    
    var data = nd[id];
    if(!data){
    	data = ld[id];
    }
    return data;
}
