var AUF = function(){
	return {
		init: function(){
			this.portlet_activite_image_vide();
			this.hide_employees();
		},
		portlet_activite_image_vide: function(){
			$(".news .photo img[src='']").attr("src", "/auf-theme/images/logo.jpg");
		},
		hide_employees: function(){
			var max_employees = 12,
				employee_selector = ".portlet-boundary_aufuserlist_WAR_aufsync_ .box_centre .news";

			if ( $(".portlet-boundary_aufuserlist_WAR_aufsync_").length > 0 ){
				/* hide all other employees higher than max_employees */
				$(employee_selector).slice(max_employees).hide();

				/* add button to show 'em all after the 12th element */
				$(employee_selector + ":eq(11)").after(
					'<div class="clearfix"><input class="aui-button-input auf-show-all-employees" type="button" value="Afficher tous les employés"></div>'
				);

				$("input.auf-show-all-employees").click(function(){
					$(employee_selector).slice(max_employees).fadeIn();
					$(this).fadeOut(function(){
						$(this).parent().remove()
					});
				});
			}
		}
	}
}();

AUI().ready(

	/*
	This function gets loaded when all the HTML, not including the portlets, is
	loaded.
	*/

	function() {
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
		AUF.init();
	}
);