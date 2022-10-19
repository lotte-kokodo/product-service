package shop.kokodo.productservice.service.interfaces;

import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public void save(CategoryDto categoryDto);
    public List<CategoryDto> findAll();
    public List<CategoryDto> findByName(String name);
}
