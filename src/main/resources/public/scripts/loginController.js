(function (){

  var app = angular.module("emblem");

  var LoginController = function($scope, $rootScope, $location, $timeout, AuthenticationService) {

      // Reset the login status before we start
      if (!AuthenticationService.IsLoggedIn()) {
          AuthenticationService.Init();
      }

      $scope.login = function (username, password) {
          $scope.dataLoading = true;
          AuthenticationService.Login(username, password, function(response) {
              if(response.success) {
                  AuthenticationService.SetCredentials(username, password);
                  $timeout(function(){
                      $location.path('/');
                  }, 1000);
              } else {
                  $timeout(function(){
                      $scope.error = response.message;
                      $scope.dataLoading = false;
                  }, 1000);
              }
          });
      };
  };

  app.controller("LoginCtrl", LoginController);

})();
