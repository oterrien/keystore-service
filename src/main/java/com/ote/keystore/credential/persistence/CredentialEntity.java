package com.ote.keystore.credential.persistence;

import com.ote.keystore.cryptor.Cryptable;
import com.ote.keystore.cryptor.annotation.Crypted;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "T_CREDENTIAL")
@Data
@NoArgsConstructor
public class CredentialEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")
    @TableGenerator(name = "tab", initialValue = 0, allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LOGIN", length = 32)
    private String login;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "APPLICATION")
    private String application;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;
}
