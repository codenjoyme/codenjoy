$(document).ready(function(){
  $("#nav").on("click","a", function (event) {
    event.preventDefault();
    var id = $(this).attr('href'),
    top = $(id).offset().top;
    $('body,html').animate({scrollTop: top}, 1000);
  });  
  
  
   try {
        $.browserSelector();
        if($("html").hasClass("chrome")) {
            $.smoothScroll();
        }
    } catch(err) {

    };
  
  $('#scrollup').mouseover( function(){
		$( this ).animate({opacity: 0.8},100);
	}).mouseout( function(){
		$( this ).animate({opacity: 0.5},100);
	}).click( function(){
		window.scroll(0 ,0); 
		return false;
	});

	$(window).scroll(function(){
		if ( $(document).scrollTop() > 0 ) {
			$('#scrollup').fadeIn('fast');
		} else {
			$('#scrollup').fadeOut('fast');
		}
	});
});
