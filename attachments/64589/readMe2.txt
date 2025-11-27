# ================================
# 1. Generate Root Certificate Authority (CA)
# ================================
openssl genrsa -out rootCA.key 4096
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 1825 -out rootCA.crt \
  -subj "/C=ES/ST=Sevilla/L=Sevilla/O=abc Organization/CN=abc Root CA/emailAddress=abc@abc.com"

# ================================
# 2. Prepare Server Certificate with SAN (Subject Alternative Name)
# ================================
cat > server_cert.cnf <<EOF
[req]
default_bits = 2048
prompt = no
default_md = sha256
distinguished_name = dn
req_extensions = req_ext

[dn]
C = ES
ST = Sevilla
L = Sevilla
O = abc Organization
OU = abc Eng
CN = 192.168.1.13

[req_ext]
subjectAltName = @alt_names

[alt_names]
IP.1 = 192.168.1.13
DNS.1 = localhost
EOF

openssl genrsa -out server.key 2048
openssl req -new -key server.key -out server.csr -config server_cert.cnf
openssl x509 -req -in server.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial \
  -out server.crt -days 365 -sha256 -extensions req_ext -extfile server_cert.cnf

# ================================
# 3. Prepare Client Certificate
# ================================
cat > client_cert.cnf <<EOF
[req]
default_bits = 2048
prompt = no
default_md = sha256
distinguished_name = dn

[dn]
C = ES
ST = Sevilla
L = Sevilla
O = abc Organization
OU = abc Eng
CN = yourclient
EOF

openssl genrsa -out client.key 2048
openssl req -new -key client.key -out client.csr -config client_cert.cnf
openssl x509 -req -in client.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial \
  -out client.crt -days 365 -sha256

# ================================
# 4. Create PKCS12 Keystore for Java/Jenkins
# ================================
openssl pkcs12 -export -out client_keystore.p12 -inkey client.key -in client.crt -certfile rootCA.crt

# ================================
# 5. Add Root CA to Java Truststore (fixes PKIX error)
# ================================
sudo keytool -import -trustcacerts -alias abc_root_ca -file rootCA.crt \
  -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

# ================================
# 6. Prepare NGINX Certs Directory and Configuration
# ================================
mkdir -p nginx/certs2
cp rootCA.crt server.crt server.key nginx/certs2/

mkdir -p conf
cat > conf/default2.conf <<EOF
server {
    listen 1443 ssl;
    server_name 192.168.1.13;

    ssl_certificate     /etc/nginx/certs/server.crt;
    ssl_certificate_key /etc/nginx/certs/server.key;
    ssl_client_certificate /etc/nginx/certs/rootCA.crt;
    ssl_verify_client on;

    ssl_protocols       TLSv1.2 TLSv1.3;
    ssl_ciphers         HIGH:!aNULL:!MD5;

    proxy_set_header X-SSL-Client-Subject \$ssl_client_s_dn;
    proxy_set_header X-SSL-Client-Verify \$ssl_client_verify;

    location / {
        proxy_pass http://192.168.1.13:7990;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;
    }
}
EOF

# ================================
# 7. Run NGINX in Docker with New Certificates & Config
# ================================
docker run --rm --name nginx-proxy2 \
  -p 2443:1443 \
  -v $(pwd)/conf/default2.conf:/etc/nginx/conf.d/default.conf:ro \
  -v $(pwd)/nginx/certs2:/etc/nginx/certs:ro \
  -d nginx

# ================================
# 8. Test End-to-End mTLS with curl
# ================================
curl -vk --cert client.crt --key client.key --cacert rootCA.crt https://localhost:2443/rest/api/1.0/projects/test

# ================================
# 9. TROUBLESHOOTING
# ================================

# ---(A) Verify Server Certificate is Correct and Signed by Root CA
echo
echo "*** Verify: Is server certificate signed by your Root CA? ***"
openssl x509 -in server.crt -noout -issuer -subject
echo "Should show:"
echo "  - issuer: ... CN=abc Root CA ..."
echo "  - subject: ... CN=192.168.1.13 ..."
echo
openssl verify -CAfile rootCA.crt server.crt
# Output should be: server.crt: OK

# ---(B) Verify Client Certificate is Correct and Signed by Root CA
echo
echo "*** Verify: Is client certificate signed by your Root CA? ***"
openssl x509 -in client.crt -noout -issuer -subject
echo "Should show:"
echo "  - issuer: ... CN=abc Root CA ..."
echo "  - subject: ... CN=yourclient ..."
echo
openssl verify -CAfile rootCA.crt client.crt
# Output should be: client.crt: OK

# ---(C) List certificates within your PKCS12 Java keystore
echo
echo "*** List your PKCS12 contents (should show one PrivateKeyEntry for your client cert) ***"
keytool -list -keystore client_keystore.p12 -storetype PKCS12 -storepass changeit -v

# ---(D) Convert PKCS12 (.p12) to Java JKS format (if needed, e.g. for legacy Java code)
echo
echo "*** Convert PKCS12 to JKS ***"
keytool -importkeystore \
  -srckeystore client_keystore.p12 \
  -srcstoretype PKCS12 \
  -srcstorepass changeit \
  -destkeystore client_keystore.jks \
  -deststoretype JKS \
  -deststorepass changeit

# To view entries in the JKS:
echo
echo "*** List entries in the JKS (should see a PrivateKeyEntry and alias) ***"
keytool -list -keystore client_keystore.jks -storepass changeit -v

# ---(E) Get full details of any certificate
openssl x509 -in server.crt -noout -text
openssl x509 -in client.crt -noout -text

# ---(F) List trusted CAs in your Java truststore
keytool -list -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit | grep abc

# ---(G) Test NGINX SSL debug (if needed)
# Add in nginx.conf "error_log /var/log/nginx/error.log debug;"
# docker logs nginx-proxy2

# ---(H) Clean up or regenerate truststore if outdated/trust still broken
# sudo keytool -delete -alias abc_root_ca -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit
# sudo keytool -import -trustcacerts -alias abc_root_ca -file rootCA.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

# ---(I) Test basic server-side certificate details in the running nginx container:
docker exec nginx-proxy2 openssl x509 -noout -issuer -subject -in /etc/nginx/certs/server.crt

# ---(J) Final test: full chain verification
openssl s_client -connect localhost:2443 -cert client.crt -key client.key -CAfile rootCA.crt

# Should see "Verify return code: 0 (ok)" at the bottom


# ================================
# Done! NGINX mTLS with working troubleshooting and fix steps included.
# ================================