Gestartet durch Benutzer [8mha:////4ICNShb4e4ehx7wElYjhvoZsuV6GXw055AcgZUYF3PizAAAAlx+LCAAAAAAAAP9b85aBtbiIQTGjNKU4P08vOT+vOD8nVc83PyU1x6OyILUoJzMv2y+/JJUBAhiZGBgqihhk0NSjKDWzXb3RdlLBUSYGJk8GtpzUvPSSDB8G5tKinBIGIZ+sxLJE/ZzEvHT94JKizLx0a6BxUmjGOUNodHsLgAyuEgZu/dLi1CL9xJTczDwAEmIUecAAAAA=[0mSLSE Admin
[EnvInject] - Loading node environment variables.
Baue auf dem Agenten „[8mha:////4MEo9ONPFCc+B6dIajvXIJzSOz1Mb8vwNyXfTfrmMdUbAAAAoB+LCAAAAAAAAP9b85aBtbiIQTGjNKU4P08vOT+vOD8nVc83PyU1x6OyILUoJzMv2y+/JJUBAhiZGBgqihhk0NSjKDWzXb3RdlLBUSYGJk8GtpzUvPSSDB8G5tKinBIGIZ+sxLJE/ZzEvHT94JKizLx0a6BxUmjGOUNodHsLgAyuEgYR/eT83ILSktQifWfPeOcQZ8cAQzMA9zRg+skAAAA=[0mCI_CTCAP16“ (ci_ctc) in Arbeitsbereich /shd/CTC/TOOLS/Jenkins/workspace/ChrisTest
[ChrisTest] $ /bin/sh -xe /tmp/jenkins5202261866403083758.sh
+ echo 'void doSomething() { int a; }'
+ echo 'Project(TestProject)'
+ echo 'Add_Definitions(-Wunused)'
+ echo 'Add_Library(TestLib main.cpp)'
[build] $ cmake /shd/CTC/TOOLS/Jenkins/workspace/ChrisTest
CMake Warning (dev) in CMakeLists.txt:
  No cmake_minimum_required command is present.  A line of code such as

    cmake_minimum_required(VERSION 3.5)

  should be added at the top of the file.  The version specified may be lower
  if you wish to support older CMake versions for this project.  For more
  information run "cmake --help-policy CMP0000".
This warning is for project developers.  Use -Wno-dev to suppress it.

-- Configuring done
-- Generating done
-- Build files have been written to: /shd/CTC/TOOLS/Jenkins/workspace/ChrisTest/build
[build] $ /usr/bin/gmake
Scanning dependencies of target TestLib
[ 50%] Building CXX object CMakeFiles/TestLib.dir/main.o
/shd/CTC/TOOLS/Jenkins/workspace/ChrisTest/main.cpp: In function ‘void doSomething()’:
/shd/CTC/TOOLS/Jenkins/workspace/ChrisTest/main.cpp:1:26: warning: unused variable ‘a’ [-Wunused-variable]
 void doSomething() { int a; }
                          ^
[100%] Linking CXX static library libTestLib.a
[100%] Built target TestLib
Skipping issues blame since Git is the only supported SCM up to now.
[GNU C Compiler 4 (gcc)] Sleeping for 5 seconds due to JENKINS-32191...
[GNU C Compiler 4 (gcc)] Parsing console log (workspace: '/shd/CTC/TOOLS/Jenkins/workspace/ChrisTest')
[GNU C Compiler 4 (gcc)] Post processing issues on 'CI_CTCAP16' with encoding 'UTF-8'
[GNU C Compiler 4 (gcc)] Resolving absolute file names for all issues
[GNU C Compiler 4 (gcc)] -> 0 resolved, 0 unresolved, 1 already resolved
[GNU C Compiler 4 (gcc)] Copying affected files to Jenkins' build folder /var/lib/jenkins/jobs/ChrisTest/builds/11
[GNU C Compiler 4 (gcc)] -> 1 copied, 0 not in workspace, 0 not-found, 0 with I/O error
[GNU C Compiler 4 (gcc)] Resolving module names from module definitions (build.xml, pom.xml, or Manifest.mf files)
[GNU C Compiler 4 (gcc)] -> resolved module names for 1 issues
[GNU C Compiler 4 (gcc)] Resolving package names (or namespaces) by parsing the affected files
[GNU C Compiler 4 (gcc)] -> resolved package names of 1 affected files
[GNU C Compiler 4 (gcc)] No filter has been set, publishing all 1 issues
[GNU C Compiler 4 (gcc)] Creating fingerprints for all affected code blocks to track issues over different builds
[GNU C Compiler 4 (gcc)] -> created fingerprints for 1 issues
[GNU C Compiler 4 (gcc)] Skipping blaming as requested in the job configuration
[GNU C Compiler 4 (gcc)] Attaching ResultAction with ID 'gcc4' to run 'ChrisTest #11'.
[GNU C Compiler 4 (gcc)] Using reference build 'ChrisTest #10' to compute new, fixed, and outstanding issues
[GNU C Compiler 4 (gcc)] Issues delta (vs. reference build): outstanding: 0, new: 1, fixed: 0
[GNU C Compiler 4 (gcc)] No quality gates have been set - skipping
[GNU C Compiler 4 (gcc)] Health report is disabled - skipping
[GNU C Compiler 4 (gcc)] Created analysis result for 1 issues (found 1 new issues, fixed 0 issues)
Started calculate disk usage of build
Finished Calculation of disk usage of build in 0 seconds
Started calculate disk usage of workspace
Finished Calculation of disk usage of workspace in 0 seconds
Notifying upstream projects of job completion
Finished: SUCCESS
