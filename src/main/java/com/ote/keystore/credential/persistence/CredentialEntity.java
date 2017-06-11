package com.ote.keystore.credential.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "T_CREDENTIAL")
@Data
@NoArgsConstructor
public class CredentialEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public Integer id;

    @Column(name = "LOGIN")
    public String login;

    @Column(name = "PASSWORD")
    public String password;

    @Column(name = "APPLICATION")
    public String application;

    @Column(name = "DESCRIPTION")
    public String description;
}
