
node { 
  echo "in node"

  def workspaceDir = pwd()
  dir("$workspaceDir") { deleteDir() }

  echo "clear"
  checkout scm

  def other = load "jenkins/other.groovy"
  echo "before other"
  other.testEcho()
  echo "after default"
  other.testEcho("overriding default")
  echo "after override"
  echo "out prepare"
}

