package com.ote.keystore.credential;

import com.ote.keystore.credential.persistence.CredentialEntity;
import com.ote.keystore.credential.persistence.CredentialPersistenceService;
import com.ote.keystore.merger.BeanMergerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Profile("CredentialPersistenceServiceMock")
@Service
@Slf4j
@Primary
public class CredentialPersistenceServiceMock extends CredentialPersistenceService {

    private final List<CredentialEntity> repositoryMock = new ArrayList<>();

    @Autowired
    private BeanMergerService beanMergerService;

    public void cleanRepositoryMock() {
        repositoryMock.clear();
    }

    @Override
    public boolean exists(Integer id) {
        return repositoryMock.stream().
                filter(p -> p.getId().equals(id)).
                map(p -> true).
                findAny().
                orElse(false);
    }

    @Override
    public CredentialEntity find(Integer id) {
        return repositoryMock.stream().
                filter(p -> p.getId().equals(id)).
                findAny().
                orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public Page<CredentialEntity> find(Specification<CredentialEntity> filter, Pageable pageRequest) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public CredentialEntity create(CredentialEntity entity) {
        Integer maxId = repositoryMock.stream().
                map(CredentialEntity::getId).
                max(Integer::compareTo).
                orElse(-1);
        entity.setId(maxId + 1);
        repositoryMock.add(entity);
        return entity;
    }

    @Override
    public CredentialEntity reset(Integer id, CredentialEntity entity) {
        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        entity.setId(id);
        repositoryMock.stream().filter(p -> p.getId().equals(id)).forEach(p -> p = entity);
        return entity;
    }

    @Override
    public CredentialEntity merge(Integer id, CredentialEntity partialEntity) {
        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        return repositoryMock.stream().filter(p -> p.getId().equals(id)).peek(p -> {
            try {
                beanMergerService.copyNonNullProperties(partialEntity, p);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).findAny().get();
    }

    @Override
    public void delete(Integer id) {
        if (!exists(id)) {
            throw new NotFoundException(id);
        }
        repositoryMock.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public void delete(Specification<CredentialEntity> filter) {
        //TODO
        throw new NotImplementedException();
    }
}
