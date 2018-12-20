@ECHO OFF

set M2_HOME=D:\frameworks\apache-maven-2.2.1
set MVN_EXEC=D:\frameworks\apache-maven-2.2.1\bin\mvn

cd veloster-api
%MVN_EXEC% clean javadoc:jar source:jar install
rem cd ..
rem cd veloster-droid
rem %MVN_EXEC% clean javadoc:jar source:jar install


exit