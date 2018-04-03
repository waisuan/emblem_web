(function() {

    var AuthenticationService = function($http, $cookieStore, $rootScope, $timeout) {

        var response = {
            success: false
        };
        //  Function defined for when the user login is initiate
        var Login = function(username, password, callback) {
            $http.get('/api/login/' + username + '/' + password).then(function(data) {
                response.success = true;
                callback(response);
            }).catch(function(response) {
                console.error('Oops:: ', response.status, response.data);
                if (response.status === 409) {
                    response.message = 'User is already logged in.';
                } else if (response.status === 404) {
                    response.message = 'Username/password is incorrect.';
                } else {
                    response.message = 'Failed to login.';
                }
                callback(response);
            });
        };

        //  Sets the cookie and the state to logged in
        var SetCredentials = function(username, password) {
            <!-- https://github.com/brix/crypto-js -->
            var authdata = CryptoJS.AES.encrypt(username + ':' + password, "emblem").toString();
            $rootScope.globals = {
                currentUser: {
                    username: username,
                    authdata: authdata
                }
            };

            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
            $cookieStore.put('globals', $rootScope.globals);
        };

        //  Clears the cookie and the state for the application to recognise a logged out state
        var ClearCredentials = function(callback) {
            if ($rootScope.globals.currentUser) {
                $http.get('/api/logout/' + $rootScope.globals.currentUser.username).then(function(data) {
                    Init();
                    callback({success: true});
                }).catch(function(response) {
                    console.error('Oops:: ', response.status, response.data);
                    // TODO should we do something here? toast?
                    // if (response.status === 404) {
                    //     response.message = 'User no longer exists.';
                    // } else {
                    //     response.message = 'Failed to logout properly.';
                    // }
                    Init();
                    callback({success: false});
                });
            }
        };

        var Init = function() {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic ';
        };

        var IsLoggedIn = function() {
            $rootScope.globals = $cookieStore.get('globals') || {};
            if ($rootScope.globals.currentUser) {
                $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
            }
            return $rootScope.globals.currentUser;
        };


        return {
            Init: Init,
            Login: Login,
            SetCredentials: SetCredentials,
            ClearCredentials: ClearCredentials,
            IsLoggedIn: IsLoggedIn
        };

    }

    //  Register the service with the application
    var module = angular.module("emblem");
    module.factory("AuthenticationService", AuthenticationService)

}());
