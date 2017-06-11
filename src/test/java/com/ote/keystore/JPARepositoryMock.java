package com.ote.keystore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

public class JPARepositoryMock<TE, TK extends Serializable> implements JpaRepository<TE, TK>, JpaSpecificationExecutor<TE> {

    @Override
    public List<TE> findAll() {
        return null;
    }

    @Override
    public List<TE> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<TE> findAll(Iterable<TK> iterable) {
        return null;
    }

    @Override
    public <S extends TE> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends TE> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<TE> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TE getOne(TK tk) {
        return null;
    }

    @Override
    public <S extends TE> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TE> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public TE findOne(Specification<TE> specification) {
        return null;
    }

    @Override
    public List<TE> findAll(Specification<TE> specification) {
        return null;
    }

    @Override
    public Page<TE> findAll(Specification<TE> specification, Pageable pageable) {
        return null;
    }

    @Override
    public List<TE> findAll(Specification<TE> specification, Sort sort) {
        return null;
    }

    @Override
    public long count(Specification<TE> specification) {
        return 0;
    }

    @Override
    public Page<TE> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TE> S save(S s) {
        return null;
    }

    @Override
    public TE findOne(TK tk) {
        return null;
    }

    @Override
    public boolean exists(TK tk) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(TK tk) {

    }

    @Override
    public void delete(TE te) {

    }

    @Override
    public void delete(Iterable<? extends TE> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends TE> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TE> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TE> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TE> boolean exists(Example<S> example) {
        return false;
    }
}
