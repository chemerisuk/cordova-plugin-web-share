var exec = require("cordova/exec");
var PLUGIN_NAME = "ShareDialog";
var DEFAULT_IOS_EXCLUSIONS = [
        "com.apple.UIKit.activity.AddToReadingList",
        "com.apple.UIKit.activity.AirDrop"
    ];

module.exports = function(options) {
    return new Promise(function(resolve, reject) {
        if (!options || !options.text && !options.url) {
            reject(new TypeError("Nothing to share"));
        } else {
            if (!options.iosExcludedActivities) {
                options.iosExcludedActivities = DEFAULT_IOS_EXCLUSIONS;
            }

            exec(resolve, reject, PLUGIN_NAME, "share", [options]);
        }
    });
};
