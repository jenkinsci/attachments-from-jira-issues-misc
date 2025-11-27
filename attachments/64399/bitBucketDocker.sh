docker run \
    -v "$(pwd)/data:/var/atlassian/application-data/bitbucket" \
    -p 7990:7990 -p 7999:7999 \
    -e BITBUCKET_HOME=/var/atlassian/application-data/bitbucket \
    -e FEATURE_PUBLIC_ACCESS=true \
    --name="bitbucket" \
    -d \
    --restart unless-stopped \
    atlassian/bitbucket-server