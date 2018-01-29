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

        $scope.displayEditView = false;
        $scope.idToEdit;
        $scope.showEditButton = true;
        $scope.todos = [];

        $scope.clickEdit = function(index){
            $scope.displayEditView = true;
            $scope.showEditButton=false;
            $scope.idToEdit = index;
            $scope.todo = $scope.todos[index];
        };

        $scope.edit = function(){
            $scope.showEditButton=true;
            var todoToSave = {};
            TodoService.put($scope.todo)
                .then(function(res){
                    TodoService.list()
                        .then(function (res) {
                            $scope.todos = res.data;
                        }, function () {
                            $scope.todos = [];
                        });
                    $scope.displayEditView = false;
                });
        };

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