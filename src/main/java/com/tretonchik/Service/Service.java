package com.tretonchik.Service;

import com.tretonchik.models.Model;

import java.util.List;

public interface Service<T extends Model<U>, U> {
    List<T> findAll();
    T findById(U id);
    void save(T model);
    void update(T model);
    void delete(T model);
    void deleteById(U id);
    <V> List<T> findByColumn(String columnName, V columnValue);
    <V> T findByColumnUnique(String columnName, V columnValue);
}
