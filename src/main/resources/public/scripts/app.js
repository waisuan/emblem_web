var app = angular.module('emblem', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'bootstrap.angular.validation',
    'smart-table',
    'ui.bootstrap'
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
    }).when('/test', {
        templateUrl: 'views/test.html',
        controller: 'ListMachinesCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('ListMachinesCtrl', function($scope, $http, $location, $route, $timeout) {
    // $scope.searchType = 'Serial No.';
    $scope.toDel = '';
    $scope.notes = '';
    $scope.options = [{actual: 'All', internal: '$'}, {actual: 'Serial No.', internal: 'serialNumber'}, {actual: 'State', internal: 'state'},
                      {actual: 'Model', internal: 'model'}, {actual: 'TNC Date', internal: 'tncDate'}, {actual: 'PPM Date', internal: 'ppmDate'},
                      {actual: 'Customer', internal: 'customer'}, {actual: 'Status', internal: 'status'}, {actual: 'Account Type', internal: 'accountType'},
                      {actual: 'Brand', internal: 'brand'}, {actual: 'Person In Charge', internal: 'personInCharge'}, {actual: 'Reported By', internal: 'reportedBy'},
                      {actual: 'Additional Notes', internal: 'additionalNotes'}, {actual: 'Created On', internal: 'dateOfCreation'}, {actual: 'Last Updated On', internal: 'lastUpdated'}];
    // $scope.headers = $scope.options.slice(1);
    $scope.searchTypeOption = $scope.options[0].actual;
    $scope.machineFilter = {$: undefined}; // initial non-filtering value
    $scope.machinesPerPage = 10;
    $scope.currentPage = 1;
    $scope.pagesToDisplay = 5;

    $http.get('/api/machines').then(function(data) {
        $scope.machines = data.data;
    })

    $scope.setDelAim = function(machine) {
        $scope.toDel = machine;
    }

    $scope.showNotes = function(notes) {
        $scope.notes = notes;
    }

    // $scope.toggleSearchVal = function(searchType) {
    //     $scope.searchType = searchType;
    //     $('#searchValueInput').val('');
    //     if (searchType == 'All') {
    //         $('#searchValueInput').prop('disabled', true);
    //         $('#machineSearchBtn').prop('disabled', false);
    //     } else {
    //         $('#searchValueInput').prop('disabled', false);
    //         $('#machineSearchBtn').prop('disabled', true);
    //     }
    // }

    // $scope.toggleSearchBtn = function() {
    //     var isDisabled = (($scope.searchValue == null || $scope.searchValue == '') && $scope.searchType != 'All') ? true : false;
    //     $('#machineSearchBtn').prop('disabled', isDisabled);
    // }

    $scope.setMachineSearchFilter = function() {
        $scope.machineFilter = {};
        for (i = 0; i < $scope.options.length; i++) {
            if ($scope.options[i].actual == $scope.searchTypeOption) {
                $scope.machineFilter[$scope.options[i].internal || '$'] = $scope.searchValue;
                break;
            }
        }
    }

    $scope.changePage = function() {}

    // $scope.searchMachine = function() {
    //     if ($scope.searchType == 'All') {
    //         $http.get('/api/machines').then(function(data) {
    //             $scope.machines = data.data;
    //         })
    //     } else {
    //         $http.get('/api/machines/' + $scope.searchType + '/' + $scope.searchValue).then(function(data) {
    //             $scope.machines = data.data;
    //         })
    //     }
    // }

    $scope.delMachine = function() {
        var index = $scope.machines.indexOf($scope.toDel);
        if (index === -1) {
            $('#del').modal('toggle');
            $timeout(function() {
                // 1 second delay, might not need this long, but it works.
                $route.reload();
            }, 1000);
        }

        var serialNumber = $scope.machines[index].serialNumber;

        $http.delete('/api/machines/' + serialNumber).then(function(data) {
            $('#del').modal('toggle');
            $scope.machines.splice(index, 1);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
        })
        return;
    }
});

app.controller('CreateNewMachineCtrl', function($scope, $http, $location, $route) {
    $scope.newMachine = {};

    $scope.createNewMachine = function() {
        $http.post('/api/machines', $scope.newMachine).then(function(data) {
            $scope.newMachine = {};
            $('#create-success-alert').css('display', 'block');
            $('#create-success-alert').delay(3000).slideUp(500, function() {
                $('#create-success-alert').css('display', 'none');
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            $('#create-fail-alert').css('display', 'block');
            $('#create-fail-alert').delay(5000).slideUp(500, function() {
                $('#create-fail-alert').css('display', 'none');
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
            $('#create-success-alert').css('display', 'block');
            $('#create-success-alert').delay(3000).slideUp(500, function() {
                $('#create-success-alert').css('display', 'none');
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            $('#create-fail-alert').css('display', 'block');
            $('#create-fail-alert').delay(5000).slideUp(500, function() {
                $('#create-fail-alert').css('display', 'none');
            });
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
            $('#create-success-alert').css('display', 'block');
            $('#create-success-alert').delay(3000).slideUp(500, function() {
                $('#create-success-alert').css('display', 'none');
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            $('#create-fail-alert').css('display', 'block');
            $('#create-fail-alert').delay(5000).slideUp(500, function() {
                $('#create-fail-alert').css('display', 'none');
            });
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
            $('#create-success-alert').css('display', 'block');
            $('#create-success-alert').delay(3000).slideUp(500, function() {
                $('#create-success-alert').css('display', 'none');
            });
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            $('#create-fail-alert').css('display', 'block');
            $('#create-fail-alert').delay(5000).slideUp(500, function() {
                $('#create-fail-alert').css('display', 'none');
            });
        })
    }

    _fetch();
});

///////////////////////////jQuery stuff ////////////////////////////////////////
