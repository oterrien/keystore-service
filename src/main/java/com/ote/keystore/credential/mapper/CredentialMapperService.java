package com.ote.keystore.credential.mapper;

import com.ote.keystore.credential.payload.CredentialPayload;
import com.ote.keystore.credential.persistence.CredentialEntity;
import com.ote.keystore.trace.annotation.Traceable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialMapperService {

    @Traceable(level = Traceable.Level.TRACE)
    public CredentialPayload convert(CredentialEntity entity) {

        CredentialPayload payload = new CredentialPayload();
        payload.setId(entity.getId());
        payload.setLogin(entity.getLogin());
        payload.setPassword(entity.getPassword());
        payload.setApplication(entity.getApplication());
        payload.setDescription(entity.getDescription());
        payload.setEncrypted(entity.isEncrypted());
        return payload;
    }

    @Traceable(level = Traceable.Level.TRACE)
    public CredentialEntity convert(CredentialPayload payload) {

        CredentialEntity entity = new CredentialEntity();
        entity.setId(payload.getId());
        entity.setLogin(payload.getLogin());
        entity.setPassword(payload.getPassword());
        entity.setApplication(payload.getApplication());
        entity.setDescription(payload.getDescription());
        entity.setEncrypted(payload.isEncrypted());
        return entity;
    }

    @Traceable(level = Traceable.Level.TRACE)
    private Sort getSort(String sortingBy, String sortingDirection) {

        Sort.Order orderByPrimaryKeyAsc = new Sort.Order(Sort.Direction.ASC, "id");

        if (StringUtils.isEmpty(sortingBy)) {
            return new Sort(orderByPrimaryKeyAsc);
        }

        Sort.Direction sortingDirectionEnum = "ASC".equals(sortingDirection) ? Sort.Direction.ASC : Sort.Direction.ASC;

        Sort.Order orderByPropertyAndDirection = new Sort.Order(sortingDirectionEnum, sortingBy);

        if ("id".equalsIgnoreCase(sortingBy)) {
            return new Sort(orderByPropertyAndDirection);
        }

        return new Sort(orderByPropertyAndDirection, orderByPrimaryKeyAsc);
    }

    @Traceable(level = Traceable.Level.TRACE)
    public Pageable getPageRequest(String sortingBy, String sortingDirection, int pageSize, int pageIndex) {

        return new PageRequest(pageIndex, pageSize, getSort(sortingBy, sortingDirection));
    }

    @Traceable(level = Traceable.Level.TRACE)
    public Specification<CredentialEntity> getFilter(CredentialPayload filter) {

        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(filter.getLogin())) {
                predicates.add(criteriaBuilder.equal(root.get("login"), filter.getLogin()));
            }

            if (!StringUtils.isEmpty(filter.getPassword())) {
                predicates.add(criteriaBuilder.equal(root.get("password"), filter.getPassword()));
            }

            if (!StringUtils.isEmpty(filter.getApplication())) {
                predicates.add(criteriaBuilder.equal(root.get("application"), filter.getApplication()));
            }

            if (!StringUtils.isEmpty(filter.getDescription())) {
                predicates.add(criteriaBuilder.equal(root.get("description"), filter.getDescription()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
