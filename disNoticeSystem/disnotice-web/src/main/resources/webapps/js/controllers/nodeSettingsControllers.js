var nodeAddPageURL = "/pages/settings/node/nodeAdd.html";
var nodeEditPageURL = "/pages/settings/node/nodeEdit.html";
var nodeGetDataPageURL = "/pages/data/nodeData.html";

// rest settings url
var nodeGetAllRest = settingsRestRoot+"/node/getall";
var nodeUpdateRest = settingsRestRoot+"/node/update";
var nodeAddRest = settingsRestRoot+"/node/add";
var nodeDeleteRest = settingsRestRoot+"/node/delete";
// rest actions url
var nodeWatchRest = actionRestRoot + "/node/watch"
var nodeUnWatchRest = actionRestRoot + "/node/unwatch"
var nodeDataRest = actionRestRoot + "/node/getData"
var nodeDataChangeRest = actionRestRoot + "/node/changeData"
var nodeDataDeleteRest = actionRestRoot + "/node/deleteData"
//=========


nodeSettingApp.filter('searchFor', function(){
	return function(arr, searchString){
		if(!searchString){
			return arr;
		}
		var result = [];
		searchString = searchString.toLowerCase();
		// Using the forEach helper method to loop through the array
		angular.forEach(arr, function(item){
			if(item.zkName.toLowerCase().indexOf(searchString) !== -1){
				result.push(item);
			}
		});
		return result;
	};
});

function InstantSearchController($http,$scope){
	  $http.get(zkGetAllRest).success(function(response) {$scope.zkSettingLists = response.zkList;
      	  angular.forEach($scope.zkSettingLists, function(value, key) {
      	       if(value.zkID==$scope.data.zkID){
      	        $scope.selectZKName=value.zkName;
      	        $scope.selectZKID=value.zkID;
      	       }
            })
	  });
}

nodeSettingApp.controller('getNodeSettingsCtrl',nodeGetAll );

function nodeGetAll($scope, $http) {
     $http.get(nodeGetAllRest).success(function(response) {$scope.nodeSettingLists = response.nodeList;});
}

nodeSettingApp.controller('getDataNodeActionCtrl', function($http,$route,$scope,$rootScope,$timeout,dialogs) {
    getNodeData($scope,$http);
    $scope.launch = function(which,nodeID,key,value){
            switch(which){
                  case 'dataEdit':
                                $scope.newnode = {
                                    nodeName: 'input new node name',
                                    nodePath: '',
                                };
                                if(key==null||key==''||value==null||value==''){
                                    dialogs.notify("Validate","key or value is null");
                                }else{
                                var dlg = dialogs.confirm("data update confirm","key="+key+","+"value="+value);
                                dlg.result.then(function(btn){  //yes
                                   changeNodeDataValue($route,dialogs,$scope,$http,$scope.nodeID,key,value);
                                },function(btn){ // no

                                });
                                }
                                break;
                      case 'dataDelete':
                                var dlg = dialogs.confirm("data delete confirm","key="+key);
                                    dlg.result.then(function(btn){  //yes
                                        deleteNodeDataValue($route,dialogs,$scope,$http,$scope.nodeID,key);
                                    },function(btn){ // no

                                    });
                                break;

            }
        }; // end launch
});

function updateNodeSetting($route,dialogs,$scope,$http,data){
         $http.post(nodeUpdateRest,{'nodeID':data.nodeID,"nodeName":data.nodeName,"nodePath":data.nodePath,"nodeType":data.nodeType,"zkID":data.zkID,"extraInfoStr":data.extraInfoStr})
         .success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });
}

function addNodeSetting($route,dialogs,$scope,$http,nodeName,nodePath,zkID){
     $http.post(nodeAddRest,{"nodeName":nodeName,"nodePath":nodePath,"zkID":zkID}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });
}

function deleteNodeSetting($route,dialogs,$scope,$http,nodeID){
     $http.post(nodeDeleteRest,{"nodeID":nodeID}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });
}

function watchNodeSetting($route,dialogs,$scope,$http,nodeID){
     $http.post(nodeWatchRest,{"nodeID":nodeID}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });
}

function unWatchNodeSetting($route,dialogs,$scope,$http,nodeID){
     $http.post(nodeUnWatchRest,{"nodeID":nodeID}).success(function(response) {
         dialogs.notify("Response MSG",response.msg);
         $scope.saveReturnMsg = response.msg;
         $route.reload();
	 });
}

function changeNodeDataValue($route,dialogs,$scope,$http,nodeID,key,value){
    $http.post(nodeDataChangeRest,{"nodeID":nodeID,"key":key,"value":value}).success(function(response) {
        dialogs.notify("Response MSG",response.msg);
        $scope.saveReturnMsg = response.msg;
        getNodeData($scope,$http);
	});
}

function deleteNodeDataValue($route,dialogs,$scope,$http,nodeID,key){
    $http.post(nodeDataDeleteRest,{"nodeID":nodeID,"key":key}).success(function(response) {
        dialogs.notify("Response MSG",response.msg);
        $scope.saveReturnMsg = response.msg;
        getNodeData($scope,$http);
	});
}


function getNodeData($scope,$http){
       $http.post(nodeDataRest,{"nodeID":$scope.searchNode.nodeID}).success(function(response) {
           $scope.nodeID=response.nodeID;
           $scope.datamap=response.datamap;
   });
}

nodeSettingApp.controller('nodeDialogService',function($http,$route,$scope,$rootScope,$timeout,dialogs){
		//-- Methods --//
		$scope.launch = function(which,data){
			switch(which){
                case 'nodeEdit':
                    var dlg = dialogs.create(nodeEditPageURL,'nodeEditCtrl',$scope.node,{size:'lg'});
                    break;
                case 'nodeDelete':
                    var dlg = dialogs.confirm("delete confirm","nodeID="+$scope.node.nodeID+",nodeName="+$scope.node.nodeName);
                    dlg.result.then(function(btn){  //yes
                       deleteNodeSetting($route,dialogs,$scope,$http,$scope.node.nodeID);
                    },function(btn){ // no
                    });
                    break;
               case 'nodeWatch':
                    var dlg = dialogs.confirm("watch confirm","nodeID="+$scope.node.nodeID+",nodeName="+$scope.node.nodeName);
                    dlg.result.then(function(btn){  //yes
                       watchNodeSetting($route,dialogs,$scope,$http,$scope.node.nodeID);
                    },function(btn){ // no
                    });
                    break;
                case 'nodeUnWatch':
                    var dlg = dialogs.confirm("no watch confirm","nodeID="+$scope.node.nodeID+",nodeName="+$scope.node.nodeName);
                    dlg.result.then(function(btn){  //yes
                       unWatchNodeSetting($route,dialogs,$scope,$http,$scope.node.nodeID);
                    },function(btn){ // no
                    });
                    break;
                 case 'nodeAdd':
                        $scope.newnode = {
                 			nodeName: 'input new node name',
                 			nodePath: '',
                 		};
                  var dlg = dialogs.create(nodeAddPageURL,'nodeAddCtrl',$scope.newnode,{size:'lg'});
                  break;
                  case 'getData':
                        var dlg = dialogs.create(nodeGetDataPageURL,'nodeDataCtrl',$scope.node,{size:'lg'});
                        break;

			}
		}; // end launch
	})
	.controller('nodeEditCtrl',function(dialogs,$route,$scope,$http,$modalInstance,data){
	    $scope.data = data;
		//-- Methods --//
		$scope.cancel = function(){
			$modalInstance.dismiss('Canceled');
		}; // end cancel
		$scope.save = function(){
		    updateNodeSetting($route,dialogs,$scope,$http,data)
			$modalInstance.close();
		}; // end save
	})
    .controller('nodeAddCtrl',function(dialogs,$route,$scope,$http,$modalInstance,data){
        $scope.data = data;
        //-- Methods --//
        $scope.cancel = function(){
            $modalInstance.dismiss('Canceled');
        }; // end cancel
        $scope.save = function(){
           addNodeSetting($route,dialogs,$scope,$http,data.nodeName,data.nodePath,data.zkID)
            $modalInstance.close();
        }; // end save
    })
    .controller('nodeDataCtrl',function(dialogs,$route,$scope,$http,$modalInstance,data){
            $scope.searchNode = data;
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
