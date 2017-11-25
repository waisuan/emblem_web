/**
 * Created by shekhargulati on 10/06/14.
 */

var app = angular.module('emblem', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);

app.config(['$locationProvider', function($locationProvider) {
  $locationProvider.hashPrefix('');
  // $locationProvider.html5Mode(true);
}]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/machines.html',
        controller: 'ListMachinesCtrl'
    }).when('/new_machine', {
        templateUrl: 'views/new_machine.html',
        controller: 'CreateNewMachineCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

// app.controller('SearchController', function ($scope, $http, $location) {
//
// });

app.controller('ListMachinesCtrl', function ($scope, $http) {
    $http.get('/api/machines').then(function (data) {
        console.log(data);
        console.log(data.data.length);
        $scope.machines = data.data;
        console.log($scope.machines);
        // console.log($scope.machines[1].customer);
    })

    $scope.searchType='Serial No.';

    $scope.searchMachine = function () {
      console.log($scope.searchValue);
      console.log($scope.searchType);
      $http.get('/api/machines/'+ $scope.searchType+'/'+ $scope.searchValue, {params:{"type": $scope.searchType, "value": $scope.searchValue}}).then(function (data) {
        console.log(data);
        $scope.machines = data.data;
      })
    }
});

app.controller('CreateNewMachineCtrl', function ($scope, $http, $location, $route) {
    $scope.newMachine = {
    };
    $scope.createNewMachine = function () {
        console.log('createNewMachine');
        $http.post('/api/machines', $scope.newMachine).then(function (data) {
            console.log(data);
            $route.reload();
            //$location.path('/new_machine');
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }
});

function toggleSearchValueInput (mode) {
  $('#searchValueInput').prop('disabled', mode);
  return false;
}
