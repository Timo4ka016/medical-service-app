package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.CreateUpdateCategoryDto;
import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public void createCategory(
            @RequestBody CreateUpdateCategoryDto createUpdateCategoryDto) {
        categoryService.createCategory(createUpdateCategoryDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public void updateCategory(
            @RequestParam Long id,
            @RequestBody CreateUpdateCategoryDto createUpdateCategoryDto
    ) {
        categoryService.updateCategoryDto(id, createUpdateCategoryDto);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public List<Category> findCategory(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name
    ) {
        return categoryService.getCategory(id, name);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public void deleteCategory(
            @RequestParam Long id
    ) {
        categoryService.deleteCategory(id);
    }
}
