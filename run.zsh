sudo docker rm -f app
sudo docker pull yapp19th/develop
sudo docker run --name app -d -v /home/image:/home/image -p 8000:8000 --net devsol0427_minning_network yapp19th/develop
sudo docker rmi -f $(sudo docker images -f "dangling=true" -q)
