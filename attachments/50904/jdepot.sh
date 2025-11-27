#!/bin/bash
CHANGE=$1
 
P4PORT=192.168.1.26:1666
JUSER=jenkins
JPASS=11a2b89bff4eca82203e153ec0d4e44995
JSERVER=http://192.168.1.26:8080
 
 
curl --header 'Content-Type: application/json' \
     --request POST \
     --silent \
     --user $JUSER:$JPASS \
     --data payload="{change:$CHANGE,p4port:\"$P4PORT\"}" \
     $JSERVER/p4/change
