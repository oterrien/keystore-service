package com.ote.keystore.credential.persistence;

import com.ote.keystore.cryptor.CryptorService;
import com.ote.keystore.merger.BeanMergerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
public class CredentialPersistenceService {

    @Autowired
    private BeanMergerService beanMergerService;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private CryptorService cryptorService;

    //region read
    @Transactional(readOnly = true)
    public long count() {
        return credentialRepository.count();
    }

    @Transactional(readOnly = true)
    public long count(Specification<CredentialEntity> filter) {
        return credentialRepository.count(filter);
    }

    @Transactional(readOnly = true)
    public boolean exists(Integer id) {
        return credentialRepository.exists(id);
    }

    @Transactional(readOnly = true)
    public CredentialEntity find(Integer id) {
        CredentialEntity entity = credentialRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException(id);
        }
        return entity;
    }

    @Transactional(readOnly = true)
    public Page<CredentialEntity> find(Specification<CredentialEntity> filter, Pageable pageRequest) {
        return credentialRepository.findAll(filter, pageRequest);
    }
    //endregion

    // region create
    public CredentialEntity create(CredentialEntity entity, String secretKey) {
        entity.setId(null);
        entity = cryptorService.encrypt(secretKey, entity);
        return credentialRepository.save(entity);
    }
    //endregion

    //region update
    public CredentialEntity reset(Integer id, CredentialEntity entity, String secretKey) {

        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        entity.setId(id);
        entity = cryptorService.encrypt(secretKey, entity);
        return credentialRepository.save(entity);
    }

    public CredentialEntity merge(Integer id, CredentialEntity partialEntity, String secretKey) {

        CredentialEntity entity = find(id);
        if (entity == null) {
            throw new NotFoundException(id);
        }

        // Copy non null properties from partialEntity to person
        try {
            partialEntity = cryptorService.encrypt(secretKey, partialEntity);
            beanMergerService.copyNonNullProperties(partialEntity, entity);
        } catch (Exception e) {
            throw new NotMergeableException(id, e);
        }

        return credentialRepository.save(entity);
    }
    //endregion

    //region delete
    public void delete(Integer id) {
        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        credentialRepository.delete(id);
    }

    public void delete(Specification<CredentialEntity> filter) {
        credentialRepository.deleteInBatch(credentialRepository.findAll(filter));
    }
    //endregion

    //region exceptions
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(Integer id) {
            super("Unable to find user with id " + id);
        }
    }

    public static class NotMergeableException extends RuntimeException {
        public NotMergeableException(Integer id, Exception e) {
            super("Unable to find user with id " + id, e);
        }
    }
    //endregion
}
