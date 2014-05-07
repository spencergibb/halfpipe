SSH_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' $1)
SSH_USER=${2:-root}
ssh -i ~/build/docker/insecure_key -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $SSH_USER@$SSH_HOST
