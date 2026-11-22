package kz.platonus.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    // Объектіні сақтау
    void save(T entity);

    // ID арқылы табу
    Optional<T> findById(ID id);

    // Барлық объектілерді алу
    List<T> findAll();

    // Объектіні жаңарту
    void update(T entity);

    // Объектіні өшіру
    void deleteById(ID id);

    // Объектілер санын есептеу
    long count();
}