'use strict';
document.registerElement('page-id');

/* App Module */

angular.module('guestbookApp', [ 'commonModule', 'ngRoute', 'homeModule'])
		.config(['$routeProvider', function($routeProvider) {

					  $routeProvider
					  .when('/home', {templateUrl: 'views/home.html', controller: 'homeController'})
					  .otherwise({redirectTo : '/home'});
					  

		}]).run(function ($rootScope, $routeParams, $route, $location) {

					$rootScope.currentPath = function() {
						return $route.current.originalPath;
					};
					$rootScope.$on( "$routeChangeStart",function(event, next, current){
						//console.info(next.params)
					});

				});