package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll(Specification<Category> spec);
}
