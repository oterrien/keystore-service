package com.ote.keystore.credential.persistence;

import com.ote.keystore.cryptor.annotation.Encrypt;
import com.ote.keystore.merger.BeanMergerService;
import com.ote.keystore.trace.annotation.Traceable;
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

    //region read
    @Transactional(readOnly = true)
    @Traceable(level = Traceable.Level.TRACE)
    public long count() {
        return credentialRepository.count();
    }

    @Transactional(readOnly = true)
    @Traceable(level = Traceable.Level.TRACE)
    public long count(Specification<CredentialEntity> filter) {
        return credentialRepository.count(filter);
    }

    @Transactional(readOnly = true)
    @Traceable(level = Traceable.Level.TRACE)
    public boolean exists(Integer id) {
        return credentialRepository.exists(id);
    }

    @Transactional(readOnly = true)
    @Traceable(level = Traceable.Level.TRACE)
    public CredentialEntity find(Integer id) {
        CredentialEntity entity = credentialRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException(id);
        }
        return entity;
    }

    @Transactional(readOnly = true)
    @Traceable(level = Traceable.Level.TRACE)
    public Page<CredentialEntity> find(Specification<CredentialEntity> filter, Pageable pageRequest) {
        return credentialRepository.findAll(filter, pageRequest);
    }
    //endregion

    // region create
    @Traceable(level = Traceable.Level.TRACE)
    public CredentialEntity create(@Encrypt(secretKey = "#secretKey") CredentialEntity entity, String secretKey) {
        entity.setId(null);
        return credentialRepository.save(entity);
    }
    //endregion

    //region update
    @Traceable(level = Traceable.Level.TRACE)
    public CredentialEntity reset(Integer id, @Encrypt(secretKey = "#secretKey") CredentialEntity entity, String secretKey) {

        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        entity.setId(id);
        return credentialRepository.save(entity);
    }

    @Traceable(level = Traceable.Level.TRACE)
    public CredentialEntity merge(Integer id, @Encrypt(secretKey = "#secretKey") CredentialEntity partialEntity, String secretKey) {

        CredentialEntity entity = find(id);
        if (entity == null) {
            throw new NotFoundException(id);
        }

        // Copy non null properties from partialEntity to person
        try {
            beanMergerService.copyNonNullProperties(partialEntity, entity);
        } catch (Exception e) {
            throw new NotMergeableException(id, e);
        }

        return credentialRepository.save(entity);
    }
    //endregion

    //region delete
    @Traceable(level = Traceable.Level.TRACE)
    public void delete(Integer id) {
        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        credentialRepository.delete(id);
    }

    @Traceable(level = Traceable.Level.TRACE)
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
