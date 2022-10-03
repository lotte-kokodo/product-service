package shop.kokodo.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.CategoryService;
import shop.kokodo.productservice.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public Response save(@RequestBody ProductDto productDto) {
        productService.saveProduct(productDto);
        return Response.success();
    }

    @PutMapping("/update")
    public Response update(@RequestBody ProductDto productDto){
        productService.updateProduct(productDto);
        return Response.success();
    }

    @DeleteMapping("/delete")
    public Response delete(@RequestBody ProductDto productDto){
        productService.deleteProduct(productDto.getId());
        return Response.success();
    }

    @GetMapping("/productId/{productId}")
    public Response findById(@PathVariable("productId") long id) {
        Product product = productService.findById(id);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(product,ProductDto.class);
        return Response.success(productDto);
    }

    @GetMapping("/productAll")
    public Response findAll() {
        return Response.success(productService.findAll());
    }

    @GetMapping("/categoryId/{categoryId}")
    public Response productByCategory(@PathVariable("categoryId") long categoryId){
        return Response.success(productService.findProductByCategory(categoryId));
    }

    @GetMapping("/totalSearch/{totalSearch}")
    public Response productByTotalSearch(@PathVariable("totalSearch") String totalSearch){
        return Response.success(productService.findProductByTotalSearch(totalSearch));
    }

    @GetMapping("/categorySearch/{categoryId}/{displayName}")
    public Response productByCategorySearch(@PathVariable("categoryId") long categoryId,
                                            @PathVariable("displayName") String displayName) {
        return Response.success(productService.findProductByCategorySearch(categoryId, displayName));
    }






}
