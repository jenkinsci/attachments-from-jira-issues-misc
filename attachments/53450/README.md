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
- Manage Jenkins
  - Manage Nodes and Clouds
    - Configure Clouds
      - Amazon EC2
        - Amazon EC2 Credentials
          - Add your AWS credentials and select them
        - Select your region
        - EC2 Key Pair's Private Key
          - Add SSH username with private key
          - User: ec2-user
          - Private key: see `jenkins_parallel_test.pem`
          - Add and select
        - Test connection to make sure this bit is working
        - Set your security group names
        - Click advanced at the bottom
        - Set your subnet IDs for VPC
      - Save
- New Item
  - Set name to test
  - Select pipeline
  - Ok
  - Add the pipeline script from `Jenkinsfile.groovy`
  - Save
- Build now
