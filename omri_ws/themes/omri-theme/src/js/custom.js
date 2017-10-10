
jQuery.noConflict();
(function($) {
    $(function() {
    	$(".omri-profile-menu").on('click',function(){
    		if($(this).hasClass("open")){
    			$(this).removeClass("open");
    		}else{
    			$(this).addClass("open");
    		}
    	});
    	$(document).click(function(e) {
    		$(".omri-profile-menu").removeClass("open");
    	});
    	
    	$(".portlet-icon-back").remove();
    });
})(jQuery);