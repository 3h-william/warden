 homePage.controller('homePageCtrl', function($scope, $http) {
     // get allGroupLeftButton
     $http.get(groupGetAllRest)
     .success(function(response) {$scope.groupSettingLists = response.groupList;});

    // get node from group
     $scope.showGroup = function (group) {
        $http.post(groupNodeMappingSettingRest,{'groupID':group.groupID}).success(function(response) {
            $scope.selectedNodeList = response.selectedNodeList;
            $scope.selectGroupName = group.groupName;
        });
     }
 });

