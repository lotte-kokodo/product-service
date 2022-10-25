package shop.kokodo.productservice.service;

import shop.kokodo.productservice.entity.ProductDetail;

import java.util.List;

public interface ProductDetailService {

    List<ProductDetail> findByProductId(long productId);
}
