package com.ote.keystore.credential;

import com.ote.keystore.JpaRepositoryDefaultImpl;
import com.ote.keystore.credential.persistence.CredentialEntity;
import com.ote.keystore.credential.persistence.CredentialRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Profile("CredentialRepositoryMock")
@Repository
@Primary
public class CredentialRepositoryMock extends JpaRepositoryDefaultImpl<CredentialEntity, Integer> implements CredentialRepository {

    private final List<CredentialEntity> repositoryMock = new ArrayList<>();

    @Override
    public boolean exists(Integer id) {
        return repositoryMock.stream().
                filter(p -> p.getId().equals(id)).
                map(p -> true).
                findAny().
                orElse(false);
    }

    @Override
    public CredentialEntity findOne(Integer id) {
        return repositoryMock.stream().
                filter(p -> p.getId().equals(id)).
                findAny().
                orElse(null);
    }

    @Override
    public <S extends CredentialEntity> S save(S s) {
        if (exists(s.getId())) {
            delete(s.getId());
        }

        if (s.getId() == null || s.getId() < 0) {
            Integer maxId = repositoryMock.stream().
                    map(CredentialEntity::getId).
                    max(Integer::compareTo).
                    orElse(-1);
            s.setId(maxId + 1);
        }

        repositoryMock.add(s);
        return s;
    }

    @Override
    public void delete(Integer id) {
        repositoryMock.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public void deleteInBatch(Iterable<CredentialEntity> iterable) {
        iterable.forEach(p -> repositoryMock.removeIf(p1 -> p1.getId().equals(p.getId())));
    }

    @Override
    public void deleteAll() {
        repositoryMock.clear();
    }
}