#docker run -d --net=host --dns=10.1.42.1 --name=consulserver1 32degrees/consulserver:0.1 /sbin/my_init --enable-insecure-key
docker run -d --name=consulserver1 32degrees/consulserver:0.1 /sbin/my_init --enable-insecure-key
