openssl genrsa -out ca_open_jade.key 1024
openssl req -new -key ca_open_jade.key -out ca_open_jade.csr -subj '/CN=Autoridade Certificadora Raiz OpenJade/O=ICP-Brasil-Open-Jade/C=BR/ST=Curitiba/L=Parana'
openssl x509 -req -days 3650 -in ca_open_jade.csr -signkey ca_open_jade.key -out ca_open_jade.crt
openssl genrsa -out server_open_jade.key 1024
openssl req -new -key server_open_jade.key -out server_open_jade.csr -subj '/CN=Autoridade Certificadora Raiz OpenJade/O=ICP-Brasil-Open-Jade/C=BR/ST=Curitiba/L=Parana'
openssl x509 -req -days 3650 -in server_open_jade.csr -signkey server_open_jade.key -out server_open_jade.crt

openssl pkcs12 -export -clcerts -in ca_open_jade.crt -inkey ca_open_jade.key -out keystore_open_jade.p12 -name alias_ca

#openssl genrsa -out client_sic.key 1024
#openssl req -new -key client_sic.key -out client_sic.csr -subj '/CN=Autoridade Certificadora Raiz OpenJade/O=ICP-Brasil-Open-Jade/C=BR/ST=Curitiba/L=Parana'
#openssl x509 -req -days 3650 -CA ca_open_jade.crt -CAkey ca_open_jade.key -CAcreateserial -in client_sic.csr -out client_sic.crt
#senha: homolog

#SSLCACertificateFile /tmp/ca_open_jade.crt
#SSLCertificateFile /tmp/server_open_jade.crt
#SSLCertificateKeyFile /tmp/server_open_jade.key
#SSLVerifyClient require
#SSLVerifyDepth  10
