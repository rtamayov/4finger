module.exports = function (ctx) {

    var fs = ctx.requireCordovaModule("fs");
    var path = ctx.requireCordovaModule("path");
    var xcode = ctx.requireCordovaModule("xcode");
    var deferral = ctx.requireCordovaModule('q').defer();

     /**
     * Recursively search for file with the tiven filter starting on startPath
     */
    function searchRecursiveFromPath(startPath, filter, rec, multiple) {
        if (!fs.existsSync(startPath)) {
            console.log("no dir ", startPath);
            return;
        }

        var files = fs.readdirSync(startPath);
        var resultFiles = []
        for (var i = 0; i < files.length; i++) {
            var filename = path.join(startPath, files[i]);
            var stat = fs.lstatSync(filename);
            if (stat.isDirectory() && rec) {
                fromDir(filename, filter); //recurse
            }

            if (filename.indexOf(filter) >= 0) {
                if (multiple) {
                    resultFiles.push(filename);
                } else {
                    return filename;
                }
            }
        }
        if (multiple) {
            return resultFiles;
        }
    }

    var xcodeProjPath = searchRecursiveFromPath('platforms/ios', '.xcodeproj', false);
    var projectPath = xcodeProjPath + '/project.pbxproj';
    console.log("Found", projectPath);

    var proj = xcode.project(projectPath);
    var mXCBuildConfigurationSections = proj.parseSync().pbxXCBuildConfigurationSection()
    
    //create the new BuildConfig
    var newBuildConfig = {}
    for(prop in mXCBuildConfigurationSections) {
        var value = mXCBuildConfigurationSections[prop];
        if(!(typeof value === 'string')) {
            value.buildSettings['ONLY_ACTIVE_ARCH'] = "YES"
            value.buildSettings['ARCHS'] = "arm64" //Change to what is needed
            value.buildSettings['VALID_ARCHS'] = "arm64" //Change to what is needed
            value.buildSettings['ALWAYS_EMBED_SWIFT_STANDARD_LIBRARIES'] = "YES" //Not needed but an extra
            value.buildSettings['EMBEDDED_CONTENT_CONTAINS_SWIFT'] = "YES" //Not needed but an extra
        }
        newBuildConfig[prop] = value;
    }

    //Change BuildConfigs
    proj.hash.project.objects['XCBuildConfiguration'] = newBuildConfig

    fs.writeFile(proj.filepath, proj.writeSync(), 'utf8', function (err) {
        if (err) {
            deferral.reject(err);
            return;
        }
        console.log("finished writing xcodeproj");
        deferral.resolve();
    });

    debugger
    return deferral.promise;
};