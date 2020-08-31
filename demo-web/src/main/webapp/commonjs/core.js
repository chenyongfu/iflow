/**
 * 公共方法 add by chenyf
 * 
 */
var cathy = {};

// 分辨率宽度，高度
cathy.screenWidth  = window.screen.width;
cathy.screenHeight = window.screen.height;

// 浏览器当前窗口可视区域宽度，高度
cathy.windowWidth  = $(window).width(); 
cathy.windowHeight = $(window).height(); 

/**
 * 获取根路径: contextPath
 */
cathy.getContextPath = function() {
	var pathName = window.document.location.pathname;
	if(pathName == '/' || pathName == '') {
		return '/';
	}
	var index = pathName.substr(1).indexOf("/");
	return pathName.substr(0, index + 1);
}

/**
 * 从URL获取参数
 * @param {} paras
 * @return {String}
 */
cathy.getParameter = function(key) {
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    for ( var i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j
                .indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[key.toLowerCase()];
    if (typeof (returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
};

/**
 * 处理url
 */
cathy.smartPath = function(url) {
	if(!url){
		return null;
	}
	
	// 站外地址
	if(url.indexOf('http://') == 0 || url.indexOf('https://') == 0){
		return url;
	}
	
	// 站内地址
	if(url.indexOf('/') != 0){
		url = '/'+ url;
	}
	// var context = cathy.getContextPath();
	// if(context != '/') {
	// 	url = context + url;
	// }
	return url;
}

/**
 * 删除 左右两端 的空格
 */
cathy.trim = function(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 获取cookie
 */
cathy.getCookie = function(cathy) {// 获取指定名称的cookie的值
	var arrStr = document.cookie.split("; ");
	for (var i = 0; i < arrStr.length; i++) {
		var temp = arrStr[i].split("=");
		if (temp[0] == cathy){
			return unescape(temp[1]);
		}
	}
};

/**
 * 設置cookie
 */
cathy.setCookie = function(key, value) {
	var days = 30; // 此 cookie 将被保存 30 天
	var exp = new Date(); // new Date("December 31, 9998");
	exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
	document.cookie = key + "=" + escape(value) + ";expires="+ exp.toGMTString();
};

/**
 * 删除cookie
 */
cathy.deleteCookie = function(key) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval = cathy.getCookie(key);
	if (cval != null) {
		document.cookie = key + "=" + cval + ";expires=" + exp.toGMTString();
	}
};

/**
 * 公用跳转菜单方法
 * @param dom
 */
cathy.redirect = function(dom){
	var mainTab = $('#mainTabPanel');
	var title = dom.textContent;
	if(mainTab.tabs('exists', title)){
		// 已经打开过的，直接激活并刷新
		$('#mainTabPanel').tabs('select', title)
	} else{

		var id = dom.id;
		if(id && id.indexOf('_') == 0){
			id = id.substring(1, id.length);
		}
		var url = cathy.smartPath(id);
		var iframe = '<iframe scrolling="no" frameborder="0"  src="'+ url +'" style="width:100%;height:100%;"></iframe>';
		
		// 增加一个tab
		$('#mainTabPanel').tabs('add', {
			id: id,
            title: title,
            closable: true,
            content: iframe
        });
	}
}

/**
 * 消息提示框
 * 右下角冒出来
 */
cathy.notify = function(msg){
	$.messager.show({
		title: '系统提示',
		iconCls: 'icon-user',
		msg: msg,
		timeout: 3000
	});
}

/**
 * 确认对话框
 * 系统的确认对话框居中太丑
 */
cathy.confirm = function(htmlTitle, callback){
	$.messager.confirm('确认', htmlTitle, function(ok){
		if(ok && callback){
			callback();
		}
	});
}; 

/**
 * 封装alert
 * 系统的确认对话框居中太丑
 */
cathy.alert = function(msg){
	$.messager.alert('系统消息', msg, 'info');
}; 

/**
 * 封装error
 * 系统的确认对话框居中太丑
 */
cathy.error = function(htmlState){
	if(htmlState && htmlState.indexOf('<h1>') == 0){
		$.messager.alert('系统消息', '服务器异常，请稍后重试<br>'+ htmlState, 'error');
	}
};

/**
 * 格式化时间为：yyyy-mm-dd 格式
 */
cathy.format2Ymd = function(time) {
	if(time && !isNaN(time)){
		var date = new Date(time);
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		if(m <= 9){
			m = '0'+ m;
		}
		var d = date.getDate();
		if(d <= 9){
			d = '0'+ d;
		}
		return y +'-'+ m +'-'+ d;
	}
    return null;
}

/**
 * 选中树的第一个节点
 */
cathy.selectFirstTreeNode = function(id){
	var _dom = $('#'+ id);
	if(_dom){
		var children = _dom.tree('getRoots');
		if(children && children[0]){
			_dom.tree('select', children[0].target);
		}
	}
}

/**
 * 选中当前节点的上一个树节点
 * 如果有兄弟节点，选中前面一个兄弟节点，没有则选中父节点
 */
cathy.selectPreviousNode = function(treeId){
	var treeDom = $('#'+ treeId +'');
	if(!treeDom){
		return;
	}
	var node = treeDom.tree('getSelected');
	if(!node || !node.target){
		return;
	}
	
	// 父节点
	var parent = treeDom.tree('getParent', node.target);
	if(!parent){
		// 说明是顶层节点，选中上一个兄弟节点
		var roots = treeDom.tree('getRoots');
		if(roots && roots.length > 0){
			var index = 0;
			for(var i=0; i<roots.length; i++){
				if((node.itemId && node.itemId == roots[i].itemId) 
				    || (node.id && node.id == roots[i].id)){
					index = i;
					break;
				}
			}
			if(index == 0){
                index = index + 1;
            }else{
                index = index - 1;
            }
			treeDom.tree('select', roots[index].target);
		}
		return;		
	}
	
	if(parent.children && parent.children.length > 1){
		// 有兄弟节点，选中
		var index = 0;
        for(var j=0; j<parent.children.length; j++){
            if((node.itemId && node.itemId == parent.children[j].itemId) 
                || (node.id && node.id == parent.children[j].id)){
                index = j;
                break;
            }
        }
        if(index == 0){
        	index = index + 1;
        }else{
        	index = index - 1;
        }
        treeDom.tree('select', parent.children[index].target);
        return;
	}
	treeDom.tree('select', parent.target);
}

/* 本系列框架中,一些用得上的小功能函数,一些UI必须使用到它们,用户也可以单独拿出来用 */

// 获取一个DIV的绝对坐标的功能函数,即使是非绝对定位,一样能获取到
function getElCoordinate(dom) {
    var t = dom.offsetTop;
    var l = dom.offsetLeft;
    dom = dom.offsetParent;
    while (dom) {
        t += dom.offsetTop;
        l += dom.offsetLeft;
        dom = dom.offsetParent;
    };
    return {
        top : t,
        left : l
    };
}
// 兼容各种浏览器的,获取鼠标真实位置
function mousePosition(ev) {
    if (!ev)
        ev = window.event;
    if (ev.pageX || ev.pageY) {
        return {
            x : ev.pageX,
            y : ev.pageY
        };
    }
    return {
        x : ev.clientX + document.documentElement.scrollLeft
                - document.body.clientLeft,
        y : ev.clientY + document.documentElement.scrollTop
                - document.body.clientTop
    };
}
// 给DATE类添加一个格式化输出字串的方法
Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second ‘
        // quarter
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        "S" : this.getMilliseconds()
        // millisecond
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
                        - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                            ? o[k]
                            : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
// JS]根据格式字符串分析日期（MM与自动匹配两位的09和一位的9）
// alert(getDateFromFormat(sDate,sFormat));
function getDateFromFormat(dateString, formatString) {
    var regDate = /\d+/g;
    var regFormat = /[YyMmdHhSs]+/g;
    var dateMatches = dateString.match(regDate);
    var formatmatches = formatString.match(regFormat);
    var date = new Date();
    for (var i = 0; i < dateMatches.length; i++) {
        switch (formatmatches[i].substring(0, 1)) {
            case 'Y' :
            case 'y' :
                date.setFullYear(parseInt(dateMatches[i]));
                break;
            case 'M' :
                date.setMonth(parseInt(dateMatches[i]) - 1);
                break;
            case 'd' :
                date.setDate(parseInt(dateMatches[i]));
                break;
            case 'H' :
            case 'h' :
                date.setHours(parseInt(dateMatches[i]));
                break;
            case 'm' :
                date.setMinutes(parseInt(dateMatches[i]));
                break;
            case 's' :
                date.setSeconds(parseInt(dateMatches[i]));
                break;
        }
    }
    return date;
}
// 货币分析成浮点数
// alert(parseCurrency("￥1,900,000.12"));
function parseCurrency(currentString) {
    var regParser = /[\d\.]+/g;
    var matches = currentString.match(regParser);
    var result = '';
    var dot = false;
    for (var i = 0; i < matches.length; i++) {
        var temp = matches[i];
        if (temp == '.') {
            if (dot)
                continue;
        }
        result += temp;
    }
    return parseFloat(result);
}

// 将#XXXXXX颜色格式转换为RGB格式，并附加上透明度
function brgba(hex, opacity) {
    if (!/#?\d+/g.test(hex))
        return hex; // 如果是“red”格式的颜色值，则不转换。//正则错误，参考后面的PS内容
    var h = hex.charAt(0) == "#" ? hex.substring(1) : hex, r = parseInt(h
                    .substring(0, 2), 16), g = parseInt(h.substring(2, 4), 16), b = parseInt(
            h.substring(4, 6), 16), a = opacity;
    return "rgba(" + r + "," + g + "," + b + "," + a + ")";
}
