var app = angular.module('emblem', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'bootstrap.angular.validation'
]);

app.config(['$locationProvider', function($locationProvider) {
    $locationProvider.hashPrefix('');
}]);

// Taken from http://sagrawal31.github.io/bootstrap-angular-validation/#/
app.config(['bsValidationConfigProvider', function(bsValidationConfigProvider) {
    bsValidationConfigProvider.global.setValidateFieldsOn('submit');
    // We can also customize to enable the multiple events to display form validation state
    //bsValidationConfigProvider.global.setValidateFieldsOn(['submit', 'blur]);

    bsValidationConfigProvider.global.errorMessagePrefix = '<i class="fa fa-exclamation-circle"></i> &nbsp;';
}]);

app.config(function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/machines.html',
        controller: 'ListMachinesCtrl'
    }).when('/new_machine', {
        templateUrl: 'views/new_machine.html',
        controller: 'CreateNewMachineCtrl'
    }).when('/edit_machine/:serialNumber', {
        templateUrl: 'views/edit_machine.html',
        controller: 'EditMachineCtrl'
    }).when('/history/:serialNumber', {
        templateUrl: 'views/history.html',
        controller: 'ListHistoryCtrl'
    }).when('/add_history/:serialNumber', {
        templateUrl: 'views/add_history.html',
        controller: 'CreateHistoryCtrl'
    }).when('/edit_history/:serialNumber/:workOrderNumber', {
        templateUrl: 'views/edit_history.html',
        controller: 'EditHistoryCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('ListMachinesCtrl', function($scope, $http, $location, $route, $timeout) {
    $scope.searchType = 'Serial No.';
    $scope.toDel = '';
    $scope.notes = '';

    $http.get('/api/machines').then(function(data) {
        $scope.machines = data.data;
    })

    $scope.setDelAim = function(serialNumber) {
        $scope.toDel = serialNumber;
    }

    $scope.showNotes = function(notes) {
        $scope.notes = notes;
    }

    $scope.searchMachine = function() {
        $http.get('/api/machines/' + $scope.searchType + '/' + $scope.searchValue, {
            params: {
                "type": $scope.searchType,
                "value": $scope.searchValue
            }
        }).then(function(data) {
            $scope.machines = data.data;
        })
    }

    $scope.delMachine = function() {
        $http.delete('/api/machines/' + $scope.toDel).then(function(data) {
            $('#del').modal('toggle');
            $timeout(function() {
                // 1 second delay, might not need this long, but it works.
                $route.reload();
            }, 1000);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }
});

app.controller('CreateNewMachineCtrl', function($scope, $http, $location, $route) {
    $scope.newMachine = {};

    $scope.createNewMachine = function() {
        $http.post('/api/machines', $scope.newMachine).then(function(data) {
            $scope.newMachine = {};
            $("#create-success-alert").css('display', 'block');
            $("#create-success-alert").delay(3000).slideUp(500, function() {
                $("#create-success-alert").css('display', 'none');
            });
            // $("#create-success-alert").css('visibility', 'visible');
            // $("#create-success-alert").delay(3000).slideUp(500, function(){
            //     $("#create-success-alert").css('visibility', 'hidden');
            //     $("#create-success-alert").slideDown();
            // });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            $("#create-fail-alert").css('display', 'block');
            $("#create-fail-alert").delay(5000).slideUp(500, function() {
                $("#create-fail-alert").css('display', 'none');
            });
        })
    }
});

app.controller('EditMachineCtrl', function($scope, $http, $location, $route, $routeParams) {
    $scope.editMachine = {};

    _fetch = function() {
        $http.get('/api/machines/Serial No./' + $routeParams.serialNumber).then(function(data) {
            $scope.editMachine = data.data[0];
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    };

    $scope.updateMachine = function() {
        $http.put('/api/machines/' + $routeParams.serialNumber, $scope.editMachine).then(function(data) {
            $("#create-success-alert").css('visibility', 'visible');
            $("#create-success-alert").delay(3000).slideUp(500, function() {
                $("#create-success-alert").css('visibility', 'hidden');
                $("#create-success-alert").slideDown();
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }

    _fetch();
});

app.controller('ListHistoryCtrl', function($scope, $http, $location, $route, $routeParams, $timeout) {
    $scope.toDel = {};

    _fetch = function() {
        $http.get('/api/history/' + $routeParams.serialNumber).then(function(data) {
            $scope.history = data.data;
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    };

    $scope.setDelAim = function(serialNumber, workOrderNumber) {
        $scope.toDel['serialNumber'] = serialNumber;
        $scope.toDel['workOrderNumber'] = workOrderNumber;
    }

    $scope.delHistory = function() {
        $http.delete('/api/history/' + $scope.toDel['serialNumber'] + '/' + $scope.toDel['workOrderNumber']).then(function(data) {
            $('#del').modal('toggle');
            $timeout(function() {
                // 1 second delay, might not need this long, but it works.
                $route.reload();
            }, 500);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }

    _fetch();
});

app.controller('CreateHistoryCtrl', function($scope, $http, $location, $route, $routeParams) {
    $scope.newHistory = {
        serialNumber: $routeParams.serialNumber
    };

    $scope.createNewHistory = function() {
        $http.post('/api/history', $scope.newHistory).then(function(data) {
            $scope.newHistory = {
                serialNumber: $routeParams.serialNumber
            };
            $("#create-success-alert").css('visibility', 'visible');
            $("#create-success-alert").delay(3000).slideUp(500, function() {
                $("#create-success-alert").css('visibility', 'hidden');
                $("#create-success-alert").slideDown();
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }
});

app.controller('EditHistoryCtrl', function($scope, $http, $location, $route, $routeParams) {
    $scope.history = {
        serialNumber: $routeParams.serialNumber,
        workOrderNumber: $routeParams.workOrderNumber
    };

    _fetch = function() {
        $http.get('/api/history/' + $routeParams.serialNumber + '/' + $routeParams.workOrderNumber).then(function(data) {
            $scope.history = data.data;
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    };

    $scope.updateHistory = function() {
        $http.put('/api/history', $scope.history).then(function(data) {
            $("#create-success-alert").css('visibility', 'visible');
            $("#create-success-alert").delay(3000).slideUp(500, function() {
                $("#create-success-alert").css('visibility', 'hidden');
                $("#create-success-alert").slideDown();
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
    }

    _fetch();
});

////////////////////////////////////////

function toggleSearchValueInput(mode) {
    $('#searchValueInput').val('');
    $('#searchValueInput').prop('disabled', mode);
    return false;
}
