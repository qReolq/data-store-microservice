package qreol.project.datastoremicroservice.web.mapper;

import java.util.List;

public interface Mappable<E, D> {

    E toEntity(D dto);
    List<E> toEntity(List<D> listDto);

    D toDto(E entity);
    List<D> toDto(List<E> listEntity);

}
