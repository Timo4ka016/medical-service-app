package com.medapp.app.dts.medappbackendspring.Service;


import com.medapp.app.dts.medappbackendspring.Dto.CreateUpdateCategoryDto;
import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.CategoryRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

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

    public void addCategoryToDoctor(User user, List<Long> categoryIds) {
        User myUser = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        Set<Category> categoriesToAdd = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
            categoriesToAdd.add(category);
        }

        myUser.getCategories().addAll(categoriesToAdd);
        userRepository.save(user);
    }

    public void removeCategoryFromDoctor(User user, List<Long> categoryIds) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Set<Category> categoriesToRemove = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
            categoriesToRemove.add(category);
        }

        myUser.getCategories().removeAll(categoriesToRemove);
        userRepository.save(myUser);
    }

}


