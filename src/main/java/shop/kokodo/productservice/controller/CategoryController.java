package shop.kokodo.productservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.CategoryService;
import shop.kokodo.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public Response save(@RequestBody CategoryDto categoryDto) {
        categoryService.save(categoryDto);
        return Response.success();
    }

    @GetMapping("/all")
    public Response all() {
        List<CategoryDto> categoryList = categoryService.findAll();
        return Response.success(categoryList);
    }

    @GetMapping("categoryName/{name}")
    public Response findByName(@PathVariable("name") String name){
        System.out.println(name);
        List<CategoryDto> categoryList = categoryService.findByName(name);
        return Response.success(categoryList);
    }


}
