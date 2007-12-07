# java should be in the path, otherwise add the full path
set javaexec java

set env(CLASSPATH) {x:\tools\antlr.jar;x:\lib\bdpl.jar}
puts $env(CLASSPATH)

set testPath [file nativename [file join "." "test"]]
puts $testPath
cd $testPath

# Collect all the bdl files that are in the current directory
set files [glob *.bdl]

# Loop through all the files that have been found
foreach f $files { puts "Testing $f"
set result [exec -- $javaexec BdplMain $f]}