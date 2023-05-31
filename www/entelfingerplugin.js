// Empty constructor
function EntelFingerPlugin() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
EntelFingerPlugin.prototype.getwsq = function(righFingerCode, leftFingerCode, liveness, type,successCallback, errorCallback) {
  var options = {};
  options.righFingerCode = righFingerCode;
  options.leftFingerCode = leftFingerCode;
  options.liveness = liveness;
  options.type = type;
  cordova.exec(successCallback, errorCallback, 'EntelFingerPlugin', 'getwsq', [options]);
}

// Installation constructor that binds EntelFingerPlugin to window
EntelFingerPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.EntelFingerPlugin = new EntelFingerPlugin();
  return window.plugins.EntelFingerPlugin;
};
cordova.addConstructor(EntelFingerPlugin.install);
