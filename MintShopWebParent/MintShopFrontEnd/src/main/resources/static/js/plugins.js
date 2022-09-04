function customeNotice(title, text) {
	const notice = PNotify.notice({
		title: title,
		text: text,
		styling: 'custom',
		maxTextHeight: null,
		addModelessClass: 'nonblock',
		icon: 'fas fa-exclamation-triangle',
		addClass: 'stack-bar-top',
		shadow: false,
		width: '100%',
		stack: window.stackBarTop,
		hide: true,
		closer: true
	});

}

function showStackBarTop(type, title, text) {
	if (typeof window.stackBarTop === 'undefined') {
		window.stackBarTop = new PNotify.Stack({
			modal: false,
			dir1: 'down',
			firstpos1: 0,
			spacing1: 0,
			push: 'top',
			maxOpen: Infinity
			
		});
	}
	const opts = {
		title: title,
		text: text,
		addClass: 'stack-bar-top',
		shadow: false,
		width: '100%',
		stack: window.stackBarTop
	};
	switch (type) {
		case 'error':
			opts.title = title;
			opts.text = text;
			opts.type = 'error';
			break;
		case 'info':
			opts.title = title;
			opts.text = text;
			opts.type = 'info';
			break;
		case 'success':
			opts.title = title;
			opts.text = text;
			opts.type = 'success';
			break;
	}
	PNotify.alert(opts);
}

/*global $, document, window*/
$(document).ready(function () {
    'use strict';
    
    /***********************************************************************/
    
    //scroll up
    var scrollButton = $(".scroll-top");
    //check if it scroll more than 700 px
    $(window).scroll(function () {
        $(this).scrollTop() >= 700 ? scrollButton.show() : scrollButton.hide();
    });
    //add scroll function to the button
    scrollButton.click(function () {
        $("html,body").animate({scrollTop : 0}, 600);
    });
    
    /***********************************************************************/
    
  
	
	/***********************************************************************/
    
});
