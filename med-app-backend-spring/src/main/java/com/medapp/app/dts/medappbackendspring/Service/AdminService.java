package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.CreateUpdateCategoryDto;
import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Entity.City;
import com.medapp.app.dts.medappbackendspring.Repository.CategoryRepository;
import com.medapp.app.dts.medappbackendspring.Repository.CityRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CityRepository cityRepository;

    /*
     *  Начало сервиса категорий
     * */

    public void createCategory(CreateUpdateCategoryDto createUpdateCategoryDto) {
        Category category = Category.builder()
                .name(createUpdateCategoryDto.getName())
                .build();
        categoryRepository.save(category);
    }

    public void updateCategoryDto(Long id, CreateUpdateCategoryDto createUpdateCategoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(null);
        if (createUpdateCategoryDto.getName() != null && !createUpdateCategoryDto.getName().trim().isEmpty()) {
            category.setName(createUpdateCategoryDto.getName());
            categoryRepository.save(category);
        }
    }

    public List<Category> getCategory(Long id, String name) {
        Specification<Category> spec = Specification.where(null);
        if (id != null) spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        if (name != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        List<Category> categories = categoryRepository.findAll(spec);
        return categories;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /*
     *  Конец сервиса категорий
     * */

    /*
     *  Начало сервиса городов
     * */

    public void createCity(City city) {
        City myCity = City.builder()
                .city_name(city.getCity_name())
                .build();
        cityRepository.save(myCity);
    }

    public void updateCity(Long id, City city) {
        City myCity = cityRepository.findById(id).orElseThrow(null);
        if (city.getCity_name() != null && !city.getCity_name().trim().isEmpty()) {
            myCity.setCity_name(city.getCity_name());
            cityRepository.save(myCity);
        }
    }

    public List<City> getCity(Long id, String name) {
        Specification<City> spec = Specification.where(null);
        if (id != null) spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        if (name != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("city_name"), name));
        List<City> cityList = cityRepository.findAll(spec);
        return cityList;
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }


    /*
     *  Конец сервиса городов
     * */

}
