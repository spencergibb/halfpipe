#!/bin/sh

# from https://github.com/phusion/baseimage-docker
# `/sbin/setuser memcache` runs the given command as the user `memcache`.
# If you omit that part, the command will be run as root.

if [ -n "$JAVA_APP" ]; then
    echo "JAVA_APP must be set to the path of a valid executable jar"
    exit 1
fi

exec java -jar $JAVA_APP >>/var/log/java_app.log 2>&1
