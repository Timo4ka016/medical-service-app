package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.City;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findAll(Specification<City> spec);
}
