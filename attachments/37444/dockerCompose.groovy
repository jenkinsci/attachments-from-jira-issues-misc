import groovy.transform.Field

@Field final String COMPOSE_FILE_DEFAULT_NAME = 'docker-compose.yml'


def call(composeFile, String services, boolean doDebug = false, Closure body) {
  try {
    withEnv( dockerComposeUp( composeFile, services ) ) {
      debug(doDebug)
      body()
    }
  }
  finally {
    dockerComposeDown( composeFile )
  }
}

static def String splitComposeArgs( composeFiles ) {
  String arg = "";
  for( String composeFile : composeFiles.tokenize(" \t\n\r\f,;") ) {
    arg += "-f $composeFile "
  }

  return arg
}

def dockerComposeUp( composeFiles, String services ) {
  composeFile = splitComposeArgs( composeFiles )
  sh "export GOSU_UID=`id -u` GOSU_GID=`id -g` ; docker-compose $composeFile pull --ignore-pull-failures $services &&\
      docker-compose $composeFile up -d $services &&\
      docker-compose $composeFile ps"

  script = "SERVICES=\"$services\"\n" +
          'for SERVICE in $SERVICES; do \n' +
          "  CONTAINERID=`docker-compose $composeFile ps -q \$SERVICE`\n" +
          '  IP=`docker inspect --format \'{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}\' $CONTAINERID`\n' +
          '  PORTS=`docker inspect --format=\'{{range $p, $conf := .NetworkSettings.Ports}} {{$p}}{{end}}\' $CONTAINERID`\n' +
          '  for PORTPROTO in $PORTS; do\n' + '    IFS=\'/ \' read -r -a array <<< "$PORTPROTO"\n' +
          '    PORT="${array[0]}"\n' +
          '    PROTO="${array[1]}"\n' +
          '    echo "${SERVICE^^}_PORT_${PORT}_${PROTO^^}_PORT=$PORT"\n' +
          '    echo "${SERVICE^^}_PORT_${PORT}_${PROTO^^}_ADDR=${IP}"\n' +
          '  done\n' +
          'done'

  stdout = sh( returnStdout: true, script: "$script" )
  //stdout = sh( returnStdout: true, script: "for container in $services; do docker-compose run --rm -T -e TERMINAL=xterm \$container env|grep PORT;done" )

  return stdout.tokenize("\r?\n")
}

def dockerComposeDown( composeFiles ) {
  composeFile = splitComposeArgs( composeFiles )
  sh "export GOSU_UID=`id -u` GOSU_GID=`id -g` ; docker-compose $composeFile down -v"
}
