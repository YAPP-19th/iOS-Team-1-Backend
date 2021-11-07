package com.yapp.project.account.domain.oauth.apple;

import lombok.Data;
import java.util.List;
import java.util.Optional;

@Data
public class AppleKeyStorage {
    private List<AppleKey> keys;
    @Data
    public static class AppleKey {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;

    }

    public Optional<AppleKey> getMatchedKeyBy(String kid, String alg){
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst();
    }
}
