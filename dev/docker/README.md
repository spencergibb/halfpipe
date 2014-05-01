Development environment setup
================

Ansible, Vagrant, packer.io and docker.io


    vagrant provision
    vagrant ssh
    cd packer/
    packer build consul.json
    packer build consul_server.json

    docker run -d -p 8300:8300 -p 8301:8301 -p 8302:8302 -p 8400:8400 -p 8500:8500 -p 8600:8600 --name consul_server \
        32degrees/consul_server:0.1 /usr/local/bin/consul agent -server -bootstrap -data-dir=/opt/consul \
        -client=0.0.0.0 -config-file /opt/consul.json



    on host
    consul agent -join 192.168.32.10 -data-dir=/tmp/consul-client -bind 192.168.32.1




    export CONSUL_SERVER_IP=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' consul_server)

    # run a client for local development
    docker run -d -p 8500:8500 --name consul_client 32degrees/consul:0.1 \
        /usr/local/bin/consul agent -data-dir=/opt/consul -client=0.0.0.0 -join $CONSUL_SERVER_IP

    docker run -d -p 192.168.32.11:8500:8500 -p 192.168.32.11:8400:8400 -p 192.168.32.11:8301:8301 -p 192.168.32.11:8302:8302 -p 192.168.32.11:8600:8600 --name consul_server \
        32degrees/consul:0.1 /usr/local/bin/consul agent -server -bootstrap -data-dir=/opt/consul -client=192.168.32.11 -bind=192.168.32.11