PARENT:
script:return[
'New',
'Image',
'None'
]
Fallback:return['error']

CHILD:
script:
if (INSTALL_TYPE.equals("VAL1")) {
   return ["None"]
} else if (INSTALL_TYPE.equals("VAL2")) {
  imagelist=[]  
  baseDir = new File( "/mnt/czimages_new" )
  imgname=["None"]+baseDir.listFiles().name.sort();
  def idx_150464=imgname.findIndexValues {it == 'UBUNTU_1504_64'}.get(0)

if ((imgname.contains('UBUNTU_1504_64')))
{
  list=list+(imgname.get(idx_150464.toInteger()))
}    
imagelist=["None"]+imagelist
return imagelist
} else if (INSTALL_TYPE.equals("VAL3")) {
  return ["UBUNTU_1504_64","UBUNTU_1510_64","UBUNTU_1504_32","UBUNTU_1510_32"]
}
Fallback:return['error']