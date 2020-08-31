/**
 * 全局修改组件的默认值
 */


/**
 * 消息对话框默认值重写
 */
$.messager.defaults.top = cathy.windowHeight / 2 - 100;
$.messager.defaults.ok = '确认';
$.messager.defaults.cancel = '取消';


/**
 * 重写  datebox 显示格式
 * yyyy-mm-dd
 */
$.fn.datebox.defaults.formatter = function(date) {
	if (date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		if (m <= 9) {
			m = '0' + m;
		}
		var d = date.getDate();
		if (d <= 9) {
			d = '0' + d;
		}
		return y + '-' + m + '-' + d;
	}
	return null;
}


/**
 * 解析  datebox 方法
 * value: 毫秒数
 */
$.fn.datebox.defaults.parser = function(date) {
	if (date) {
		if (!isNaN(date)) {
			return new Date(date);
		}
		return new Date(Date.parse(date.replace(/-/g, "/")));
	}
	return date;
}

/**
 * 格式化日期为： yyyy-MM-dd
 */
Date.prototype.ymd = function(format){
    if(isNaN(this.getMonth())){
        return '';
    }
    if(!format){
        format = 'yyyy-MM-dd';
    }
    var o = {
        //month
        "M+" : this.getMonth() + 1,
        //day
        "d+" : this.getDate(),
        //hour
        "h+" : this.getHours(),
        //minute
        "m+" : this.getMinutes(),
        //second
        "s+" : this.getSeconds(),
        //quarter
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        //millisecond
        "s" : this.getMilliseconds()
    };
    if(/(y+)/.test(format)){
        format = format.replace(RegExp.$1,(this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for(var k in o){
        if(new RegExp("(" + k + ")").test(format)){
            format = format.replace(RegExp.$1,RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

/**
 * 格式化日期为： yyyy-MM-dd hh:mm:ss
 */
Date.prototype.format = function(format){
    if(isNaN(this.getMonth())){
        return '';
    }
    if(!format){
        format = 'yyyy-MM-dd hh:mm:ss';
    }
    var o = {
        //month
        "M+" : this.getMonth() + 1,
        //day
        "d+" : this.getDate(),
        //hour
        "h+" : this.getHours(),
        //minute
        "m+" : this.getMinutes(),
        //second
        "s+" : this.getSeconds(),
        //quarter
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        //millisecond
        "s" : this.getMilliseconds()
    };
    if(/(y+)/.test(format)){
        format = format.replace(RegExp.$1,(this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for(var k in o){
        if(new RegExp("(" + k + ")").test(format)){
            format = format.replace(RegExp.$1,RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};
