var app = angular.module('emblem', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'bootstrap.angular.validation',
    'smart-table',
    'ui.bootstrap',
    'ngToast'
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

app.config(['ngToastProvider', function(ngToast) {
    ngToast.configure({
          verticalPosition: 'top',
          horizontalPosition: 'center',
          maxNumber: 5,
          combineDuplications: true,
          animation: 'slide'
    });
}]);

app.config(function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/machines.html',
        controller: 'ListMachinesCtrl'
    }).when('/new_machine', {
        templateUrl: 'views/new_machine.html',
        controller: 'CreateNewMachineCtrl'
    }).when('/edit_machine/:serialNumber/:lastUpdated', {
        templateUrl: 'views/edit_machine.html',
        controller: 'EditMachineCtrl'
    }).when('/history/:serialNumber', {
        templateUrl: 'views/history.html',
        controller: 'ListHistoryCtrl'
    }).when('/add_history/:serialNumber', {
        templateUrl: 'views/add_history.html',
        controller: 'CreateHistoryCtrl'
    }).when('/edit_history/:serialNumber/:workOrderNumber/:lastUpdated', {
        templateUrl: 'views/edit_history.html',
        controller: 'EditHistoryCtrl'
    }).when('/test', {
        templateUrl: 'views/test.html',
        controller: 'TestCtrl'
    }).when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.run(function($rootScope, $location, $cookieStore, $http, $timeout, AuthenticationService) {
    $rootScope.location = $location;
    // keep user logged in after page refresh
    $rootScope.globals = $cookieStore.get('globals') || {};
    if ($rootScope.globals.currentUser) {
        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
    }

    $rootScope.$on('$locationChangeStart', function(event, next, current) {
        if ($location.path() === '/login' && $rootScope.globals.currentUser) {
            $location.path('/');
        } else if ($location.path() === '/logout' && $rootScope.globals.currentUser) {
            $('#logging-out').modal({backdrop: 'static', keyboard: false});
            $('#logging-out').modal('toggle');
            AuthenticationService.ClearCredentials(function (response) {
                console.log(response);
                $timeout(function() {
                    $('#logging-out').modal('toggle');
                    $location.path('/login');
                }, 1500);
            });
            // AuthenticationService.ClearCredentials();
            // $location.path('/login');
        } else if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
            // redirect to login page if not logged in
            $location.path('/login');
        }
    });

    // $rootScope.$on("$routeChangeStart", function(event, next, current) {
    //     console.log(next.templateUrl);
    //     if (!(next.templateUrl == "views/login.html")) {
    //         $location.path("/login");
    //     }
    // })
});

app.factory('ErrorFactory', function () {
    return {
        success: function() {
            $('#create-success-alert').css('display', 'block');
            $('#create-success-alert').delay(3000).slideUp(500, function() {
                $('#create-success-alert').css('display', 'none');
            });
        },
        failure: function() {
            $('#create-fail-alert').css('display', 'block');
            $('#create-fail-alert').delay(5000).slideUp(500, function() {
                $('#create-fail-alert').css('display', 'none');
            });
        }
    }
});

app.factory('MachinesFactory', function () {
    var machines = {};
    return {
        getMachines: function () {
            return machines;
        },
        setMachines: function(updated) {
            machines = updated;
        },
    };
});

app.controller('NavCtrl', function($scope, $http, MachinesFactory) {
    $scope.machinesDue = [];

    $http.get('/api/machines/due').then(function(data) {
        $scope.machinesDue = data.data;
    })

    $scope.$watch(function () {
        return MachinesFactory.getMachines();
    }, function (newValue, oldValue) {
        if (newValue !== oldValue) {
            $scope.machinesDue = newValue;
        }
    });
});

app.controller('ListMachinesCtrl', function($scope, $http, $location, $route, $timeout, ngToast, MachinesFactory) {
    $scope.options = [{actual: 'All', internal: '$'}, {actual: 'Serial No.', internal: 'serialNumber'}, {actual: 'State', internal: 'state'},
                      {actual: 'Model', internal: 'model'}, {actual: 'TNC Date', internal: 'tncDate'}, {actual: 'PPM Date', internal: 'ppmDate'},
                      {actual: 'Customer', internal: 'customer'}, {actual: 'Status', internal: 'status'}, {actual: 'Account Type', internal: 'accountType'},
                      {actual: 'Brand', internal: 'brand'}, {actual: 'Person In Charge', internal: 'personInCharge'}, {actual: 'Reported By', internal: 'reportedBy'},
                      {actual: 'Additional Notes', internal: 'additionalNotes'}, {actual: 'Created On', internal: 'dateOfCreation'}, {actual: 'Last Updated On', internal: 'lastUpdated'}];
    $scope.toDel = {};
    $scope.notes = '';
    $scope.searchTypeOption = $scope.options[0].actual;
    $scope.machineFilter = {$: undefined}; // initial non-filtering value
    $scope.machinesPerPage = 50;
    $scope.currentPage = 1;
    $scope.pagesToDisplay = 5;
    $scope.saving = false;
    $scope.machines = [];

    $http.get('/api/machines').then(function(data) {
        var resultWrapper = data.data;
        $scope.machines = resultWrapper.machines;
        // TODO can this be done better? we are fetching for machinesDue TWICE in one reload; here and in NavCtrl
        // is there a way we can only fetch from server (in NavCtrl) if page has been F5-ed?
        $scope.machinesDue = resultWrapper.machinesDue;
    }).catch(function(response) {
        console.error('Oops:: ', response.status, response.data);
        ngToast.create({
            className: 'danger',
            content: '<h3>Oops! Something went wrong. Please try again.</h3>',
            timeout: 5000,
            dismissButton: true,
        });
    })

    $scope.$watch('machinesDue', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            MachinesFactory.setMachines(newValue);
        }
    });

    $scope.setDelAim = function(machine) {
        $scope.toDel = machine;
    }

    $scope.showNotes = function(notes) {
        $scope.notes = notes;
    }

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

    $scope.delMachine = function() {
        $scope.saving = true;
        var index = $scope.machines.indexOf($scope.toDel);
        var serialNumber = $scope.machines[index].serialNumber;
        var lastUpdated = $scope.machines[index].lastUpdatedInLong;

        $http.delete('/api/machines/' + serialNumber + '/' + lastUpdated).then(function(data) {
            $timeout(function(){
                $scope.machines.splice(index, 1);
                var found = $scope.machinesDue.find(x => x.serialNumber === serialNumber);
                if (found) {
                    index = $scope.machinesDue.indexOf(found);
                    $scope.machinesDue.splice(index, 1);
                }

                $('#del').modal('toggle');
                $scope.saving = false;
            }, 1000);
        }).catch(function(response) {
            $timeout(function(){
                console.error('Oops:: ', response.status, response.data);
                if (response.status === 409) {
                    $('#' + serialNumber).removeClass('table-danger');
                    $('#' + serialNumber).addClass('table-danger');
                    ngToast.create({
                        className: 'danger',
                        content: '<h3>This record has recently been updated. Please refresh the page.</h3>',
                        timeout: 5000,
                        dismissButton: true,
                    });
                } else if (response.status === 404) {
                    $('#' + serialNumber).removeClass('table-danger');
                    $('#' + serialNumber).addClass('table-danger');
                    ngToast.create({
                        className: 'danger',
                        content: '<h3>This record no longer exists. Please refresh the page.</h3>',
                        timeout: 5000,
                        dismissButton: true,
                    });
                } else {
                    ngToast.create({
                        className: 'danger',
                        content: '<h3>Oops! Something went wrong. Please try again.</h3>',
                        timeout: 5000,
                        dismissButton: true,
                    });
                }

                $('#del').modal('toggle');
                $scope.saving = false;
            }, 1000);
        })
        return;
    }
});

app.controller('CreateNewMachineCtrl', function($scope, $http, $location, $route, $timeout, MachinesFactory, ErrorFactory) {
    $scope.newMachine = {};
    $scope.saving = false;

    $scope.$watch('machinesDue', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            MachinesFactory.setMachines(newValue);
        }
    });

    $scope.createNewMachine = function() {
        $scope.saving = true;
        $http.post('/api/machines', $scope.newMachine).then(function(data) {
            var resultWrapper = data.data;
            $scope.machinesDue = resultWrapper.machinesDue;
            $timeout(function(){
                $scope.newMachine = {};
                $scope.saving = false;
                ErrorFactory.success();
            }, 1000);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            if (response.status === 409) {
                $scope.errMsg = 'This record already exists.';
            } else {
                $scope.errMsg = 'Something went wrong. Please try again.';
            }
            $timeout(function(){
                $scope.saving = false;
                ErrorFactory.failure();
            }, 1000);
        })
    }
});

app.controller('EditMachineCtrl', function($scope, $http, $location, $route, $routeParams, $timeout, MachinesFactory, ngToast, ErrorFactory) {
    $scope.editMachine = {};
    $scope.saving = false;

    //$http.get('/api/machines/' + $routeParams.serialNumber + '/' + $routeParams.lastUpdated + '/0')
    $http.get('/api/machines/' + $routeParams.serialNumber, { params: { lastUpdated: $routeParams.lastUpdated, hardfailure: '0' } }).then(function(data) {
        var resultWrapper = data.data;
        $scope.editMachine = resultWrapper.machine;
        return resultWrapper.message;
    }).then(function(message) {
        if (message === "RecordWasRecentlyUpdated") {
            ngToast.create({
                className: 'warning',
                content: '<h3>This record has recently been updated. Page has been refreshed.</h3>',
                timeout: 2000,
                dismissButton: true,
            });
        }
    }).catch(function(response) {
        console.error('Oops:: ', response.status, response.data);
        if (response.status === 404) {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! This record does not exist.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        } else {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! Something went wrong. Please try again.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        }
    })

    $scope.$watch('machinesDue', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            MachinesFactory.setMachines(newValue);
        }
    });

    $scope.updateMachine = function() {
        $scope.saving = true;
        $http.put('/api/machines', $scope.editMachine).then(function(data) {
            var resultWrapper = data.data;
            $scope.editMachine = resultWrapper.machine;
            $scope.machinesDue = resultWrapper.machinesDue;
            $timeout(function(){
                $scope.saving = false;
                ErrorFactory.success();
            }, 1000);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            if (response.status === 409) {
                $scope.errMsg = 'This record has recently been updated. Please refresh the page.';
            } else if (response.status === 404) {
                $scope.errMsg = 'This record no longer exists.';
            } else {
                $scope.errMsg = 'Something went wrong. Please try again.';
            }
            $timeout(function(){
                $scope.saving = false;
                ErrorFactory.failure();
            }, 1000);
        })
    }
});

app.controller('ListHistoryCtrl', function($scope, $http, $location, $route, $routeParams, $timeout, ngToast) {
    $scope.options = [{actual: 'All', internal: '$'}, {actual: 'No.', internal: 'workOrderNumber'}, {actual: 'Date', internal: 'workOrderDate'},
                      {actual: 'Type', internal: 'workOrderType'}, {actual: 'Reported By', internal: 'reportedBy'}, {actual: 'Created On', internal: 'dateOfCreation'},
                      {actual: 'Last Updated On', internal: 'lastUpdated'} ];
    $scope.toDel = {};
    $scope.actionTaken = '';
    $scope.searchAttributes = {
        option: $scope.options[0].actual,
        value: undefined
    };
    $scope.historyFilter = {$: undefined}; // initial non-filtering value
    $scope.historyPerPage = 50;
    $scope.currentPage = 1;
    $scope.pagesToDisplay = 5;
    $scope.saving = false;
    $scope.serialNumber = $routeParams.serialNumber;
    $scope.history = [];

    $http.get('/api/history/' + $routeParams.serialNumber).then(function(data) {
        $scope.history = data.data;
    }).catch(function(response) {
        console.error('Oops:: ', response.status, response.data);
        if (response.status === 404) {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! Machine record no longer exists.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        } else {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! Something went wrong. Please try again.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        }
    })

    $scope.setDelAim = function(history) {
        $scope.toDel = history;
    }

    $scope.showActionTaken = function(actionTaken) {
        $scope.actionTaken = actionTaken;
    }

    $scope.setHistorySearchFilter = function() {
        $scope.historyFilter = {};
        for (i = 0; i < $scope.options.length; i++) {
            if ($scope.options[i].actual == $scope.searchAttributes.option) {
                $scope.historyFilter[$scope.options[i].internal || '$'] = $scope.searchAttributes.value;
                break;
            }
        }
    }

    $scope.delHistory = function() {
        $scope.saving = true;
        var index = $scope.history.indexOf($scope.toDel);
        var serialNumber = $scope.history[index].serialNumber;
        var workOrderNumber = $scope.history[index].workOrderNumber;
        var lastUpdated = $scope.history[index].lastUpdatedInLong;

        $http.delete('/api/history/' + serialNumber + '/' + workOrderNumber + '/' + lastUpdated).then(function(data) {
            $timeout(function(){
                $scope.history.splice(index, 1);
                $('#del').modal('toggle');
                $scope.saving = false;
            }, 1000);
        }).catch(function(response) {
            $timeout(function(){
                console.error('Oops:: ', response.status, response.data);
                if (response.status === 409) {
                    $('#' + workOrderNumber).removeClass('table-danger');
                    $('#' + workOrderNumber).addClass('table-danger');
                    ngToast.create({
                        className: 'danger',
                        content: '<h3>This record has recently been updated. Please refresh the page.</h3>',
                        timeout: 5000,
                        dismissButton: true,
                    });
                } else if (response.status === 404) {
                    $('#' + workOrderNumber).removeClass('table-danger');
                    $('#' + workOrderNumber).addClass('table-danger');
                    ngToast.create({
                        className: 'danger',
                        content: '<h3>This record no longer exists. Please refresh the page.</h3>',
                        timeout: 5000,
                        dismissButton: true,
                    });
                } else {
                    ngToast.create({
                        className: 'danger',
                        content: '<h3>Oops! Something went wrong. Please try again.</h3>',
                        timeout: 5000,
                        dismissButton: true,
                    });
                }

                $('#del').modal('toggle');
                $scope.saving = false;
            }, 1000);
        })
        return;
    }
});

app.controller('CreateHistoryCtrl', function($scope, $http, $location, $route, $routeParams, $timeout, ngToast, ErrorFactory) {
    $scope.newHistory = {
        serialNumber: $routeParams.serialNumber
    };
    $scope.saving = false;

    $http.get('/api/machines/' + $routeParams.serialNumber, { params: { hardfailure: '0' } }).then(function(data) {
    }).catch(function(response) {
        console.error('Oops:: ', response.status, response.data);
        if (response.status === 404) {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! Machine record no longer exists.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        } else {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! Something went wrong. Please try again.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        }
    })

    $scope.createNewHistory = function() {
        $scope.saving = true;
        $http.post('/api/history', $scope.newHistory).then(function(data) {
            $timeout(function(){
                $scope.newHistory = {
                    serialNumber: $routeParams.serialNumber
                };
                $scope.saving = false;
                ErrorFactory.success();
            }, 1000);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            if (response.status === 409) {
                $scope.errMsg = 'This record already exists.';
            } else if (response.status === 404) {
                $scope.errMsg = 'Machine record no longer exists.';
            }else {
                $scope.errMsg = 'Something went wrong. Please try again.';
            }
            $timeout(function(){
                $scope.saving = false;
                ErrorFactory.failure();
            }, 1000);
        })
    }
});

app.controller('EditHistoryCtrl', function($scope, $http, $location, $route, $routeParams, $timeout, ngToast, ErrorFactory) {
    $scope.history = {
        serialNumber: $routeParams.serialNumber,
        workOrderNumber: $routeParams.workOrderNumber
    };
    $scope.saving = false;

    $http.get('/api/history/' + $routeParams.serialNumber + '/' + $routeParams.workOrderNumber, { params: { lastUpdated: $routeParams.lastUpdated } }).then(function(data) {
        var resultWrapper = data.data;
        $scope.history = resultWrapper.history;
        return resultWrapper.message;
    }).then(function(message) {
        if (message === "RecordWasRecentlyUpdated") {
            ngToast.create({
                className: 'warning',
                content: '<h3>This record has recently been updated. Page has been refreshed.</h3>',
                timeout: 2000,
                dismissButton: true,
            });
        }
    }).catch(function(response) {
        console.error('Oops:: ', response.status, response.data);
        if (response.status === 404) {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! This record does not exist.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        } else {
            ngToast.create({
                className: 'danger',
                content: '<h3>Oops! Something went wrong. Please try again.</h3>',
                timeout: 5000,
                dismissButton: true,
            });
        }
    })

    $scope.updateHistory = function() {
        $scope.saving = true;
        $http.put('/api/history', $scope.history).then(function(data) {
            $scope.history = data.data;
            $timeout(function(){
                $scope.saving = false;
                ErrorFactory.success();
            }, 1000);
        }).catch(function(response) {
            console.error('Oops:: ', response.status, response.data);
            if (response.status === 409) {
                $scope.errMsg = 'This record has recently been updated. Please refresh the page.';
            } else if (response.status === 404) {
                $scope.errMsg = 'This record no longer exists.';
            } else {
                $scope.errMsg = 'Something went wrong. Please try again.';
            }
            $timeout(function(){
                $scope.saving = false;
                ErrorFactory.failure();
            }, 1000);
        })
    }
});

app.controller('TestCtrl', function($scope) {
    // $("table").resizableColumns();

    var
        nameList = ['PierrePierrePierrePierrePierrePierrePierrePierrePierrePierrePierrePierre', 'Pol', 'Jacques', 'Robert', 'Elisa'],
        familyName = ['Dupont', 'Germain', 'Delcourt', 'bjip', 'Menez'];

    function createRandomItem() {
        var
            firstName = nameList[Math.floor(Math.random() * 4)],
            lastName = familyName[Math.floor(Math.random() * 4)],
            age = Math.floor(Math.random() * 100),
            email = firstName + lastName + '@whatever.com',
            balance = Math.random() * 3000;

        return {
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            balance: balance
        };
    }

    $scope.displayed = [];
    for (var j = 0; j < 50; j++) {
        $scope.displayed.push(createRandomItem());
    }
});

///////////////////////////jQuery stuff ////////////////////////////////////////
