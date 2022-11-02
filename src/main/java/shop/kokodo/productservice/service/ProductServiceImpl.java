package shop.kokodo.productservice.service;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.circuitbreaker.AllCircuitBreaker;
import shop.kokodo.productservice.dto.ProductAndProductDetailDto;
import shop.kokodo.productservice.dto.ProductDetailDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.UserDto;
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
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCustomRepository productCustomRepository;
    private final SellerServiceClient sellerServiceClient;
    private final CircuitBreaker circuitBreaker = AllCircuitBreaker.createSellerCircuitBreaker();

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

    @Override
    public Product findById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    @Override
    public List<ProductDto> findAll() {
        return returnProductDtoList(productRepository.findAll());
    }

    @Override
    public List<ProductDto> findProductByCategory(long categoryId) {
        return returnProductDtoList(productRepository.findProductByCategory(categoryId));
    }

    public List<ProductDto> findProductByCategorySortingNew(long categoryId) {
        List<Product> productList = productRepository.findProductByCategory(categoryId);
        Product[] pr = productList.stream().sorted(Comparator.comparing(Product::getCreatedDate).reversed()).toArray(Product[]::new);
        productList = Arrays.asList(pr);
        return returnProductDtoList(productList);
    }

    @Override
    public List<ProductDto> findProductBySaleSortingNew() {
        List<Product> productList = productRepository.findProductBySale();
        Product[] pr = productList.stream().sorted(Comparator.comparing(Product::getCreatedDate).reversed()).toArray(Product[]::new);
        productList = Arrays.asList(pr);
        return returnProductDtoList(productList);
    }

    @Override
    public List<ProductDto> findProductBySellerSortingNew() {
        List<Product> productList = productRepository.findProductBySeller();
        Product[] pr = productList.stream().sorted(Comparator.comparing(Product::getCreatedDate).reversed()).toArray(Product[]::new);
        productList = Arrays.asList(pr);
        return returnProductDtoList(productList);
    }

    @Override
    public List<ProductDto> findProductByCategorySortingReview(long categoryId) {
        return returnProductDtoList(productRepository.findProductByCategorySortingReview(categoryId));
    }

    @Override
    public List<ProductDto> findProductBySaleSortingReview() {
        return returnProductDtoList(productRepository.findProductBySaleSortingReview());
    }

    @Override
    public List<ProductDto> findProductBySellerSortingReview() {
        return returnProductDtoList(productRepository.findProductBySellerSortingReview());
    }

    @Override
    public List<ProductDto> findProductByTotalSearch(String productDisplayName) {
        return returnProductDtoList(productRepository.findProductByTotalSearch(productDisplayName));
    }

    @Override
    public List<ProductDto> findProductByNew() {
        return returnProductDtoList(productRepository.findProductByNew());
    }

    @Override
    public List<ProductDto> findProductBySale() {
        return returnProductDtoList(productRepository.findProductBySale());
    }

    @Override
    public List<ProductDto> findProductBySeller() { return returnProductDtoList(productRepository.findProductBySeller());  }

    // TODO: detail 이미지 template 부분 추가
    public ProductAndProductDetailDto findProductDetail(long productId){
        Product product = productRepository.findById(productId).get();
//                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 상품"));
        return convertToProductAndProductDetailDto(product);
    }

    @Override
    public List<ProductDto> findBy(String name, Integer status, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                   Long sellerId) {
            return productCustomRepository.findProduct(name,status,startDateTime,endDateTime,sellerId);
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
