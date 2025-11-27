Problem
-------

`Caused: java.io.IOException: Cannot run program "docker" (in directory "/home/jenkins/agent/workspace/test"): error=2, No such file or directory`

My impression is that Jenkins is trying to run Docker in the labeled agent due to the pipeline-level Docker agent configuration despite specifying an agent by label for the last stage.  This seems like maybe a bug?  I also am open to suggestions about a better structure for this.  Note that in the real case there are many stages that I want run with the pipeline-level specified Docker agent.

Setup
-----

- Download this gist and extract the contents.  Run the following `docker-compose` to bring up Jenkins.
  - `
docker-compose build --build-arg DOCKER_SOCK_GID=$(stat -c '%g' /var/run/docker.sock) && docker-compose up --renew-anon-volumes --remove-orphans
`

- Open http://localhost:8080/
- Enter password from Jenkins startup
- Select plugins to install
  - Select None
  - Select Install
- Skip and continue as admin
- Save and finish
- Start using Jenkins
- New Item
  - Set name to test
  - Select pipeline
  - Ok
  - Add the pipeline script below
  - Save
- Build now
