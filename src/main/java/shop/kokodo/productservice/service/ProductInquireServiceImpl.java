package shop.kokodo.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.dto.ProductInquireRequestDto;
import shop.kokodo.productservice.dto.ProductInquireResponseDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductInquire;
import shop.kokodo.productservice.feign.UserServiceClient;
import shop.kokodo.productservice.repository.ProductInquireRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductInquireServiceImpl implements ProductInquireService{

    private final ProductInquireRepository productInquireRepository;
    private final ProductRepository productRepository;
//    private final UserServiceClient userServiceClient;

    @Override
    public List<ProductInquireResponseDto> findByProductId(long productId) {
        List<ProductInquire> inquires = productInquireRepository.findByProductId(productId);
        List<ProductInquireResponseDto> list=new ArrayList<>();

        for (ProductInquire inquire : inquires) {
            list.add(convertToDto(inquire));
        }

        return null;
    }

    @Override
    public long saveProductInquire(ProductInquireRequestDto productInquireDto) {
        return productInquireRepository.save(convertToProductInquire(productInquireDto)).getId();
    }

    @Override
    public long saveAnswer(long id, String answer) {

        ProductInquire productInquire = productInquireRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않은 문의"));

        productInquire.answerQuestion(answer);
        return productInquireRepository.save(productInquire).getId();

    }

    @Override
    public List<ProductInquireResponseDto> findNotAnswerInquire(long sellerId) {
        List<ProductInquire> inquires = productInquireRepository.findNotAnswerInquireBySellerId(sellerId);
        List<ProductInquireResponseDto> dtoList = new ArrayList<>();

        for (ProductInquire inquire : inquires) {
            dtoList.add(convertToDto(inquire));
        }
        return dtoList;
    }

    @Override
    public List<ProductInquireResponseDto> findAnswerInquire(long sellerId) {
        List<ProductInquire> inquires = productInquireRepository.findAnswerInquireBySellerId(sellerId);
        List<ProductInquireResponseDto> dtoList = new ArrayList<>();

        for (ProductInquire inquire : inquires) {
            dtoList.add(convertToDto(inquire));
        }

        return dtoList;
    }

    @Override
    public List<ProductInquire> findByMemberId(long memberId) {
        return productInquireRepository.findByMemberId(memberId);
    }

    private ProductInquire convertToProductInquire(ProductInquireRequestDto dto){
        Product product=productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품"));
        
        return ProductInquire.builder()
                .product(product)
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .memberId(dto.getMemberId())
                .build();
    }

    private ProductInquireResponseDto convertToDto(ProductInquire productInquire){
//        String memberName=userServiceClient.findMemberName(productInquire.getUserLoginId());

        String memberName="memberName";
        return ProductInquireResponseDto.builder()
                .productId(productInquire.getProduct().getId())
                .id(productInquire.getId())
                .question(productInquire.getQuestion())
                .answer(productInquire.getAnswer())
                .memberName(memberName)
                .build();
    }
}
