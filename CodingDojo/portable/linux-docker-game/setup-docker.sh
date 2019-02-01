# do not run this script
exit 1;

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
	
# compose
curl -L "https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose --version
	
adduser someuser
usermod -aG sudo someuser
su - someuser
    exit
usermod -aG docker someuser
su - someuser
    docker container ps -a
	
    mkdir ~/.ssh
    chmod 700 ~/.ssh
    vim ~/.ssh/authorized_keys 
        ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCTH7actwVII/hwh/C/EuAR7shdfdOovjeak86k9V3vXHSTBIoarvZVEhsAd1i/2mlujmFhecbQvT78h7aKdmwF2r+2U/QgeWwPFmpGuNQXNGskidumW3FE1eI7v8wiGy1dxtdmxfEPHpZSr7C3+d6GQsR2WbhvNay5hU7ADDRHU6KPknBn1kZL/ZEaqxlBR1hMlHANeoUTLqQbdQL8DcNAlOicatjSfXMml93vy2y2Nz91GD646TIRPhjh+b2/JzxaREr3tHzFWzBLfqFXo/6k9beUVCi4GDrTSVLA/YKxqkcVItPlr+M9TvPZsr+84eQchpuCbUb0QoHmTBt//EMv indigo@indigo-pc
    chmod 600 ~/.ssh/authorized_keys
    exit
passwd
    # enter root password
vim /etc/ssh/sshd_config
    # change
    Port 4723
    PasswordAuthentication no
    RSAAuthentication yes
    PubkeyAuthentication yes
systemctl reload sshd
# connect via putty ssh to someuser with peagent 
	
# https://docs.docker.com/engine/reference/commandline/login/
copy ~/.docker/config.json <from other server>	
	
### Other stuff
# sudo apt-get install iftop
sudo dpkg-reconfigure tzdata 
    # then select Europe
    # then Kiev	
	
	