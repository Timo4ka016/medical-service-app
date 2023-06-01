package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.FavoriteAd;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteAdRepository extends JpaRepository<FavoriteAd, Long> {
    List<FavoriteAd> findByUser(User user);
    FavoriteAd findByUserAndAd(User currentUser, Ad ad);
}
