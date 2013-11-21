var AUF = function(){
	return {
		init: function(){
			this.portlet_activite_image_vide();
		},
		portlet_activite_image_vide: function(){
			$(".news .photo img[src='']").attr("src", "/auf-theme/images/logo.jpg");
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