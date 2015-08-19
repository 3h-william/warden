        // also include ngRoute for all our routing needs
    // configure our routes
    routeApp.config(function($routeProvider) {
        $routeProvider
            // route for the home page
            .when('/', {
                templateUrl : 'pages/home/home.html'
            })

            // route for the about page
            .when('/about', {
                templateUrl : 'pages/about.html'
            })

           // route for the help page
            .when('/help', {
                templateUrl : 'pages/help.html'
            })

            // route for the about page
            .when('/settings', {
                templateUrl : 'pages/settings/group/groupSettings.html',
                controller  : 'settingsController'
            })

            // route for the contact page
            .when('/contact', {
                templateUrl : 'pages/contact.html',
                controller  : 'contactController'
            })

            .when('/settings/groupSettings', {
                templateUrl : 'pages/settings/group/groupSettings.html'
            })

            .when('/settings/zkSettings', {
                templateUrl : 'pages/settings/zk/zkSettings.html'
            })

            .when('/settings/nodeSettings', {
                templateUrl : 'pages/settings/node/nodeSettings.html'
            });
    });

    routeApp.controller('settingsController', function($scope) {
      //  $scope.message = 'Look! I am an about page.';
    });

    routeApp.controller('contactController', function($scope) {
        $scope.message = 'Contact us! JK. This is just a demo.';
    });


