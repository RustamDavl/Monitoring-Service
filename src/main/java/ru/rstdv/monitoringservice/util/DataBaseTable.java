package ru.rstdv.monitoringservice.util;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * класс предоставляет элементарные операции базы данных. будет удален при внедрении в проект JDBC API.
 *
 * @param <T> представляет определенную сущность, которая сохраняется в базе данных
 * @author RustamD
 * @version 1.0
 */
@Deprecated
public class DataBaseTable<T> {

    @Getter
    @Setter
    private Long sequence = 1L;

    private final Map<Long, T> fields = new HashMap<>();

    private Map<Long, T> fields() {
        return fields;
    }

    public T FIND_BY_ID(Long id) {
        return fields.get(id);
    }

    public T INSERT(T entity) {
        fields.put(sequence, entity);

        var entityWithId = assignId(entity);

        iterateSequence();

        return entityWithId;
    }

    public List<T> GET_ALL() {
        return fields.values().stream().toList();
    }

    private void iterateSequence() {
        sequence++;
    }

    private T assignId(T entity) {
        Field[] fields1 = entity.getClass().getDeclaredFields();
        return Arrays.stream(fields1)
                .filter(
                        field -> field.getName().equals("id")
                )
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(entity, sequence);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return entity;
                }).toList()
                .get(0);
    }

    public void clear() {
        sequence = 1L;
        fields.clear();
    }

}
