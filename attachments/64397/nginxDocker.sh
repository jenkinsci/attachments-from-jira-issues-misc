docker run --name nginx-proxy \
  -p 1443:1443 \
  -v "$(pwd)/conf/default.conf:/etc/nginx/conf.d/default.conf:ro" \
  -v "$(pwd)/certs:/etc/nginx/certs:ro" \
  -d nginx