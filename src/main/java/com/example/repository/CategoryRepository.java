package com.example.repository;

import com.example.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
  List<Category> findByType(String type);
  List<Category> findByName(String id);
  List<Category> findByCreatedDateBetween(Date startDateTime, Date endDateTime);

}
