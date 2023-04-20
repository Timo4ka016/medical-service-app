package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.CreateUpdateCategoryDto;
import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Entity.City;
import com.medapp.app.dts.medappbackendspring.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('USER_ADMIN')")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/category/create")
    public void createCategory(
            @RequestBody CreateUpdateCategoryDto createUpdateCategoryDto) {
        adminService.createCategory(createUpdateCategoryDto);
    }

    @PutMapping("/category/update")

    public void updateCategory(
            @RequestParam Long id,
            @RequestBody CreateUpdateCategoryDto createUpdateCategoryDto
    ) {
        adminService.updateCategoryDto(id, createUpdateCategoryDto);
    }

    @GetMapping("/category/get")
    public List<Category> findCategory(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name
    ) {
        return adminService.getCategory(id, name);
    }

    @DeleteMapping("/category/delete")

    public void deleteCategory(
            @RequestParam Long id
    ) {
        adminService.deleteCategory(id);
    }

    @PostMapping("/city/create")
    public void createCity(@RequestBody City city) {
        adminService.createCity(city);
    }

    @PutMapping("/city/update")
    public void updateCity(
            @RequestParam Long id,
            @RequestBody City city) {
        adminService.updateCity(id, city);
    }

    @GetMapping("/city/get")
    public List<City> findCity(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name
    ) {
        return adminService.getCity(id, name);
    }

    @DeleteMapping("/city/delete")
    public void deleteCity(
            @RequestParam Long id
    ) {
        adminService.deleteCity(id);
    }

}
