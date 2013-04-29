/**
 * Created with IntelliJ IDEA.
 * User: benno
 * Date: 4/21/13
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */


function TopicListCtrl($scope, $resource, $location, $routeParams) {

    var TopicList = $resource('/bail/topics/', {});

    var topics;

    loadList();

    function loadList() {
        topics = TopicList.query(function () {
            $scope.topics = topics
        });
    }

    $scope.onSelect = function(topic) {
        $location.path("/topics/" + topic.id)
    };

    $scope.$on('$routeChangeSuccess', function() {
        if($routeParams.topicId && topics) {
            topics.forEach( function(topic) {
                if(topic.id == $routeParams.topicId) {
                    topic.cssclass = "selected";
                } else {
                    delete topic.cssclass;
                }
            })
        }
    });

    $scope.$on('newEmails', function() {
        loadList();
    })
}
TopicListCtrl.$inject = ['$scope', '$resource', '$location', '$routeParams'];



function TopicCtrl($scope, $resource, $routeParams, $route, $location) {
    // we need to inject 'route', because otherwise the injector wont create the actual instance!
    var Topic = $resource('/bail/topics/showTopic/:topicId', { topicId : '@id' });
    if($routeParams.topicId) {
        var topic = Topic.get({ topicId: $routeParams.topicId }, function() {
            $scope.topic = topic
        })
    }

    $scope.showOriginal = function() {
        $location.path("/messages/" + this.email.id)
    };
}
TopicCtrl.$inject = ['$scope', '$resource', '$routeParams', '$route', '$location'];



function MessageCtrl($scope, $resource, $routeParams, $route, $location) {
    var Message = $resource('/bail/emails/show/:messageId', { messageId : '@id' });
    if($routeParams.messageId) {
        var message = Message.get({ messageId: $routeParams.messageId }, function() {
            $scope.message = message
        })
    }
}
MessageCtrl.$inject = ['$scope', '$resource', '$routeParams', '$route', '$location'];



function MainCtrl($scope, $rootScope, $http, popupService) {

    $scope.checkForEmailsDisabled = false;

    $scope.checkForEmails = function() {
        $scope.checkForEmailsDisabled = true;
        $scope.cssClass = "rotateAnimation";
        $http.post('/bail/topics/downloadEmails').success(function() {
            $scope.checkForEmailsDisabled = false;
            $scope.cssClass = "";
            $rootScope.$broadcast("newEmails");
        })
    };

    $scope.newEmail = function() {
        var button = jQuery("#newEmailButton");
        popupService.show(button);
    };
}
MainCtrl.$inject = ['$scope', '$rootScope', '$http', 'popupService'];




function NewEmailCtrl($scope) {

    var data = {items: [
        {value: "21", name: "Mick Jagger"},
        {value: "43", name: "Johnny Storm"},
        {value: "46", name: "Richard Hatch"},
        {value: "54", name: "Kelly Slater"},
        {value: "55", name: "Rudy Hamilton"},
        {value: "79", name: "Michael Jordan"}
    ]};

    $scope.popup.div.find("#to").autoSuggest(data.items, {
            selectedItemProp: "name",
            searchObjProps: "name",
            minChars: 3,
            startText:"" }
    );

    $scope.popup.div.find("input[name='to']").watermark("Enter recipients here...");

    $scope.popup.div.find("#subject").watermark("Subject...");
}
NewEmailCtrl.$inject = ['$scope'];