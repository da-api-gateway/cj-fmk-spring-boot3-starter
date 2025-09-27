package com.cjlabs.db.mp.mp;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 基础服务接口 - 只暴露封装的方法，不暴露MyBatis Plus原生方法
 */
public interface IFmkService<T> {

    int saveService(T entity);

    int saveBatchService(Collection<T> entityList);
    
    int updateByIdService(T entity);

    int removeByIdService(Serializable id);
    
    T getByIdService(Serializable id);
    
    Optional<T> getByIdOpService(Serializable id);
    
    List<T> listAllLimitService();
    
}