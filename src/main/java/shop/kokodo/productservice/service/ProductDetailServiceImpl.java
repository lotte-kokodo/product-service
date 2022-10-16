package shop.kokodo.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.entity.ProductDetail;
import shop.kokodo.productservice.repository.ProductDetailRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService{

    private final ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> findByProductId(long productId) {
        return productDetailRepository.findByProductId(productId);
    }
}
