

var app = angular.module('app', ['ngResource']);

app.config(function($routeProvider) {
    $routeProvider
        .when('/topics/:topicId', { templateUrl : 'partials/topic.html', controller : 'TopicCtrl' })
        .when('/messages/:messageId', { templateUrl : 'partials/message.html', controller : 'MessageCtrl'});
});


app.factory('popupService', function($http) {
    var popupBorderWidth = 3;

    var PopupService = {

        show: function(uiComponent) {

            var position = uiComponent.offset();
            var width = uiComponent[0].offsetWidth;
            var height = uiComponent[0].offsetHeight;

            var popup = this.createPopup(position.top, position.left, height, width);

            uiComponent.addClass("focusedItem");
        },

        createPopup: function(top, left, height, width) {
            var popupTop = top + height - popupBorderWidth;
            var popupLeft = left - popupBorderWidth;

            var popup = {
                div: jQuery(this.template),
                itemHighlighter: jQuery("<div></div>"),

                close: function() {
                    this.div.remove();
                    this.itemHighlighter.remove();
                }
            };

            var body = jQuery("body");
            body.append(popup.div);
            body.append(popup.itemHighlighter);

            popup.div.offset( { top: popupTop, left: popupLeft} );

            popup.itemHighlighter.addClass("popupFocusedItem");
            popup.itemHighlighter.offset( { top: top - popupBorderWidth, left: left - popupBorderWidth } );
            popup.itemHighlighter.width(width);
            popup.itemHighlighter.height(height);
        }
    };

    $http.get('partials/emailPopup.html', {}).success(function(data) {
        PopupService.template = data;
    });

    return PopupService;
});