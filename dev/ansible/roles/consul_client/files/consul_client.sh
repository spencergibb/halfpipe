#!/bin/sh

# from https://github.com/phusion/baseimage-docker
# `/sbin/setuser memcache` runs the given command as the user `memcache`.
# If you omit that part, the command will be run as root.

#exec /sbin/setuser memcache /usr/bin/memcached >>/var/log/memcached.log 2>&1
#IP=$(/sbin/ifconfig eth0 | grep "inet addr" | awk -F: '{print $2}' | awk '{print $1}')
DATA_DIR=/opt/consul
mkdir ${DATA_DIR}
exec /usr/local/bin/consul agent -join ${CONSUL_SERVER} -data-dir=${DATA_DIR} -client=0.0.0.0 >>/var/log/consul.log 2>&1
