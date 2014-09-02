#!/bin/bash


export M2_HOM=/opt/apache-maven-2.2.1/
export MVN_EXEC=/opt/apache-maven-2.2.1/bin/mvn

$MVN_EXEC clean javadoc:jar source:jar install

exit 0
