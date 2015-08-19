 var groupAddPageURL = "/pages/settings/group/groupAdd.html";
 var groupEditPageURL = "/pages/settings/group/groupEdit.html";

// rest url
var groupGetAllRest = settingsRestRoot+"/group/getall";
var groupUpdateRest = settingsRestRoot+"/group/update";
var groupAddRest = settingsRestRoot+"/group/add";
var groupDeleteRest = settingsRestRoot+"/group/delete";
var groupNodeMappingSettingRest = settingsRestRoot+"/group/getGroupNodeMapping";

 groupSettingApp.controller('getGroupSettingsCtrl', function($scope, $http) {
     $http.get(groupGetAllRest)
     .success(function(response) {$scope.groupSettingLists = response.groupList;});
 });

function updateGroupSetting($route,dialogs,$scope,$http,groupID,groupName,nodeSelection){
	 $http.post(groupUpdateRest,{'groupID':groupID,"groupName":groupName,"nodes":nodeSelection}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });

}
function addGroupSetting($route,dialogs,$scope,$http,groupName){
     $http.post(groupAddRest,{"groupName":groupName}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });

}
function deleteGroupSetting($route,dialogs,$scope,$http,groupID){
     $http.post(groupDeleteRest,{"groupID":groupID}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });
	 ;
}

groupSettingApp.controller('groupDialogService',function($http,$route,$scope,$rootScope,$timeout,dialogs){
		//-- Methods --//
		$scope.launch = function(which,data){
			switch(which){
                case 'groupEdit':
                    var dlg = dialogs.create(groupEditPageURL,'groupEditCtrl',$scope.group,{size:'lg'});
                    break;
                case 'groupDelete':
                    var dlg = dialogs.confirm("delete confirm","groupID="+$scope.group.groupID+",groupName="+$scope.group.groupName);
                    dlg.result.then(function(btn){  //yes
                       deleteGroupSetting($route,dialogs,$scope,$http,$scope.group.groupID);
                    },function(btn){ // no
                    });
                    break;
                 case 'groupAdd':
                        $scope.newgroup = {
                 			groupName: 'input new group name'
                 		};
                    var dlg = dialogs.create(groupAddPageURL,'groupAddCtrl',$scope.newgroup,{size:'lg'});
                    break;
			}
		}; // end launch
	})
	.controller('groupEditCtrl',function(dialogs,$route,$scope,$http,$modalInstance,data){
        $scope.data = data;

        $http.post(groupNodeMappingSettingRest,{'groupID':$scope.data.groupID}).success(function(response) {
            $scope.unSelectedNodeList = response.unSelectedNodeList;
            $scope.selectedNodeList = response.selectedNodeList;

            $scope.chooseUnSelectedNodeList = function chooseUnSelectedNodeList() {
                return filterFilter($scope.unSelectedNodeList, { selected: true });
            };

            $scope.chooseSelectedNodeList = function chooseSelectedNodeList() {
                 return filterFilter($scope.selectedNodeList, { selected: true });
            };
            // watch unSelectedNodeList for changes
             $scope.$watch('unSelectedNodeList|filter:{selected:true}', function (nv) {
             $scope.selection1 = nv.map(function (unSelectedNode) {
                 return unSelectedNode.nodeID;
               });
             }, true);
             $scope.$watch('selectedNodeList|filter:{selected:true}', function (nv) {
             $scope.selection2 = nv.map(function (selectedNode) {
                 return selectedNode.nodeID;
             });
             }, true);
             // init selected data
             angular.forEach($scope.selectedNodeList,function(data){
                data.selected=true;
             });

            });

        //-- Methods --//
        $scope.cancel = function(){
        $modalInstance.dismiss('Canceled');
        }; // end cancel
        $scope.save = function(){
          var allSelected=$scope.selection1.concat($scope.selection2);
          updateGroupSetting($route,dialogs,$scope,$http,data.groupID,data.groupName,allSelected)
          $modalInstance.close();
        }; // end save
	})

    .controller('groupAddCtrl',function(dialogs,$route,$scope,$http,$modalInstance,data){
        $scope.data = data;
        //-- Methods --//
        $scope.cancel = function(){
            $modalInstance.dismiss('Canceled');
        }; // end cancel
        $scope.save = function(){
            addGroupSetting($route,dialogs,$scope,$http,data.groupName)
            $modalInstance.close();
        }; // end save
    })
	.config(function($translateProvider){
		/**
		 * These are the defaults set by the dialogs.main module when Angular-Translate
		 * is not loaded.  You can reset them in your module's configuration
		 * function by using $translateProvider.  If you want to use these when
		 * Angular-Translate is used and loaded, then you need to also load
		 * dialogs-default-translations.js or include them where you are setting
		 * translations in your module.  These were separated out when Angular-Translate
		 * is loaded so as not to clobber translation set elsewhere in one's
		 * application.

		$translateProvider.translations('en-US',{
	        DIALOGS_ERROR: "Error",
	        DIALOGS_ERROR_MSG: "An unknown error has occurred.",
	        DIALOGS_CLOSE: "Close",
	        DIALOGS_PLEASE_WAIT: "Please Wait",
	        DIALOGS_PLEASE_WAIT_ELIPS: "Please Wait...",
	        DIALOGS_PLEASE_WAIT_MSG: "Waiting on operation to complete.",
	        DIALOGS_PERCENT_COMPLETE: "% Complete",
	        DIALOGS_NOTIFICATION: "Notification",
	        DIALOGS_NOTIFICATION_MSG: "Unknown application notification.",
	        DIALOGS_CONFIRMATION: "Confirmation",
	        DIALOGS_CONFIRMATION_MSG: "Confirmation required.",
	        DIALOGS_OK: "OK",
	        DIALOGS_YES: "Yes",
	        DIALOGS_NO: "No"
        });
		*/
	})
	.run(function($templateCache){
	//	$templateCache.put('/dialogs/custom.html','<div class="modal-header"><h4 class="modal-title"><span class="glyphicon glyphicon-star"></span> User\'s Name</h4></div><div class="modal-body"><ng-form name="nameDialog" novalidate role="form"><div class="form-group input-group-lg" ng-class="{true: \'has-error\'}[nameDialog.username.$dirty && nameDialog.username.$invalid]"><label class="control-label" for="course">Name:</label><input type="text" class="form-control" name="username" id="username" ng-model="user.name" ng-keyup="hitEnter($event)" required><span class="help-block">Enter your full name, first &amp; last.</span></div></ng-form></div><div class="modal-footer"><button type="button" class="btn btn-default" ng-click="cancel()">Cancel</button><button type="button" class="btn btn-primary" ng-click="save()" ng-disabled="(nameDialog.$dirty && nameDialog.$invalid) || nameDialog.$pristine">Save</button></div>');
  	//	$templateCache.put('/dialogs/custom2.html','<div class="modal-header"><h4 class="modal-title"><span class="glyphicon glyphicon-star"></span> Custom Dialog 2</h4></div><div class="modal-body"><label class="control-label" for="customValue">Custom Value:</label><input type="text" class="form-control" id="customValue" ng-model="data.val" ng-keyup="hitEnter($event)"><span class="help-block">Using "dialogsProvider.useCopy(false)" in your applications config function will allow data passed to a custom dialog to retain its two-way binding with the scope of the calling controller.</span></div><div class="modal-footer"><button type="button" class="btn btn-default" ng-click="done()">Done</button></div>');
	});
