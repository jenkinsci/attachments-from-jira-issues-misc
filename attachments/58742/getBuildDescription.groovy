/**
 * Retrieves build description from other job's build
 * @param project Jenkins project name (job name)
 * @param buildNum Build number
 */

def call(Map params = [:]) {
  Integer buildNum = params.buildNum
  def build = Jenkins.instance.getItemByFullName("${params.project}").getBuildByNumber(buildNum)
  return build.getDescription()
}
