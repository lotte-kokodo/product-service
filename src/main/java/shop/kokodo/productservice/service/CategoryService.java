package shop.kokodo.productservice.service;

import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public List<CategoryDto> findAll();
    public List<CategoryDto> findByName(String name);
}
