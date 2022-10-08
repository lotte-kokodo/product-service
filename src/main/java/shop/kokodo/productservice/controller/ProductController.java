package shop.kokodo.productservice.controller;

import feign.Param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.CategoryService;
import shop.kokodo.productservice.service.ProductService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;

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

    @DeleteMapping("/delete/{productId}")
    public Response productDelete(@PathVariable("productId") long id){
        productService.deleteProduct(id);
        return Response.success();
    }

    @GetMapping("/productId/{productId}")
    public Response findById(@PathVariable("productId") long id) {
        Product product = productService.findById(id);

        if(product == null){
            return Response.failure(0,"해당 상품이 존재하지 않습니다.");
        }

        ProductDto productDto = ProductDto.builder().id(product.getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .price(product.getPrice())
                .displayName(product.getDisplayName())
                .stock(product.getStock())
                .deadline(product.getDeadline())
                .thumbnail(product.getThumbnail())
                .sellerId(product.getSellerId())
                .deliveryFee(product.getDeliveryFee())
                .build();

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

    @GetMapping("/detail/{productId}")
    public Response productDetail(@PathVariable long productId){
        return Response.success(productService.findProductDetail(productId));
    }

    @GetMapping
    public Response findBy(@Param String productName, @Param Integer status
            , @Param String startDate, @Param String endDate){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Product> list =productService.findBy(productName,status, LocalDateTime.parse(startDate, formatter),LocalDateTime.parse(endDate, formatter));

        return Response.success(list);
    }

}