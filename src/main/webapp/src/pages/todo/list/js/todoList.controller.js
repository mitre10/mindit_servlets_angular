(function () {

    'use strict';

    angular.module("todoApp")
        .controller("TodoListCtrl", TodoListCtrl);

    TodoListCtrl.$inject = ['$scope', 'TodoService'];

    function TodoListCtrl($scope, TodoService) {
        TodoService.list()
            .then(function (res) {
                $scope.todos = res.data;
            }, function () {
                $scope.todos = [];
            });

        $scope.todo = {
            id: '',
            name: '',
            owner: '',
            priority: ''
        };

        $scope.todos = [];

        $scope.delete = function(index){
            var id = $scope.todos[index].id;
            TodoService.delete(id)
            .then(function(res){
                TodoService.list()
                    .then(function (res) {
                        $scope.todos = res.data;
                    }, function () {
                        $scope.todos = [];
                    });
            });
        };
    }

})();