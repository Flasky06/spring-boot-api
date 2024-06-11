package com.tritva.restapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tritva.restapi.domain.BookEntity;

@Repository
public interface  BookRepository extends JpaRepository<BookEntity, String>{
    
}
