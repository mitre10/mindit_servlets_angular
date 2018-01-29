(function () {

    'use strict';

    angular.module("todoApp")
        .service("TodoService", TodoService);

    TodoService.$inject = ['$http'];

    function TodoService($http) {
        return {
            list: function () {
                return $http.get('todos');
            },

            get: function (id) {
                var requestConfig = {
                    params: {id: id}
                };
                return $http.get('todos', requestConfig);
            },

            delete: function (id) {
                var requestConfig = {
                    params: {id: id}
                };
                return $http.delete('todos', requestConfig);
            },


        };
    }

})();