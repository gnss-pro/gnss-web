package com.gnss.web.common.service;

import com.gnss.web.common.dao.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>Description: 基础服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseService<T> {

    @Autowired
    private BaseRepository<T> baseRepository;

    public <S extends T> S save(S entity) {
        return baseRepository.save(entity);
    }

    public <S extends T> List<S> save(Iterable<S> entities) {
        return baseRepository.saveAll(entities);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) {
        return baseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<T> findOne(Specification<T> spec) {
        return baseRepository.findOne(spec);
    }

    public Boolean deleteById(Long id) {
        return findById(id).map(entity -> {
            baseRepository.delete(entity);
            return Boolean.TRUE;
        }).orElse(Boolean.FALSE);
    }

    @Transactional(readOnly = true)
    public long count() {
        return baseRepository.count();
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return baseRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public List<T> findAll(Specification<T> spec) {
        return baseRepository.findAll(spec);
    }
}
