angular.module('alertsDirective', [])
.directive('alertsBox', function () {
	  return {
		    templateUrl: 'templates/directives/alerts.html',
		    restrict: 'E', // it kicks in on <alerts-box> elements
		    transclude: true,//replaces <alerts-box> tag with template content
		    replace: false,
		    scope: {
		      alerts: '='
		    },
		    link: function (scope, element, attrs) {
    			scope.dismissAlert = function(index){
    				scope.alerts.splice(index, 1);
    			};
		    }
		  };
		});