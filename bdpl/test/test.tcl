# java should be in the path, otherwise add the full path
set javaexec java

set env(CLASSPATH) {x:\tools\antlr.jar;x:\lib\bdpl.jar}
puts $env(CLASSPATH)

set testPath [file nativename [file join "." "test"]]
cd $testPath

# Collect all the bdl files that are in the current directory
set files [glob *.bdl]

# Loop through all the files that have been found
foreach f $files { 
 set result [exec -- $javaexec BdplMain -f $f 2>&1 > "out/$f.out"]
 catch {set result [exec -- diff -b -q "out/$f.out" "golden/$f.golden" ]} error
 switch $error "" "puts {Testing $f......OK}" "default" "puts {Testing $f .......FAILED $error}"
}
