package com.ote.keystore.credential.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CredentialPayload {

    private Integer id;

    @NotNull
    private String login;

    @NotNull
    private String password;

    private String application;

    private String description;
}
