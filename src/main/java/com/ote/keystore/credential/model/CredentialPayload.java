package com.ote.keystore.credential.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ote.keystore.cryptor.Cryptable;
import com.ote.keystore.cryptor.annotation.Crypted;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Data
public class CredentialPayload implements Cloneable, Cryptable {

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

    @Override
    public CredentialPayload clone() throws CloneNotSupportedException {
        return (CredentialPayload) super.clone();
    }
}
