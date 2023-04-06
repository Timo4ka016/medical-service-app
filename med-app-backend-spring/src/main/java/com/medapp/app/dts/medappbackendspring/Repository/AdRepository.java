package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    Ad findByTitle(String title);

}
