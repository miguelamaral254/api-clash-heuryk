package br.com.clashproject.core;


import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface BaseMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);

    // Conversões para coleções
    default List<D> toDto(Collection<E> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    default List<E> toEntity(Collection<D> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

    // Conversões para páginas
    default Page<D> toDto(Page<E> entities) {
        return entities.map(this::toDto);
    }

    default Page<E> toEntity(Page<D> dtos) {
        return dtos.map(this::toEntity);
    }

    // Atualização parcial de entidades
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(D dto, @MappingTarget E entity);
}