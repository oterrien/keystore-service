package com.ote.keystore.credential.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Integer>, JpaSpecificationExecutor<CredentialEntity> {
}
