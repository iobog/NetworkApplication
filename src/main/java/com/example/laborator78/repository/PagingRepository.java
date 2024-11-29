package com.example.laborator78.repository;

import com.example.laborator78.domain.Entity;
import com.example.laborator78.domain.pagining.Page;
import com.example.laborator78.domain.pagining.Pageable;



public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Long id , Pageable pageable);
}