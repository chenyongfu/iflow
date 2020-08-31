
/**
作者：张宇
日期：2010－2－2
版本：1.1.2
属性：
	coordinate	四个数字在一起是画线（箭头）所有的数值:
	            第一个数表示第一个点的x坐标，第二个数表示第一个点的y坐标，
				第三个数表示第二个点的x坐标，第四个数表示第二个点的y坐标
	color		线（箭头）或点的颜色
	parentId	图形父结点的id
	pointName	图形的名称
	arrowSize	箭头的宽高
	cutArrowLen	箭头被剪掉的长度
	type		point(点) line(线) arrow(箭头) cutArrow(缩短箭头)
	position	图形偏移量
	click		图形上的单击事件,第一个参数是事件源(event),第二个参数是线条(点)的名称
*/

var Graphics = function(){
	this.coordinate = [0, 0 , 0 , 0]; 
	this.color = '#333';
	this.className = '';
	this.parentId = 'arrow';
	this.arrowSize = {'height':20, 'width':10};
	this.type = 'cutArrow';
	this.cutArrowLen = [30, 30];
	this.position = [0, 0, 0, 0];
	this.click = function(e, name){};
	this.description = '';
}
Graphics.prototype = {
	/**
	 * 画点的方法（自定义参数）
	 * @param {} x
	 * @param {} y
	 * @return {}
	 */
	drawPoint:function(x, y){
		var D = document.createElement('div');
		D.style.top = y + 'px';
		D.style.left = x + 'px';
		D.style.position = 'absolute';
		D.style.width = '1px';
		D.style.height = '1px';
		D.style.overflow = 'hidden';
		D.style.backgroundColor = this.color;
		D.className = this.className,
		D.setAttribute('name', this.className);
		D.onclick = function(e){
            var e = e || event;
            this.click(e, this);
        }
		document.getElementById(this.parentId).appendChild(D);
		return D;
	},
	/**
	 * 画点的方法（无参数，读取类中的变量）
	 */
	point:function(){
		var x1 = this.coordinate[0];
		var y1 = this.coordinate[1];
		this.drawPoint(x1, y1);	
	},
	/**
	 * 画线的方法（自定义参数）
	 * @param {} x1
	 * @param {} y1
	 * @param {} x2
	 * @param {} y2
	 */
	drawLine:function(x1, y1, x2, y2){
		var x1 = parseInt(x1);
		var y1 = parseInt(y1);
		var x2 = parseInt(x2);
		var y2 = parseInt(y2);
		var maxLen = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
		var x = (x2 - x1)/maxLen;
		var y = (y2 - y1)/maxLen;
		if(maxLen == 0){
			x = 0;
			y = 0;
		}
		for(var i = 0; i <= maxLen; i++){
			this.drawPoint(x1 + i * x, y1 + i * y); 
		}
	},
	/**
	 * 画线的方法（无参数，读取类中的变量）
	 */
	line:function(){
		var x1 = parseInt(this.coordinate[0]) + parseInt(this.position[0]);
		var y1 = parseInt(this.coordinate[1]) + parseInt(this.position[1]);
		var x2 = parseInt(this.coordinate[2]) + parseInt(this.position[2]);
		var y2 = parseInt(this.coordinate[3]) + parseInt(this.position[3]);
		this.drawLine(x1, y1, x2, y2);
	},
	/**
	 * 画箭头的方法（无参数，读取类中的变量）
	 */
	arrow:function(){
		var x1 = parseInt(this.coordinate[0]) + parseInt(this.position[0]);
		var y1 = parseInt(this.coordinate[1]) + parseInt(this.position[1]);
		var x2 = parseInt(this.coordinate[2]) + parseInt(this.position[2]);
		var y2 = parseInt(this.coordinate[3]) + parseInt(this.position[3]);
		var len = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		if(len == 0){
			createPoint(x1, y1, color);
			return;
		}
		var t = (y2-y1)/(x2-x1);
		
		var angle = Math.atan(t);
		var x, y ;
		
		var len1 = Math.min(this.arrowSize.width, len/2 - 1);
		var len2 = Math.min(this.arrowSize.height, len - 2);
		
		if(x2 < x1){
			x = (len1 - len) * Math.cos(angle) + x1;
			y = (len1 - len) * Math.sin(angle) + y1;
		} else if(x2 >= x1){
			x = (len - len1) * Math.cos(angle) + x1;
			y = (len - len1) * Math.sin(angle) + y1;
		} 
		
		var x3 = x + len2 * Math.sin(angle);
		var y3 = y - len2 * Math.cos(angle);
		
		var x4 = x - len2 * Math.sin(angle);
		var y4 = y + len2 * Math.cos(angle);
		
		this.drawLine(x1, y1, x , y);
		this.drawLine(x2, y2, x3, y3);
		this.drawLine(x2, y2, x4, y4);
		this.drawLine(x3, y3, x4, y4);
	},
	execute:function(){
		eval('this.' + this.type + '()')
	}
}	

Graphics.draw = function(option){
	var g = new Graphics();
	for(var e in option){
		g[e] = option[e];
	}
	g.execute();
}
	