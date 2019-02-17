#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

# setup docker
eval_echo "sudo apt-get update -y"
eval_echo "sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D"
eval_echo "sudo apt-get install software-properties-common -y"
eval_echo "sudo apt-get install apt-transport-https -y"
eval_echo "sudo apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'"
eval_echo "sudo apt-get update -y"
eval_echo "apt-cache policy docker-engine"
eval_echo "sudo apt-get install -y docker-engine"
eval_echo "sudo systemctl status docker --no-pager"
	
# setup compose
eval_echo "curl -L 'https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)' -o /usr/local/bin/docker-compose"
eval_echo "chmod +x /usr/local/bin/docker-compose"
eval_echo "docker-compose --version"
	
NEW_USER=alex
eval_echo "adduser --disabled-password --gecos '' $NEW_USER"

eval_echo "usermod -aG sudo $NEW_USER"
groups $NEW_USER

eval_echo "usermod -aG docker $NEW_USER"
groups $NEW_USER
	
eval_echo "mkdir /home/$NEW_USER/.ssh"
ls -la /home/$NEW_USER/.ssh

eval_echo "chown $NEW_USER:$NEW_USER /home/$NEW_USER/.ssh"
ls -la /home/$NEW_USER/.ssh

eval_echo "chmod 700 /home/$NEW_USER/.ssh"
ls -la /home/$NEW_USER/.ssh

eval_echo "echo 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCTH7actwVII/hwh/C/EuAR7shdfdOovjeak86k9V3vXHSTBIoarvZVEhsAd1i/2mlujmFhecbQvT78h7aKdmwF2r+2U/QgeWwPFmpGuNQXNGskidumW3FE1eI7v8wiGy1dxtdmxfEPHpZSr7C3+d6GQsR2WbhvNay5hU7ADDRHU6KPknBn1kZL/ZEaqxlBR1hMlHANeoUTLqQbdQL8DcNAlOicatjSfXMml93vy2y2Nz91GD646TIRPhjh+b2/JzxaREr3tHzFWzBLfqFXo/6k9beUVCi4GDrTSVLA/YKxqkcVItPlr+M9TvPZsr+84eQchpuCbUb0QoHmTBt//EMv indigo@indigo-pc' >> /home/$NEW_USER/.ssh/authorized_keys"
ls -la /home/$NEW_USER/.ssh/authorized_keys

eval_echo "chown $NEW_USER:$NEW_USER /home/$NEW_USER/.ssh/authorized_keys"
ls -la /home/$NEW_USER/.ssh/authorized_keys

eval_echo "chmod 600 /home/$NEW_USER/.ssh/authorized_keys"
ls -la /home/$NEW_USER/.ssh/authorized_keys

eval_echo "sed -i 's/\(Port \).*\$/\14632/' /etc/ssh/sshd_config"
cat /etc/ssh/sshd_config | grep '^Port '

# comment PasswordAuthentication
# eval_echo "sed -i '/PasswordAuthentication /s/^/#/' /etc/ssh/sshd_config"
eval_echo "sed -i '/PasswordAuthentication /s/^#//' /etc/ssh/sshd_config"
eval_echo "sed -i 's/\(PasswordAuthentication \).*\$/\1no/' /etc/ssh/sshd_config"
cat /etc/ssh/sshd_config | grep '^PasswordAuthentication '

eval_echo "sed -i 's/\(RSAAuthentication \).*\$/\1yes/' /etc/ssh/sshd_config"
cat /etc/ssh/sshd_config | grep '^RSAAuthentication '

eval_echo "sed -i 's/\(PubkeyAuthentication \).*\$/\1yes/' /etc/ssh/sshd_config"
cat /etc/ssh/sshd_config | grep '^PubkeyAuthentication '

eval_echo "sudo systemctl reload sshd"

echo "[93mPlease try to connect via ssh as $NEW_USER[0m"

# setup dockerhub account
# https://docs.docker.com/engine/reference/commandline/login/
# copy ~/.docker/config.json <from other server>

