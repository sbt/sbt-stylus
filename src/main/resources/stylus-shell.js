/*global process, require */

var requireIfExists = require('node-require-fallback'),
    fs = require("fs"),
    jst = requireIfExists("jstranspiler/1.0.1", "jstranspiler"), // sync with build.sbt
    nodefn = requireIfExists("when/3.5.2-3/node", "when/node"), // sync with build.sbt
    mkdirp = requireIfExists("mkdirp/0.5.0", "mkdirp"), // sync with build.sbt
    path = require("path"),
    stylus = requireIfExists("stylus/0.51.1", "stylus"), // sync with build.sbt
    nib = requireIfExists("nib/1.1.0", "nib"); // sync with build.sbt

var promised = {
    mkdirp: nodefn.lift(mkdirp),
    readFile: nodefn.lift(fs.readFile),
    writeFile: nodefn.lift(fs.writeFile)
  };


var args = jst.args(process.argv);


function processor(input, output) {

  return promised.readFile(input, "utf8").then(function(contents) {
    var result = null;

    var options = args.options;
    options.filename = input;

    var style = stylus(contents, options)
    if (options.useNib) style.use(nib());

    options.plugins.forEach(function(plugin) {
      var tmp = require(plugin);
      style.use(tmp());
    });

    style.render(function (err, css) {
        if (err) {
          throw parseError(input, contents, err);
        } else {
          result = {
            css: css,
            style: style
          };
        }
      });
    return result;
    
  }).then(function(result) {
    return promised.mkdirp(path.dirname(output)).yield(result);

  }).then(function(result) {
    return promised.writeFile(output, result.css, "utf8").yield(result);

  }).then(function(result) {
    return {
      source: input,
      result: {
          filesRead: [input].concat(result.style.deps()),
          filesWritten: [output]
      }
    };
  }).catch(function(e) {
    if (jst.isProblem(e)) return e; else throw e;
  });

}


jst.process({processor: processor, inExt: ".styl", outExt: (args.options.compress? ".min.css" : ".css")}, args);


/**
 * Utility to take a stylus error object and coerce it into a Problem object.
 */
function parseError(input, contents, err) {
  var errLines = err.message.split("\n");
  var lineNumber = (errLines.length > 0? errLines[0].substring(errLines[0].indexOf(":") + 1) : 0);
  var lines = contents.split("\n", lineNumber);
  return {
    message: err.name + ": " + (errLines.length > 2? errLines[errLines.length - 2] : err.message),
    severity: "error",
    lineNumber: parseInt(lineNumber),
    characterOffset: 0,
    lineContent: (lineNumber > 0 && lines.length >= lineNumber? lines[lineNumber - 1] : "Unknown line"),
    source: input
  };
}


