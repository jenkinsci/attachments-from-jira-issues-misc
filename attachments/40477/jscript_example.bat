************* JScript portion **********/
var _g=new Object();
_g.loc='';
try {
  var env=WScript.CreateObject("WScript.Shell").Environment("Process"),
      cnt,
      ln=0,
      skip=false,
      quit=false,
      stdin=WScript.StdIn,
      stdout=WScript.Stdout,
      stderr=WScript.Stderr,
      output,
      input;
  _g.ForReading=1;
  _g.ForWriting=2;
  _g.TemporaryFolder=2;
  _g.fso = new ActiveXObject("Scripting.FileSystemObject");
  _g.inFile=env('/F');
  _g.outFile=env('/O');
  _g.tempFile='';
  _g.delim=env('/D');
  _g.term=env('/U')?'\n':'\r\n';
