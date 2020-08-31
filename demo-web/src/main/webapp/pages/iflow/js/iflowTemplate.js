
$(function(){
	
	// 模板表格
    $('#templateGrid').datagrid({
        fit: true,
        border: false,
        striped: true,
        rownumbers: true,
        fitColumns: true,
        autoRowHeight: false,
        singleSelect : true,
        loadMsg: '正在加载...',
        url: cathy.smartPath('/iflow/findIflowTemplateByPage'),
        idField: 'templateId',
        columns:[[
            {field: 'templateId', title: '模板ID', width: 100},
            {field: 'templateCode', title: '模板编码', width: 100},
            {field: 'templateName', title: '模板名称', width: 100},
            {field: 'state', title: '状态', width: 100,
                formatter: function(value, row, index){
                    if(value == '1'){
                        return "就绪";
                    } else if(value == '2'){
                        return "运行中";
                    }
                }
            },
            {field: 'rejectStrategy', title: '驳回策略', width: 100,
                formatter: function(value, row, index){
                    if(value == '1'){
                        return "回到上一步";
                    } else if(value == '2'){
                        return "重新开始";
                    }
                }
            },
            {field: 'businessFormUrl', title: '表单地址', width: 200},
            {field: 'createdDate', title: '创建时间', width: 100, 
                formatter: function(value, row, index){
                    return new Date(value).format();
                }
            }
        ]],
        pagination: true
    }); 
    
});

/**
 * 设计器
 */
function designer(){
	
	var currentRow = $('#templateGrid').datagrid('getSelected');
    if(!currentRow){
        cathy.notify('请先选择一条记录');
        return;
    }
    
    if(currentRow.state != '1'){
        cathy.notify('只能设计状态为 就绪 的流程模板');
        return;
    }
	
	var url = cathy.smartPath('iflow/showDesigner.html');
	url = url +"?templateId="+ currentRow.templateId;
	var iframe = '<iframe scrolling="no" frameborder="0"  src="'+ url +'" style="width:100%;height:100%;"></iframe>';
	
	var win = $('#designerDialog').dialog({
		title: ' 流程模板设计',
		iconCls:'icon-designer',
        modal: true,
        width: cathy.windowWidth,
        height: cathy.windowHeight - 1,
        minimizable: false,
        collapsible: false,
        maximizable: false,
        content: iframe
    }); 
    
}

/**
 * 新增模板
 */
function addTemplate(){
    // 弹出框
    var dialog = $('#templateDialog').show().dialog({
        title: '新增模板',
        width: 420,
        height: 320,
        closable: false,
        shadow: true,
        modal: true,
        buttons:[{
            text:'保存',
            iconCls: 'icon-save',
            handler:function(){
                // 提交表单
                $('#templateForm').form('submit', {
                    url: cathy.smartPath('/iflow/saveIflowTemplate'),
                    onSubmit: function(param){
                        // 验证表单
                        return $('#templateForm').form('validate');
                    },
                    success: function(res){
                        if(res == 'success'){
                            cathy.notify('保存成功');
                            dialog.dialog('close');
                            $('#templateGrid').datagrid('reload');
                            return;
                        }
                        cathy.error(res);
                    }
                });
            }
        },{
            text:'返回',
            iconCls: 'icon-red-back',
            handler:function(){
                dialog.dialog('close');
            }
        }]
    }); 
    
    // 默认清空数据
    $('#templateForm').form('clear');
    
    // 默认值
    $('#templateForm').form('load', {
    	state: 1,
        rejectStrategy: '2'
    });
    
}

/**
 * 修改模板
 */
function editTemplate(){
    var currentRow = $('#templateGrid').datagrid('getSelected');
    if(!currentRow){
        cathy.notify('请先选择一条记录');
        return;
    }
    
    if(currentRow.state != '1'){
        cathy.notify('只能修改状态为 就绪 的流程模板');
        return;
    }
    
    // 弹出框
    var dialog = $('#templateDialog').show().dialog({
        title: '修改模板',
        width: 420,
        height: 320,
        closable: false,
        shadow: true,
        modal: true,
        buttons:[{
            text:'保存',
            iconCls: 'icon-save',
            handler:function(){
                // 提交表单
                $('#templateForm').form('submit', {
                    url: cathy.smartPath('/iflow/saveIflowTemplate'),
                    onSubmit: function(param){
                        // 验证表单
                        return $('#templateForm').form('validate');
                    },
                    success: function(res){
                        if(res == 'success'){
                            cathy.notify('保存成功');
                            dialog.dialog('close');
                            $('#templateGrid').datagrid('reload');
                            return;
                        }
                        cathy.error(res);
                    }
                });
            }
        },{
            text:'返回',
            iconCls: 'icon-red-back',
            handler:function(){
                dialog.dialog('close');
            }
        }]
    }); 
    
    // 默认清空数据
    $('#templateForm').form('clear');
    
    // 默认值
    $('#templateForm').form('load', currentRow);
    
}

/**
 * 删除模板
 */
function deleteTemplate(){
    var currentRow = $('#templateGrid').datagrid('getSelected');
    if(!currentRow){
        cathy.notify('请先选择一条记录');
        return;
    }
    
    if(currentRow.state != '1'){
        cathy.notify('只能删除状态为 就绪 的任务');
        return;
    }
    cathy.confirm('删除操作不可恢复，您确定要删除该模板吗？ ', function(){
        $.post(cathy.smartPath('/iflow/deleteIflowTemplate'), {id: currentRow.templateId}, function(res){
            if(res == 'success'){
                cathy.notify('流程模板删除成功');
                $('#templateGrid').datagrid('reload');
            }else{
                cathy.notify(res);
            }
        });
    });
}

function start(){
	$.post(cathy.smartPath('/iflow/start'), function(res){
        if(res == 'success'){
            cathy.notify('发起成功');
        }else{
            cathy.notify(res);
        }
    });
}

/**
 * 模板编码唯一性校验
 */
function codeValidate(code){
    var id = $('#templateId').val();
    if(id){
        // 编辑时，如果编码没有修改，则不验证
        var selected = $('#templateGrid').datagrid('getSelected');
        if(!selected){
            cathy.notify('表格当前行丢失，请重新选择一行进行编辑');
            return true;
        }
        if(selected.templateCode){
            var oldValue = selected.templateCode;
            if(oldValue == code){
                return true;
            }
        }
    }
    
    // 校验, 同步执行
    var result = true;
    $.ajax({
        async: false,
        url : cathy.smartPath('iflow/uniqueValidate'),
        data : {
            code : code
        },
        success : function(res) {
            result = res;
        }
    });
    return result;
}
