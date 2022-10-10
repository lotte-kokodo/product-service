package shop.kokodo.productservice.feign.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.feign.response.FeignResponse;
import shop.kokodo.productservice.feign.response.FeignResponse.ProductOfCart;
import shop.kokodo.productservice.feign.response.FeignResponse.Stock;
import shop.kokodo.productservice.feign.service.interfaces.ProductFeignService;

@RestController
@RequestMapping("/products/feign")
public class ProductFeignController {

    private final ProductFeignService productFeignService;

    @Autowired
    public ProductFeignController(
        ProductFeignService productFeignService) {
        this.productFeignService = productFeignService;
    }

    @GetMapping("/unitPrice/{productId}")
    public FeignResponse.Price getProductPrice(@PathVariable Long productId) {

        return productFeignService.getProductPrice(productId);
    }

    @GetMapping("/unitPrice")
    public List<FeignResponse.Price> getProductsPrice(@RequestParam List<Long> productIds) {

        return productFeignService.getProductPrices(productIds);
    }

    @GetMapping("/cart")
    public Map<Long, ProductOfCart> getCartProducts(@RequestParam List<Long> productIds) {

        List<ProductOfCart> productOfCarts = productFeignService.getCartProducts(productIds);

        return productOfCarts.stream()
            .collect(Collectors.toMap(ProductOfCart::getId, Function.identity()));
    }

    @GetMapping("/stock/{productId}")
    public FeignResponse.Stock getProductStock(@PathVariable Long productId) {
        return productFeignService.getProductStock(productId);
    }


}
