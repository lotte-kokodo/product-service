package shop.kokodo.productservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.feign.FeignResponse;
import shop.kokodo.productservice.service.interfaces.ProductFeignService;

@RestController
@RequestMapping("/product-service")
public class ProductFeignController {

    private final ProductFeignService productFeignService;

    @Autowired
    public ProductFeignController(
        ProductFeignService productFeignService) {
        this.productFeignService = productFeignService;
    }

    @GetMapping("/unit-price/{productId}")
    public FeignResponse.Price getProductPrice(@PathVariable Long productId) {

        return productFeignService.getProductPrice(productId);
    }

    @GetMapping("/unit-price")
    public List<FeignResponse.Price> getProductsPrice(@RequestParam List<Long> productIds) {

        return productFeignService.getProductPrices(productIds);
    }

}
