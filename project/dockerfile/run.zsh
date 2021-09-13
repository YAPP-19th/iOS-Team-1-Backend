#!/bin/bash



while true
do
cat << _EOF_
***************************
1. Build image < docker build -t postgres-ko:13.0 . <== 처음에만
2. Docker-compose up -d (실행하기) <== 주로 이것만 사용
3. find ipAddress <== 처음에만
0. quit
***************************
_EOF_
read -p "[0-3] 사이의 숫자를 입력해주세요."
if [[ $REPLY =~ ^[0-3]$ ]]; then
  case $REPLY in
    1)
      docker build -t postgres-ko:13.0 .
      ;;
    2)
      docker-compose up -d
      ;;
    3)
       docker inspect postgres-yapp | grep -i ipaddress
       ;;
     0)
       echo "사용해주셔서 감사합니다.:)"
       break
    esac
else
  echo "올바른 값을 입력해주세요"ㅎ
fi
done



