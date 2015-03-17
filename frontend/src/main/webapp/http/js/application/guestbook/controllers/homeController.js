angular.module('homeModule',[ 'commonModule']).controller('homeController', function($scope, $rootScope, $location, $http) {

    function guests($scope, $http) {
        $http.get('/server/guests').success(function(data) {
            $scope.signed = data;
        });
    }

    $scope.sign = function(){

        var dataObj = {
            firstname : $scope.guest.firstname,
            lastname : $scope.guest.lastname
        };
        var res = $http.post('/server/guest/sign', dataObj);
        res.success(function(data, status, headers, config) {
            guests($scope, $http);
        });
        res.error(function(status, headers, config) {
            alert( "failed ");
        });
        $scope.guest.firstname='';
        $scope.guest.lastname='';
    }

    guests($scope, $http);

});
