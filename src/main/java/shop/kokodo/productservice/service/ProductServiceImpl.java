package shop.kokodo.productservice.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.circuitbreaker.AllCircuitBreaker;

import shop.kokodo.productservice.dto.ProductAndProductDetailDto;
import shop.kokodo.productservice.dto.ProductDetailDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;
import shop.kokodo.productservice.exception.NoSellerServiceException;
import shop.kokodo.productservice.feign.SellerServiceClient;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductCustomRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCustomRepository productCustomRepository;
    private final SellerServiceClient sellerServiceClient;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ProductCustomRepository productCustomRepository, SellerServiceClient sellerServiceClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productCustomRepository = productCustomRepository;
        this.sellerServiceClient = sellerServiceClient;
    }

    private CircuitBreaker circuitBreaker = AllCircuitBreaker.createSellerCircuitBreaker();

    /* 상품삭제 */
    @Transactional
    @Override
    public void deleteProduct(long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        category.getProductList().remove(product);
        categoryRepository.save(category);
        productRepository.deleteById(product.getId());
    }

    /* 단일 상품, 전체 상품 조회 */
    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }
    @Override
    public List<ProductDto> findAll() {
        return returnProductDtoList(productRepository.findAll());
    }

    /* 카테고리 별 상품 */
    @Override
    public Page<Product> findProductByCategory(long categoryId, int page) {
        Pageable pageable = PageRequest.of(page,20);
        return productRepository.findProductByCategory(categoryId, pageable);
    }

    /* ===메인화면=== */
    /* 신상품 */
    @Override
    public Page<Product> findProductByNew(int page) {
        LocalDate now = LocalDate.now();
        String month = monthStr(now.getMonthValue());
        String lastMonth = monthStr(now.minusMonths(1).getMonthValue());

        Pageable pageable = PageRequest.of(page,20);
        return productRepository.findProductByNew(month,lastMonth,pageable);
    }

    /* 타임 세일 상품 */
    @Override
    public Page<Product> findProductBySale(int page) {
        LocalDate now = LocalDate.now();
        int dayOfMonth = now.getDayOfMonth();
        String nextMonth = monthStr(now.plusMonths(1).getMonthValue());
        String month = monthStr(now.getMonthValue());
        String day = dayOfMonth < 10 ? "0" + (dayOfMonth) : Integer.toString(dayOfMonth);

        Pageable pageable = PageRequest.of(page,20);
        return productRepository.findProductBySale(nextMonth,month,day,pageable);
    }

    /* MD추천 */
    @Override
    public Page<Product> findProductBySeller(int page) {
        Pageable pageable = PageRequest.of(page,20);
        return productRepository.findProductBySeller(pageable);
    }

    /* ===전체 상품 검색===*/
    @Override
    public Page<Product> findProductByTotalSearch(String productDisplayName, int page) {
        Pageable pageable = PageRequest.of(page,20);
        return productRepository.findProductByTotalSearch(productDisplayName,pageable);
    }

    /* Feign Client */
    // TODO: detail 이미지 template 부분 추가
    public ProductAndProductDetailDto findProductDetail(long productId){
        Product product = productRepository.findById(productId).get();
//                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 상품"));
        return convertToProductAndProductDetailDto(product);
    }

    @Override
    public List<ProductDto> findBy(String name, Integer status, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                   Long sellerId,int page) {

            return productCustomRepository.findProduct(name,status,startDateTime,endDateTime,sellerId, PageRequest.of(page,10));
    }

    @Override
    public List<Long> getProductSellerId(List<Long> productId) {
        List<Long> sellerIdList = new ArrayList<>();
        for (Long pId : productId) {
            sellerIdList.add(productRepository.findSellerIdByProductId(pId));
        }
        return sellerIdList;
    }

    @Override
    public List<ProductDto> findBySellerId(Long sellerId) {


        Boolean sellerValid = circuitBreaker.run(()->sellerServiceClient.getSeller(sellerId),throwable -> false);

        if(!sellerValid) throw new NoSellerServiceException();
        return returnProductDtoList(productRepository.findBySellerId(sellerId));
    }

    @Override
    public Optional<Product> findProductOpById(Long productId) {
        return productRepository.findById(productId);
    }

    public ProductAndProductDetailDto convertToProductAndProductDetailDto(Product product){

        List<ProductDetail> detailList = product.getProductDetailList();
        return ProductAndProductDetailDto.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .productDetailList(convertToProductDetailDto(detailList))
                .name(product.getName())
                .price(product.getPrice())
                .displayName(product.getDisplayName())
                .stock(product.getStock())
                .deadline(product.getDeadline())
                .thumbnail(product.getThumbnail())
                .sellerId(product.getSellerId())
                .deliveryFee(product.getDeliveryFee())
                .build();
    }

    public String monthStr (int monthValue) {
        return monthValue < 10 ? "0"+monthValue : Integer.toString(monthValue);
    }

    public List<ProductDetailDto> convertToProductDetailDto(List<ProductDetail> detail){
            List<ProductDetailDto> detailDtos = new ArrayList<>();
        for (ProductDetail productDetail : detail) {
            detailDtos.add(ProductDetailDto.builder()
                            .id(productDetail.getId())
                            .image(productDetail.getImage())
                            .orders(productDetail.getOrders())
                    .build());
        }

        return detailDtos;
    }

    public List<ProductDto> returnProductDtoList (List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();

        for(Product p : productList){
            ProductDto productDto = new ProductDto(p.getId(),p.getCategory().getId(),
                    p.getName(),p.getPrice(),p.getDisplayName(),
                    p.getStock(),p.getDeadline(),p.getThumbnail(),
                    p.getSellerId(),p.getDeliveryFee());

            productDtoList.add(productDto);
        }

        return productDtoList;
    }
}
