package shop.kokodo.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.ProductDetail;
import shop.kokodo.productservice.service.ProductDetailService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productDetail")
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping("/{productId}")
    public Response findByProductId(@PathVariable long productId){

        System.out.println(productId);
        List<ProductDetail> list = productDetailService.findByProductId(productId);

        return Response.success(list);
    }
}
