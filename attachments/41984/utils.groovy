#!groovy

@NonCPS
def convert_windows_to_git_bash(text) {
    List tag_name_lines = text.split('\\\\')
    def disk = tag_name_lines[0]
    disk = disk[0..-2]
    def changed_disk = "/${disk}"
    tag_name_lines[0] = changed_disk
    tag_name_lines.join('/')
}
@NonCPS
def get_second_line(text) {
    List tag_name_lines = text.split('\n')
    tag_name_lines[1]
}
def withSSHAgent(passphrase, keyfile, command) {
    def output = sh(returnStdout: true, script: """
    echo "#!/bin/sh" > ./ssh_pass.sh
    echo "echo '${passphrase}'" >> ./ssh_pass.sh
    chmod +x ./ssh_pass.sh
    eval \$(ssh-agent)
    DISPLAY=: SSH_ASKPASS=./ssh_pass.sh ssh-add "${keyfile}"
    echo "SSH_AGENT_PID=\$SSH_AGENT_PID;export SSH_AGENT_PID;SSH_AUTH_SOCK=\$SSH_AUTH_SOCK;export SSH_AUTH_SOCK"
    """)
    output = get_second_line(output)
    try {
        sh(script: """${output}
        ${command}
        """)
    }
    finally {
        sh(script: """${output}
        trap 'ssh-agent -k' EXIT
        if [ -e ./ssh_pass.sh ]
        then
            rm ./ssh_pass.sh
        fi
        """)
    }
}
def build_shell(passphrase, keyfile, workspace, repo_owner, conan_profile) {
    withEnv(["CONAN_USER_HOME=${env.WORKSPACE}", "CONAN_USERNAME=${repo_owner}"
    , "CONAN_CHANNEL=stable"]) {
        withSSHAgent(passphrase, keyfile, 
        """rm -rf \"${workspace}/build\"
        git --version
        conan remote add conan-local https://conan.iv.integra-s.com False || true
        conan user -r conan-local -p 1 conan || true
        conan remote remove conan-center || true
        conan remote remove conan-transit || true    
        conan remove '*' -f || true
        conan install . -if build -pr=./conan_profiles/${conan_profile}
        conan build . -if build -bf build
        conan export-pkg -f -bf build . ${repo_owner}/stable
        """)
    }
}
def header_only_export_shell(passphrase, keyfile, workspace, repo_owner) {
    withEnv(["CONAN_USER_HOME=${env.WORKSPACE}", "CONAN_USERNAME=${repo_owner}"
    , "CONAN_CHANNEL=stable"]) {
        withSSHAgent(passphrase, keyfile, 
        """rm -rf \"${workspace}/build\"
        conan remote add conan-local https://conan.iv.integra-s.com False || true
        conan user -r conan-local -p 1 conan || true
        conan remote remove conan-center || true
        conan remote remove conan-transit || true    
        conan remove '*' -f || true
        conan export . ${repo_owner}/stable
        """)
    }
}
def conan_prepare() {
    def repo_url = sh(returnStdout: true, script: "git config --get remote.origin.url")
    repo_url = repo_url.trim()
    List list = repo_url.split('/')
    repo_name = list[-1]
    repo_name = repo_name[0..-5]
    repo_owner = list[-2]
    if(repo_owner == "IntegraVideo") {
        repo_owner = "iv"
    }
}
def conan_success() {
    withEnv(["CONAN_USER_HOME=${env.WORKSPACE}", "CONAN_USERNAME=${repo_owner}"
    , "CONAN_CHANNEL=stable"]) {
        sh "conan upload --force --all -c -r conan-local '${repo_name}/*'"
    }
    cleanWs()
}
def conan_failure() {
    withEnv(["CONAN_USER_HOME=${env.WORKSPACE}", "CONAN_USERNAME=${repo_owner}"
    , "CONAN_CHANNEL=stable"]) {
        echo "conan_failure"
    }
    cleanWs()
}
def conan_build(conan_profile) {
    withCredentials([sshUserPrivateKey(credentialsId: 'arnold', passphraseVariable: 'GOGS_PASSPHRASE'
    , usernameVariable: 'GOGS_USER', keyFileVariable: 'GOGS_KEY')]) {
        script {
            if(isUnix()) {
                build_shell(env.GOGS_PASSPHRASE, env.GOGS_KEY, env.WORKSPACE, repo_owner, conan_profile)
            }
            else {
                GIT_BASH_GOGS_KEY = convert_windows_to_git_bash(env.GOGS_KEY)
                workspace = convert_windows_to_git_bash(env.WORKSPACE)
                build_shell(env.GOGS_PASSPHRASE, GIT_BASH_GOGS_KEY, workspace, repo_owner, conan_profile)
            }
        }
    }
}
def conan_header_only_export() {
    withCredentials([sshUserPrivateKey(credentialsId: 'arnold', passphraseVariable: 'GOGS_PASSPHRASE'
    , usernameVariable: 'GOGS_USER', keyFileVariable: 'GOGS_KEY')]) {
        script {
            if(isUnix()) {
                header_only_export_shell(env.GOGS_PASSPHRASE, env.GOGS_KEY, env.WORKSPACE, repo_owner)
            }
            else {
                GIT_BASH_GOGS_KEY = convert_windows_to_git_bash(env.GOGS_KEY)
                workspace = convert_windows_to_git_bash(env.WORKSPACE)
                header_only_export_shell(env.GOGS_PASSPHRASE, GIT_BASH_GOGS_KEY, workspace, repo_owner)
            }
        }
    }    
}
def jenkinsfile_success() {
    slackSend(color: 'good', message: "Build ${env.JOB_NAME} is succeeded(${env.BUILD_URL})")
    cleanWs()
}
def jenkinsfile_failure() {
    withEnv(["CONAN_USER_HOME=${env.WORKSPACE}", "CONAN_USERNAME=${repo_owner}"
    , "CONAN_CHANNEL=stable"]) {
        echo "jenkinsfile_failure" 
    }
    slackSend(color: 'danger', message: "Something wrong with build ${env.JOB_NAME}(${env.BUILD_URL})")
    cleanWs()
}
return this