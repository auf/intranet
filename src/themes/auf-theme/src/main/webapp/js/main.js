// Mocked console
(function() {
    var method;
    var noop = function () {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());

var AUF = function(){
	console.log("AUF namespace");

	return {
		init: function(){
			console.log("init");

			this.portlet_activite_image_vide();
			this.hide_employees();
			this.hide_placeholders_on_click();
		},
		portlet_activite_image_vide: function(){
			console.log("portlet_activite_image_vide");

			$(".news .photo img[src='']").attr("src", "/auf-theme/images/logo.jpg");
		},
		hide_employees: function(){
			console.log("hide_employees");

			var max_employees = 12,
				employee_selector = ".portlet-boundary_aufuserlist_WAR_aufsync_ .box_centre .news";


			console.log("length: ", $(employee_selector).length);

			if ( $(employee_selector).length > max_employees ){
				/* hide all other employees higher than max_employees variable */
				$(employee_selector).slice(max_employees).hide();

				/* add button to show 'em all after the 12th element */
				$(employee_selector + ":eq(11)").after(
					'<div class="clearfix"><input class="aui-button-input auf-show-all-employees" type="button" value="Afficher tous les employés"></div>'
				);

				/* show all employee button's click behavior */
				$("input.auf-show-all-employees").click(function(){
					$(employee_selector).slice(max_employees).fadeIn();
					$(this).fadeOut(function(){
						$(this).parent().remove()
					});
				});
			}
		},
		hide_placeholders_on_click : function(){
			console.log("hide_placeholders_on_click");

			$("input[name=_aufsync_WAR_aufsync_name], input[name=_3_keywords]").focus(function(){
				var placeholder_text = $(this).attr("placeholder");
				$(this).attr("placeholder", "").data("placeholder", placeholder_text);
			}).blur(function(){
				var $t = $(this);
				if ($t.val() === "") {
					$t.attr(
						"placeholder",
						$t.data("placeholder")
					);
				}
			});
		}
	}
}();

AUI().ready(

	/*
	This function gets loaded when all the HTML, not including the portlets, is
	loaded.
	*/

	function() {
		console.log("AUI().ready()");
		AUF.init();
	}
);

Liferay.Portlet.ready(

	/*
	This function gets loaded after each and every portlet on the page.

	portletId: the current portlet's id
	node: the Alloy Node object of the current portlet
	*/

	function(portletId, node) {

}
);

Liferay.on(
	'allPortletsReady',

	/*
	This function gets loaded when everything, including the portlets, is on
	the page.
	*/

	function() {
		AUI().use('aui-toggler', function(A){
			A.all('.portlet').each(function(node){
				node.one('.portlet-topper').placeAfter('<div class="toggle-portlet-btn"></div>');
				var button = node.one('.toggle-portlet-btn');
  				var content = node.one('.portlet-content');

    			var portletToggler = new A.Toggler(
      				{
        				animated: true,
        				container: node,
        				header: button,
        				content: content,
        				transition: {
        				  duration: .5,
        				  easing: 'cubic-bezier'
        				}
      				}
   				);
			});
		});
	}
);