sudo docker rm -f app
sudo docker pull yapp19th/develop
sudo docker run --name app -d -p 8000:8000 yapp19th/develop
sudo docker rmi -f $(sudo docker images -f "dangling=true" -q)
