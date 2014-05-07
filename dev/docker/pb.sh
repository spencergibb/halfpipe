#!/bin/sh
ansible-playbook -i ../ansible/docker.ini -u vagrant --private-key=~/.vagrant.d/insecure_private_key ../ansible/docker.yml "$@"
