var routeApp = angular.module('disNoticeApp', ['ngRoute','settings',"homePage"]);
var groupSettingApp = angular.module('groupSettingApp',['ui.bootstrap','dialogs.main']);
var zkSettingApp = angular.module('zkSettingApp',['ui.bootstrap','dialogs.main']);
var nodeSettingApp = angular.module('nodeSettingApp',['ui.bootstrap','dialogs.main']);
var settings = angular.module('settings',['groupSettingApp','zkSettingApp','nodeSettingApp']);
var homePage = angular.module('homePage',['ui.bootstrap','dialogs.main']);

var settingsRestRoot = "/rest/settings";
var actionRestRoot = "/rest/action";