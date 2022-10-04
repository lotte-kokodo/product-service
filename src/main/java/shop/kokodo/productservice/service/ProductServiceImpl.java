package shop.kokodo.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Product saveProduct(ProductDto productDto) {
        Product product = Product.builder()
                .category(categoryRepository.findById(productDto.getCategoryId())
                        .orElse(Category.builder().name("신규 카테고리- 관리자 문의 필요.").build()))
                .name(productDto.getName())
                .price(productDto.getPrice())
                .displayName(productDto.getDisplayName())
                .stock(productDto.getStock())
                .deadline(productDto.getDeadline())
                .thumbnail(productDto.getThumbnail())
                .sellerId(productDto.getSellerId())
                .deliveryFee(productDto.getDeliveryFee())
                .build();

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElse(new Product());

        product.setCategory(categoryRepository.findById(productDto.getCategoryId()) .orElse(new Category()));
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDisplayName(productDto.getDisplayName());
        product.setStock(productDto.getStock());
        product.setDeadline(productDto.getDeadline());
        product.setThumbnail(productDto.getThumbnail());
        product.setSellerId(productDto.getSellerId());
        product.setDeliveryFee(productDto.getDeliveryFee());

        return productRepository.save(product);
    }

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

    @Override
    public List<ProductDto> findProductByTotalSearch(String productDisplayName) {
        return returnProductDtoList(productRepository.findProductByTotalSearch(productDisplayName));
    }

    @Override
    public List<ProductDto> findProductByCategorySearch(long categoryId, String productDisplayName) {
        return returnProductDtoList(productRepository.findProductByCategorySearch(categoryId, productDisplayName));
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
