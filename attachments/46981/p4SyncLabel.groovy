#!/usr/bin/env groovy

def call(String credential, String ws_name, String ws_view, String label, boolean append, boolean force) {
   def p4cspec = [$class: 'WorkspaceSpec',
                     allwrite: false,
                     clobber: true,
                     compress: false,
                     locked: false,
                     modtime: false,
                     rmdir: false,
                     streamName: '',
                     line: 'LOCAL',
                     view: "${ws_view}",
                     changeView: '',
                     type: 'WRITABLE',
                     serverID: '',
                     backup: true]

   def p4ws = [$class: 'ManualWorkspaceImpl',
                     charset: 'none',
                     pinHost: false,
                     name: "${ws_name}",
                     spec: p4cspec,
                     cleanup: false]

   def p4Obj = p4(credential: "${credential}",
                  workspace: p4ws)

   def label_ext
   if (label == "head" || label == "now") {
      label_ext = "#head"
   } else {
      if (append) {
         label_ext = "@=" + label
      } else {
         label_ext = "@" + label
      }
   }
   if (force) {
      p4Obj.run('sync','-f','...'+label_ext)
   } else {
      p4Obj.run('sync','...'+label_ext)
   }
}
