package com.ote.keystore.credential.payload;

import com.ote.keystore.cryptor.Cryptable;
import com.ote.keystore.cryptor.annotation.Crypted;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CredentialPayload implements Cryptable {

    private Integer id;

    @NotNull
    @Crypted
    private String login;

    @NotNull
    @Crypted
    private String password;

    @Crypted
    private String application;

    @Crypted
    private String description;

    private boolean isEncrypted;
}
