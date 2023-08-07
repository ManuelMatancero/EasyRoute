$(document).ready(function(){
       
    $('ul li a').click(function() {
    $('ul li.active').removeClass('active');
    $(this).closest('li').addClass('active');
});

});

