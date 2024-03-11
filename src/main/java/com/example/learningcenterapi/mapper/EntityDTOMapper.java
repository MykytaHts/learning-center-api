package com.example.learningcenterapi.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EntityDTOMapper<RequestDTO, ResponseDTO, T> {
    RequestDTO toRequestDTO(T entity);
    ResponseDTO toResponseDTO(T entity);

    List<RequestDTO> toRequestDTOList(Collection<T> entityList);
    List<ResponseDTO> toResponseDTOList(Collection<T> entityList);

    Set<RequestDTO> toRequestDTOSet(Collection<T> entitySet);
    Set<ResponseDTO> toResponseDTOSet(Collection<T> entitySet);

    T fromRequestDTO(RequestDTO dto);
    T fromResponseDTO(ResponseDTO dto);

    List<T> fromRequestDTOToList(Collection<RequestDTO> dtoList);
    List<T> fromResponseDTOToList(Collection<ResponseDTO> dtoList);

    Set<T> fromRequestDTOToSet(Collection<RequestDTO> dtoSet);
    Set<T> fromResponseDTOToSet(Collection<ResponseDTO> dtoSet);
}
