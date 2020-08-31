<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file="/resource/common_include4easyui.jsp"%>

<script type="text/javascript" src="<c:url value="/pages/iflow/js/iflowTemplate.js"/>"></script>

</head>
<body>

    <!-- 工具栏 -->
    <div id='tb' style="background:#E0ECFF;border:1px solid #ccc;margin-top:1px;">
        <span class='icon-grid-title' style='display:inline-block; margin-left:5px; margin-top:6px; width:16px;'>&nbsp;</span>
        <span class="l-btn-text" style='display:inline-block; margin-top:3px; margin-left:-1px;'>定时任务列表</span>
        <div style="float: right">
            <a id='addBtn' onclick='addTemplate()' href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>
            <a id='editBtn' onclick='editTemplate()' href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
            <a id='designerBtn' onclick='designer()' href="#" class="easyui-linkbutton" data-options="iconCls:'icon-designer',plain:true">设计模板</a>
            <a id='deleteBtn' onclick='deleteTemplate()' href="#" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true">删除</a>
            <a id='startBtn' onclick='start()' href="#" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true">开始流程</a>
            <span style='display:inline-block; width:13px;'></span>
        </div>
    </div>
    
    <!-- 模板表格 -->
    <table id="templateGrid"></table>  
    
        <!-- 编辑面板 -->
    <div id="templateDialog" style="display:none;">
        <form id="templateForm" method="POST">
            <input type="hidden" name="templateId" id="templateId"/>
            <table cellpadding="2">
                <tr>
                    <td>模板编码:</td>
                    <td><input class="easyui-textbox" data-options="required:true" type="text" name="templateCode" validType="call[codeValidate]" invalidMessage="模板编码重复，请重新输入" style="width:290px;"></input></td>
                </tr>
                <tr>
                    <td>模板名称:</td>
                    <td><input class="easyui-textbox" data-options="required:true" type="text" name="templateName" style="width:290px;"></input></td>
                </tr>
                <tr title="绑定的表单信息页面，查看的时候会传入启动流程时传入的businessId，该页面需要管理员根据businessId查询出表单信息">
                    <td>表单地址:</td>
                    <td><input class="easyui-textbox" data-options="required:true" type="text" name="businessFormUrl" style="width:290px;"></input></td>
                </tr>
                <tr>
                    <td>状态:</td>
                    <td>
                        <select class="easyui-combobox" readonly name="state" style="width:290px;">   
                            <option value="1">就绪</option>
                            <option value="2">运行中</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>说明:</td>
                    <td><textarea rows="5" name="remark" style="width:285px;border-radius:5px 5px 5px 5px;border:1px solid #95B8E7;"></textarea></td>
                </tr>
            </table>
        </form>
    </div>

    <!-- 设计面板 -->
    <div id="designerDialog"></div>

</body>
</html>