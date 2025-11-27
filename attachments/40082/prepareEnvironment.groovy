def call(args) {
  echo "Preparing envirnonment..."
  def isLibrary = args.isLibrary?:false
  def isWeblogicCommonLibrary = args?.isWeblogicCommonLibrary?:false
  
  echo "Is library? ${isLibrary}"
  echo "Is weblogic common library? ${isWeblogicCommonLibrary}"

  if(isLibrary && !isWeblogicCommonLibrary) {
      echo "Preparing environment for library" 
  } else {
      echo "Preparing environment for app" 
  }
}