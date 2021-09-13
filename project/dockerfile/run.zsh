#!/bin/bash



while true
do
cat << _EOF_
***************************

1. Docker-compose up -d (실행하기) <== 주로 이것만 사용
2. find ipAddress <== 처음에만
3. redis 접속하기
0. quit
***************************
_EOF_
read -p "[0-3] 사이의 숫자를 입력해주세요."
if [[ $REPLY =~ ^[0-3]$ ]]; then
  case $REPLY in
    1)
      docker-compose up -d
      ;;
    2)
       docker inspect postgres-yapp | grep -i ipaddress
       ;;
    3)
      docker exec -it   yapp_redis /bin/bash
      ;;
    0)
       echo "사용해주셔서 감사합니다.:)"
       break
    esac
else
  echo "올바른 값을 입력해주세요"ㅎ
fi
done



