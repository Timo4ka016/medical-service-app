package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteAdDto {
    private Long id;
    private AdShortInfo ad;
}
