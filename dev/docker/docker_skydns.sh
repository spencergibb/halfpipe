docker run --net=host -d -p 10.1.42.1:53:53/udp --name skydns crosbymichael/skydns -nameserver 8.8.8.8:53 -domain halfpipe

