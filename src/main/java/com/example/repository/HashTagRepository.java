package com.example.repository;

import com.example.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository  extends JpaRepository<HashTag,String> {
}
