var app = angular.module('myApp', []);

app.controller('myCtrl', function ($scope, $http) {

    $scope.rates = [];
    $scope.currencies = [];
    $scope.convertValue;

    var url = "allRates";

    //Спрашиваем AjaX
    $http.get(url).then(function(response) {
        //Успех
        $scope.rates = response.data;
    }, function(response) {
        //Ошибка
        $scope.rates = [];
        alert('Сетевая ошибка');
    });



    $scope.updateRates = function () {
        if($scope.course !== null && $scope.course !== undefined) {

            var url = "allCurrencies";

            $http.get(url).then(function(response) {
                $scope.currencies = response.data;
                $scope.check();
            }, function(response) {
                $scope.currencies = [];
                $scope.check();
                alert('Сетевая ошибка');
            });
        }else{
            $scope.currencies = [];
            $scope.check();
        }
    }

    $scope.check = function () {
        if($scope.count>=0 && $scope.count!==null && $scope.count!==undefined &&
                $scope.from!==null && $scope.from!==undefined &&
                $scope.to!==null && $scope.to!==undefined &&
                $scope.course !== null && $scope.course !== undefined) {

            var params = {
                "date" : $scope.course,
                "from" : $scope.from,
                "to" : $scope.to,
                "count" : $scope.count
             };
            url = "Convert?"+$.param(params);

            $http.get(url).then(function(response) {
            $scope.convertValue = response.data;
            }, function(response) {
            $scope.convertValue =-1;
            alert('Сетевая ошибка');
            });
        } else {
            $scope.convertValue =-1;
        }
    }



});