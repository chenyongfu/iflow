<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file="/resource/common_include4easyui.jsp"%>

<script type="text/javascript">
    // 已有的模板数据
    var globalComponentMap = '${componentMap}';
</script>

<link rel="stylesheet" type="text/css" href="<c:url value="/pages/iflow/css/gooFlow.css"/>"/>
<script type="text/javascript" src="<c:url value="/pages/iflow/js/gooFlow.js"/>"></script>
<script type="text/javascript" src="<c:url value="/pages/iflow/js/iflowDesigner.js"/>"></script>
</head>

<body class="easyui-layout" data-options="border:false" style="margin:1px;">   
    <div data-options="region:'center', fit:true,border:false" style="overflow:hidden;">
        <!-- 模板表头 -->
        <div id="designerTitle" style="background:#E0ECFF;border:1px solid #ccc;border-right:none;">
            <span class='icon-grid-title' style='display:inline-block; margin-left:5px; margin-top:6px; width:16px;'>&nbsp;</span>
            <span class="l-btn-text" style='display:inline-block; margin-top:3px; margin-left:-1px;'>模板设计</span>
            <div style="float: right">
                <a id='saveBtn' onclick='save()' href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
                <span style='display:inline-block; width:13px;'></span>
            </div>
        </div>
        <div id="designerDiv"></div>
    </div>
    <div data-options="region:'east'" style="width:300px;border-top:none;">
        <!-- 属性表头 -->
        <div style="background:#E0ECFF;border:1px solid #ccc;border-left:none;">
            <span class='icon-grid-title' style='display:inline-block; margin-left:5px; margin-top:6px; width:16px;'>&nbsp;</span>
            <span class="l-btn-text" style='display:inline-block; margin-top:3px; margin-left:-1px;'>属性</span>
        </div>
        
        <form id="propertyForm">
	        <div id='taskInfo' style="display:none;">
		        <table border=0 style="width:100%;margin-top:5px;">
		            <tr>
		                <td width=80>处理人类型: </td>
	                    <td>
	                        <input type="radio" class="whodoItem" name="whodoWay" value=1 checked="checked"/><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>指定处理人</label>
	                        <input type="radio" class="whodoItem" name="whodoWay" value=2 /><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>指定bean</label>
	                    </td>
		            </tr>
		            <tr class="whodoClass">
		                <td>处理人ID: </td>
		                <td>
	                        <input class="easyui-textbox" id="whodoId" name="whodoId" style="width:190px;"></input>
	                    </td>
		            </tr>
		            <tr class="whodoClass">
                        <td>email: </td>
                        <td>
                            <input class="easyui-textbox" id="email" name="email" style="width:190px;"></input>
                        </td>
                    </tr>
                    <tr class="whodoClass">
                        <td></td>
                        <td>执行到这一步时，会发送提醒邮件</td>
                    </tr>
		            <tr class="beanClass" style="display:none;">
	                    <td>spring beanId: </td>
	                    <td>
	                        <input class="easyui-textbox" id="whodoBean" name="whodoBean" style="width:190px;"></input>
	                    </td>
	                </tr>
	                <tr class="beanClass">
	                    <td></td>
                        <td>该bean需要<lable style="color:red;">实现IflowHandler接口</lable></td>
                    </tr>
		        </table>
		    </div>
	        
	        <div id="actionInfo" style="display:none;">
	            <table border=0 style="width:100%;margin-top:5px;">
	                <tr>
	                    <td width=80>spring beanId: </td>
	                    <td>
	                        <input class="easyui-textbox" id="actionBean" name="actionBean" style="width:190px;"></input>
	                    </td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td>该bean需要<lable style="color:red;">实现IflowAction接口</lable></td>
	                </tr>
	            </table>
	        </div>
	        
	        <div id="arrowInfo" style="display:none;">
	            <table border=0 style="width:100%;margin-top:5px;">
	                <tr>
		                <td width=80>连接类型: </td>
		                <td>
		                    <input type="radio" class="arrowItem" name="arrowType" value=1 checked="checked"/><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>无条件</label>
		                    <input type="radio" class="arrowItem" name="arrowType" value=2 /><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>条件表达式</label>
		                </td>
	                </tr>
	                <tr class="arrowClass" style="display:none;">
	                    <td colspan="2">
	                        <textarea id="arrowExpression" name="arrowExpression" rows="5" style="width:280px;border-radius:5px 5px 5px 5px;border:1px solid #95B8E7;"></textarea>
	                    </td>
	                </tr>
	            </table>
	        </div>
	        
	        <div id="forkInfo" style="display:none;">
	            <table border=0 style="width:100%;margin-top:5px;">
	                <tr>
	                    <td width=80>会签类型: </td>
	                    <td>
	                        <input type="radio" class="signItem" name="signWay" value=1 checked="checked"/><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>按条件选择</label>
	                    </td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td>
	                        <input type="radio" class="signItem" name="signWay" value=2 /><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>全部通过</label>
	                    </td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td>
	                        <input type="radio" class="signItem" name="signWay" value=3 /><label style='display:inline-block; vertical-align:middle; margin-top:-7px;'>指定数目</label>
	                    </td>
	                </tr>
	                <tr class="signClass" style="display:none;">
	                    <td width=80>会签数目: </td>
	                    <td>
	                        <input class="easyui-textbox" id="needSignCount" name="needSignCount" style="width:190px;"></input>
	                    </td>
	                </tr>
	            </table>
	        </div>
        </form>
    </div>
    
</body>
</html>
