jQuery(document).ready(function($) {

/*$("input[placeholder='placeholder']").livequery(function(){ alert("ciao"); });*/
/*	$('input') 
    .livequery(function(){ 
    // use the helper function hover to bind a mouseover and mouseout event 
        $(this) 
            .hover(function() { 
                $(this).addClass('hover'); 
            }, function() { 
                $(this).removeClass('hover'); 
            }); 
    }, function() { 
        // unbind the mouseover and mouseout events 
        $(this) 
            .unbind('mouseover') 
            .unbind('mouseout'); 
    });*/
	
	/*$('body').find("input[placeholder='placeholder']").livequery(function(){ alert("ciao"); });*/

	
});

function intplaceholder2(){
	$(":input[placeholder]").livequery(
					function(){
						var ua = window.navigator.userAgent;
			            var msie = ua.indexOf("MSIE ");
			            
						var palceHolder = $(this).attr( 'placeholder' );
						var labelSetted = $(this).prev().is('span');
						if (!labelSetted && (msie > 0)) {
							$(this).before("<span class='label' style='display:block'>" +palceHolder +"</span>");
						}
						
						
					});
}