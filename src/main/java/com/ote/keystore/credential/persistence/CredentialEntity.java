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
public class CredentialEntity implements Cloneable, Cryptable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")
    @TableGenerator(name = "tab", initialValue = 0, allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LOGIN")
    @Crypted
    private String login;

    @Column(name = "PASSWORD")
    @Crypted
    private String password;

    @Column(name = "APPLICATION")
    @Crypted
    private String application;

    @Column(name = "DESCRIPTION")
    @Crypted
    private String description;

    @Column(name = "IS_ENCRYPTED")
    private boolean isEncrypted;

    @Override
    public CredentialEntity clone() throws CloneNotSupportedException {
        return (CredentialEntity) super.clone();
    }
}
