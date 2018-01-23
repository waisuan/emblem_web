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
    }).when('/edit_machine/:id', {
        templateUrl: 'views/edit_machine.html',
        controller: 'EditMachineCtrl'
    }).when('/history/:id', {
        templateUrl: 'views/history.html',
        controller: 'ListHistoryCtrl'
    }).when('/add_history/:id', {
        templateUrl: 'views/add_history.html',
        controller: 'CreateHistoryCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

// app.controller('SearchController', function ($scope, $http, $location) {
//
// });

app.controller('ListMachinesCtrl', function ($scope, $http, $location, $route, $timeout) {
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

    $scope.toDel = '';

    $scope.delMachine = function () {
        console.log('delMachine');
        console.log($scope.toDel);
        $http.delete('/api/machines/' + $scope.toDel).then(function (data) {
            console.log(data);
            $('#del').modal('toggle');
            $timeout(function(){
               // 1 second delay, might not need this long, but it works.
               $route.reload();
             }, 1000);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }

    $scope.setDelAim = function (msg) {
        $scope.toDel = msg;
        console.log('gonna del...' + msg);
    }

    $scope.showModal = false;

    $scope.showNotes = function () {
        console.log('showNotes');
        $scope.showModal = !$scope.showModal;
    }
});

app.controller('CreateNewMachineCtrl', function ($scope, $http, $location, $route) {
    $scope.newMachine = {
    };
    $scope.createNewMachine = function () {
        console.log('createNewMachine');
        $http.post('/api/machines', $scope.newMachine).then(function (data) {
            console.log(data);
            $scope.newMachine = {};
            $("#create-success-alert").css('visibility', 'visible');
            //$route.reload();
            //$location.path('/new_machine');
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }
});

app.controller('EditMachineCtrl', function ($scope, $http, $location, $route, $routeParams) {
    console.log($routeParams.id);
    $scope.editMachine = {};
    _fetch = function () {
        //, {params:{"type": "serialNumber", "value": $routeParams.id}}
        $http.get('/api/machines/Serial No./' + $routeParams.id).then(function (data) {
          $scope.editMachine = data.data[0];
          console.log($scope.editMachine);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    };
    _fetch();
    $scope.updateMachine = function () {
        console.log('editMachine');
        $http.put('/api/machines/' + $routeParams.id, $scope.editMachine).then(function (data) {
            console.log(data);
            //$route.reload();
            $location.path('/');
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }
});

app.controller('ListHistoryCtrl', function ($scope, $http, $location, $route, $routeParams) {
    console.log($routeParams.id);
    _fetch = function () {
        //, {params:{"type": "serialNumber", "value": $routeParams.id}}
        $http.get('/api/history/' + $routeParams.id).then(function (data) {
          $scope.history = data.data;
          console.log($scope.history);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    };
    _fetch();
    // $scope.updateMachine = function () {
    //     console.log('editMachine');
    //     $http.put('/api/machines/' + $routeParams.id, $scope.editMachine).then(function (data) {
    //         console.log(data);
    //         //$route.reload();
    //         $location.path('/');
    //     }).catch(function(response) {
    //         console.error('Oops:: ', response.status, response.data);
    //     })
    // }
});

app.controller('CreateHistoryCtrl', function ($scope, $http, $location, $route, $routeParams) {
    console.log($routeParams.id);
    $scope.newHistory = {
        serialNumber : $routeParams.id
    };
    $scope.createNewHistory = function () {
        console.log('createNewHistory');
        $http.post('/api/history', $scope.newHistory).then(function (data) {
            console.log(data);
            $scope.newHistory = {
                serialNumber : $routeParams.id
            };
            $("#create-success-alert").css('visibility', 'visible');
            //$route.reload();
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
