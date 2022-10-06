package shop.kokodo.productservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.ProductResponse;
import shop.kokodo.productservice.dto.ProductResponse.GetOrderProduct;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.interfaces.CategoryService;
import shop.kokodo.productservice.service.interfaces.ProductService;

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

    // 주문서 상품 정보 요청 API
    @GetMapping("/orderProducts")
    public Response getOrderProducts(@RequestParam List<Long> productIds) {
        List<Product> products = productService.getOrderProducts(productIds);

        List<ProductResponse.GetOrderProduct> orderProducts = products.stream()
            .map(product ->
                GetOrderProduct.builder()
                    .id(product.getId())
                    .thumbnail(product.getThumbnail())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build())
            .collect(Collectors.toList());

        return Response.success(orderProducts);
    }
}
