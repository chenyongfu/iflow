<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<!-- 引入地址栏logo图标 -->
<link rel="shortcut icon" type="image/x-icon" href="<c:url value='/resource/images/favicon.png'/>"/>

<!-- 引入 easyui -->
<link rel="stylesheet" type="text/css" href="<c:url value='/easyui/themes/default/easyui.css'/>"/> 
<link rel="stylesheet" type="text/css" href="<c:url value='/easyui/themes/icon.css'/>"/> 
<script type="text/javascript" src="<c:url value="/easyui/jquery.min.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/easyui/jquery.easyui.min.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/commonjs/move_limit.js"/>"></script>
<script type="text/javascript" src="<c:url value="/commonjs/json2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/commonjs/core.js"/>"></script> 
<script type="text/javascript" src="<c:url value="/commonjs/override.js"/>"></script>
<script type="text/javascript" src="<c:url value="/commonjs/easyui_validate.js"/>"></script>

<!-- 增加权限操作 -->
<script type="text/javascript">
var CHECK_PERMISSION_KEY = '${check_permission_key}';
var AUTHORIZE_OPERATION_LIST = '${authorize_operation_list}';
var NO_AUTHORIZE_OPERATION_LIST = '${no_authorize_operation_list}';
</script>
<script type="text/javascript" src="<c:url value="/commonjs/authorize.js"/>"></script> 