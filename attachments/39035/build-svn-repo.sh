#!/bin/bash

set -xe
export CWD=$(pwd)
echo "CWD=${CWD}"

#
# define directories
#
export REPOS_DIR=${CWD}/repos
export WORKING_DIR=${CWD}/working
export REPOS_WORKING_DIR=${WORKING_DIR}/repos
export PROJECT_WORKING_DIR=${WORKING_DIR}/project
export REPOS_URL=svn://localhost/
#
# cleanup
#
rm -rf ${REPOS_DIR} ${WORKING_DIR}

killall -q svnserve || true

#
# create start condition
#
svnadmin create ${REPOS_DIR}
perl -pi \
	-e 's/# anon-access = read/anon-access = none/;' \
	-e 's/# auth-access = write/auth-access = write/;' \
	-e 's/# password-db = passwd/password-db = passwd/;' \
	${REPOS_DIR}/conf/svnserve.conf

echo "myself=itsme" >> ${REPOS_DIR}/conf/passwd
echo "jenkins=testit" >> ${REPOS_DIR}/conf/passwd
export USERPASS="--username=myself --password=itsme"

svnserve -d --root=${REPOS_DIR}

svn co ${USERPASS} ${REPOS_URL} ${REPOS_WORKING_DIR}

#
# build working directories
#
svn mkdir --parents ${REPOS_WORKING_DIR}/libraries/{lib1,lib2,lib3}/{trunk,tags,branches}
svn mkdir --parents ${REPOS_WORKING_DIR}/projects/{project1,project2}/{mainproject,subproject1,subproject2}/{trunk,tags,branches}
svn ci ${USERPASS}  -m'created directory structure' ${REPOS_WORKING_DIR}
#svn list ${USERPASS} -R ${REPOS_URL}
#
# building externals
#
(
echo "^/projects/project1/subproject1/trunk subproject1"
echo "^/projects/project1/subproject2/trunk subproject2"
) | svn ps "svn:externals" -F - ${REPOS_WORKING_DIR}/projects/project1/mainproject/trunk

(
echo "^/libraries/lib1/trunk lib1"
echo "^/libraries/lib2/trunk lib2"
echo "^/libraries/lib3/trunk lib3"
) | svn ps "svn:externals" -F - ${REPOS_WORKING_DIR}/projects/project1/subproject1/trunk

(
echo "^/libraries/lib1/trunk lib1"
echo "^/libraries/lib2/trunk lib2"
echo "^/libraries/lib3/trunk lib3"
) | svn ps "svn:externals" -F - ${REPOS_WORKING_DIR}/projects/project1/subproject2/trunk
svn ci ${USERPASS} -m'created svn:externals' ${REPOS_WORKING_DIR}

#
# check out one project
#
svn co ${USERPASS} ${REPOS_URL}/projects/project1/mainproject/trunk ${PROJECT_WORKING_DIR}

#
# fill with some code
#
echo "function add(a,b) return a+b end" > ${PROJECT_WORKING_DIR}/subproject1/lib1/add.lua
echo "function sub(a,b) return a-b end" > ${PROJECT_WORKING_DIR}/subproject1/lib1/sub.lua

echo "function mul(a,b) return a*b end" > ${PROJECT_WORKING_DIR}/subproject1/lib2/mul.lua
echo "function div(a,b) return a/b end" > ${PROJECT_WORKING_DIR}/subproject1/lib2/div.lua

svn add ${PROJECT_WORKING_DIR}/subproject1/lib*/*.lua
svn ci ${USERPASS} -m'added modules' ${PROJECT_WORKING_DIR}/subproject1/lib*

#
# deploy to other dirs
#
svn up ${USERPASS} ${PROJECT_WORKING_DIR}

echo "ALL DONE"

