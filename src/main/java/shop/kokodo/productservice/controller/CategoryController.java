package shop.kokodo.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity all() {
        List<CategoryDto> categoryList = categoryService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }

    @GetMapping("/categoryName/{name}")
    public Response findByName(@PathVariable("name") String name){
        System.out.println(name);
        List<CategoryDto> categoryList = categoryService.findByName(name);
        return Response.success(categoryList);

    }


}
