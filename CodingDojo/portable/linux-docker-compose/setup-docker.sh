# setup docker
sudo apt-get update -y
sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
sudo apt-get install software-properties-common -y
sudo apt-get install apt-transport-https -y
sudo apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'
sudo apt-get update -y
apt-cache policy docker-engine
sudo apt-get install -y docker-engine
sudo systemctl status docker --no-pager
	
# setup compose
curl -L "https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose --version
	
NEW_USER=alex
adduser $NEW_USER
usermod -aG sudo $NEW_USER
usermod -aG docker $NEW_USER
	
mkdir /home/$NEW_USER/.ssh
chmod 700 /home/$NEW_USER/.ssh
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCTH7actwVII/hwh/C/EuAR7shdfdOovjeak86k9V3vXHSTBIoarvZVEhsAd1i/2mlujmFhecbQvT78h7aKdmwF2r+2U/QgeWwPFmpGuNQXNGskidumW3FE1eI7v8wiGy1dxtdmxfEPHpZSr7C3+d6GQsR2WbhvNay5hU7ADDRHU6KPknBn1kZL/ZEaqxlBR1hMlHANeoUTLqQbdQL8DcNAlOicatjSfXMml93vy2y2Nz91GD646TIRPhjh+b2/JzxaREr3tHzFWzBLfqFXo/6k9beUVCi4GDrTSVLA/YKxqkcVItPlr+M9TvPZsr+84eQchpuCbUb0QoHmTBt//EMv indigo@indigo-pc" >> /home/$NEW_USER/.ssh/authorized_keys
chmod 600 /home/$NEW_USER/.ssh/authorized_keys

sed -i "s/\(Port \).*\$/\18473/" /etc/ssh/sshd_config
sed -i "s/\(PasswordAuthentication \).*\$/\1no/" /etc/ssh/sshd_config
sed -i "s/\(RSAAuthentication \).*\$/\1yes/" /etc/ssh/sshd_config
sed -i "s/\(PubkeyAuthentication \).*\$/\1yes/" /etc/ssh/sshd_config
systemctl reload sshd
# connect via putty ssh to $NEW_USER with peagent 

# setup dockerhun account	
# https://docs.docker.com/engine/reference/commandline/login/
# copy ~/.docker/config.json <from other server>	
	
# setup timezone
echo "Europe/Kiev" > /etc/timezone   
sudo dpkg-reconfigure -f noninteractive tzdata
	
	