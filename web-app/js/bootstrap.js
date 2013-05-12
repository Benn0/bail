

var app = angular.module('app', ['ngResource']);

app.config(function($routeProvider) {
    $routeProvider
        .when('/topics/:topicId', { templateUrl : 'partials/topic.html', controller : 'TopicCtrl' })
        .when('/messages/:messageId', { templateUrl : 'partials/message.html', controller : 'MessageCtrl'});
});


app.factory('popupService', function($http, $rootScope, $compile, $controller) {
    var popupBorderWidth = 3;

    var PopupService = {

        popups: [
            {
                name: "NewEmail",
                templateUrl: "partials/emailPopup.html",
                controller: "NewEmailCtrl"
            }
        ],

        focusedItemCssClass: "focusedItem",

        findSettingsFor: function (popupName) {
            return $(this.popups).each(function() {
                if(this.name == popupName) {
                    return this;
                }
                return null;
            })[0];
        },

        show: function(uiComponent, popupName) {
            var position = uiComponent.offset();
            var width = uiComponent[0].offsetWidth;
            var height = uiComponent[0].offsetHeight;

            var settings = this.findSettingsFor(popupName);
            if( ! settings) {
                throw "No settings found for popup " + popupName;
            }
            var popup = this.createPopup(position.top, position.left, height, width, uiComponent, settings);

            uiComponent.addClass(this.focusedItemCssClass);

            var link = $compile(popup.div.contents());
            var lastScope = this.createScope(popup);
            var locals = {
                $scope: lastScope
            };
            var controller = $controller(settings.controller, locals);
            popup.div.children().data('$ngControllerController', controller);

            link(lastScope);
        },

        createScope: function(popup) {
            var lastScope;
            var that = this;

            lastScope = $rootScope.$new();

            lastScope.popup = popup;
            lastScope.close = function () {
                this.popup.div.remove();
                this.popup.div = null;
                this.popup.itemHighlighter.remove();
                this.popup.itemHighlighter = null;
                this.popup.element.removeClass(that.focusedItemCssClass);
                this.popup.element = null;
                this.$destroy();
            };
            return lastScope;
        },

        createPopup: function(top, left, height, width, uiComponent, settings) {
            var popupTop = top + height - popupBorderWidth;
            var popupLeft = left - popupBorderWidth;

            var popup = {
                div: $(this.template),
                itemHighlighter: $("<div></div>"),
                element: uiComponent
            };

            var body = $("body");
            body.append(popup.div);
            body.append(popup.itemHighlighter);

            popup.div.offset( { top: popupTop, left: popupLeft} );

            popup.itemHighlighter.addClass("popupFocusedItem");
            popup.itemHighlighter.offset( { top: top - popupBorderWidth, left: left - popupBorderWidth } );
            popup.itemHighlighter.width(width);
            popup.itemHighlighter.height(height);

            return popup;
        }
    };

    // TODO: do this dynamically according to the popup settings
    $http.get('partials/emailPopup.html', {}).success(function(data) {
        PopupService.template = data;
    });

    return PopupService;
});