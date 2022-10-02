package shop.kokodo.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.feign.ProductPriceResponse;
import shop.kokodo.productservice.service.interfaces.ProductFeignService;

@RestController
@RequestMapping("/product-service")
public class ProductFeignController {

    private final ProductFeignService productFeignService;

    public ProductFeignController(
        ProductFeignService productFeignService) {
        this.productFeignService = productFeignService;
    }

    @GetMapping("/products/{productId}/unit-price")
    public ProductPriceResponse getProductUnitPrice(@PathVariable Long productId) {

        return null;
    }


}
