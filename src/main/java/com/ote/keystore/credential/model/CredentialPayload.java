package com.ote.keystore.credential.model;

import com.ote.keystore.cryptor.annotation.Crypted;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CredentialPayload implements Cloneable {

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

    @Override
    public CredentialPayload clone() throws CloneNotSupportedException {
        return (CredentialPayload) super.clone();
    }
}
