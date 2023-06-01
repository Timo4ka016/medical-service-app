package com.medapp.app.dts.medappbackendspring.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long price;
    private String address;
    private String city;
    private Double rating;

    @OneToMany(mappedBy = "ad")
    private Set<FavoriteAd> favoriteAds;
    @ManyToOne
    private User user;
    @ManyToOne
    private Category category;

}
