
var easyuiPanelOnMove = function(left, top){
	
	if(left < 0){
		$(this).window('move', {
			left: 1
		});
	}
	if(top < 0){
		$(this).window('move', {
			top: 1
		});
	}
	
	var width = $(this).panel('options').width;
	var height = $(this).panel('options').height;
	
	var parentPanel = $(this).panel('panel').parent();
	var parentPanelWidth = parentPanel.width();
	var parentPanelHeight = parentPanel.height();
	
	if(parentPanel.css('overflow') == 'hidden'){
		if(left > parentPanelWidth - width){
			$(this).window('move', {
				left: parentPanelWidth - width
			});
		}
	}
	
	var parentHeight = $(this).parent().height();
	if(top > parentPanelHeight - parentHeight){
		$(this).window('move', {
			top: parentPanelHeight - parentHeight -12
		});
	}
}

$.fn.panel.defaults.onMove  = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
