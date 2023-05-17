package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    Ad findByTitle(String title);
    List<Ad> findByUserId(Long userId);
//    @Query("SELECT a FROM Ad a WHERE a.city = :city AND a.rating BETWEEN :ratingMin AND :ratingMax")
//    List<Ad> findByCityAndRating(@Param("city") String city, @Param("ratingMin") Double rating_min, @Param("ratingMax") Double rating_max);

    List<Ad> findByCityAndRatingBetween(String city, Double minRating, Double maxRating);

    List<Ad> findByCategoryId(Long categoryId);

    List<Ad> findAll(Specification<Ad> spec);

}
