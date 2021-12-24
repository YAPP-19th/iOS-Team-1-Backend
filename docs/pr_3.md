#[일관성 있는 응답 DTO에 대한 Pull Request](https://github.com/YAPP-19th/iOS-Team-1-Backend/pull/3)

---
글쓴이 : [박수한](https://github.com/VIXXPARK)


PR을 보시면 아시다시피 처음에 같이 작업한 [박솔찬](https://github.com/solchan98) 님이 `ResponsEntity 객체로 StatusCode, Messagem Data(body)로 모두 응답이 가능한데 Message객체를 생성한 특별한 이유가 있나요?` 라고 질문을 했습니다.
여기에서 마지막 PR의 내용은 서로 상의해보자는 걸로 나와 있을 겁니다.

저의 의견은 일관성 있는 응답 형태를 제공해야 클라이언트에서 작업하기 좋고 만에 하나 에러가 나거나 잘못되더라도 우리가 설정한 Status가 있으면 추적하기 좋으니깐 하는 것이 좋을 것 같다고 했습니다.

솔찬님 께서는 `데이터를 바로 주는 것이 더 편하지 않을까요?` 그리고 굳이 그렇게 안해도 되지 않을까? 라는 의견을 제시해주었다.

저도 그러한 의견에 일부 동의하기도 했고 솔찬님도 저의 의견에 어느정도 일리 있는 의견이라 생각하여 클라이언트의 의견을 듣기로 결정했습니다.
클라이언트에서 나온 의견에는 대체로 제대로된 응답만 주면 상관 없다고 하는 의견도 있었지만, 어느 정도 일관성 있는 응답 형식이 있으면 다루기가 좋을 것 같다는 의견이 있었습니다.

그래서 현재 지금 사용하고 있는 응답 형태로 사용하게 되었습니다.

```java
    @Getter
    @AllArgsConstructor
    @Builder
    public static class EmailValidationResponseMessage {
        private Message message;
        private EmailValidationResponse data;

        public static EmailValidationResponseMessage of(StatusEnum status, String message, EmailValidationResponse data) {
            return EmailValidationResponseMessage.builder().message(
                    Message.builder().status(status).msg(message).build()
            ).data(data).build();
        }
    }
```

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private StatusEnum status;
    private String msg;

    public static Message of(StatusEnum status, String message){
        return Message.builder().status(status)
                                .msg(message)
                                .build();
    }

    public static Message of(String msg){
        return Message.of(StatusEnum.BAD_REQUEST,msg);
    }
}

```

일반적으로 `Message` 클래스를 has-a 상속을 하고 각 응답에 맞는 데이터를 넣어주었다.

근데 여기서도 의견이 나뉜 것이 있었습니다.

- 제네릭을 통해 간편하게 사용하자!
- 아니다! 우리는 스웨거를 통해 api 문서를 보여주는데 그런식으로 하면 제대로 나오질 않기 때문에 스웨거를 쓰는 의미가 없게 된다.

첫 번째 의견은 저의 의견이었으며, 두 번째 의견은 솔찬님 의견이었습니다.

저는 이 때 솔찬님 의견 듣고 주저없이 OKAY 라고 외쳤습니다.

생각해보니 코드 좀 더 적는 게 더 이득이라는 것을 알았습니다.

이 코드 적을 것을 노션이나 다른 곳에 적고, 또 설명하는 시간이 더 많이 걸릴 것이라는 것을 느꼈기 때문입니다.
이렇게 하나의 PR을 시작해서 많은 의견을 다루었고, 잘 마무리 했던 것 같습니다.