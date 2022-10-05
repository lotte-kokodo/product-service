package shop.kokodo.productservice.service;

import shop.kokodo.productservice.dto.ProductInquireRequestDto;
import shop.kokodo.productservice.dto.ProductInquireResponseDto;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductInquireService {

    public List<ProductInquireResponseDto> findByProductId(long productId);
    public long saveProductInquire(ProductInquireRequestDto productInquireDto);
    public long saveAnswer(long id, String answer);

    public List<ProductInquireResponseDto> findNotAnswerInquire(long sellerId);
    public List<ProductInquireResponseDto> findAnswerInquire(long sellerId);
}
