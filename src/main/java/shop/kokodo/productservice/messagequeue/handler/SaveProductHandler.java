package shop.kokodo.productservice.messagequeue.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.dto.ProductDetailTemplateDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.TemplateDto;
import shop.kokodo.productservice.dto.kafka.ProductAndDetailDto;
import shop.kokodo.productservice.entity.*;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@Transactional
public class SaveProductHandler{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public SaveProductHandler(ProductRepository productRepository, CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }


    public void saveProduct(String message) {
        log.info("Kafka save product Message : " + message);
        ProductAndDetailDto productAndDetailDto = new ProductAndDetailDto();
        try{
            productAndDetailDto = objectMapper.readValue(message, new TypeReference<ProductAndDetailDto>() {});

        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        Product product = convertToProduct(productAndDetailDto);

        product.changeProductDetail(convertToProductDetail(productAndDetailDto.getDetails()));

        productRepository.save(product);

    }

    public void saveProductTemplate(String message) {
        log.info("Kafka save product template Message : " + message);
        ProductDetailTemplateDto productDetailTemplateDto = new ProductDetailTemplateDto();
        try{
            productDetailTemplateDto = objectMapper.readValue(message, new TypeReference<ProductDetailTemplateDto>() {});

        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        Product product = convertToProductTemplate(productDetailTemplateDto);

        product.changeTemplateRec(convertToTemplateRec(productDetailTemplateDto.getTemplateDto()));

        productRepository.save(product);

    }

    private final TemplateRec convertToTemplateRec(TemplateDto templateDto){
        return TemplateRec.builder()
                .imageOne(templateDto.getImageOne())
                .imageTwo(templateDto.getImageTwo())
                .imageThree(templateDto.getImageThree())
                .imageFour(templateDto.getImageFour())
                .imageFive(templateDto.getImageFive())
                .writingTitle(templateDto.getWritingTitle())
                .writingTitleDetail(templateDto.getWritingTitleDetail())
                .writingHighlightOne(templateDto.getWritingHighlightOne())
                .writingHighlightTwo(templateDto.getWritingHighlightTwo())
                .writingName(templateDto.getWritingName())
                .writingDescription(templateDto.getWritingDescription())
                .build();
    }

    private final Product convertToProductTemplate(ProductDetailTemplateDto productDetailTemplateDto){
        return Product.builder()
                .category(categoryRepository.findById(productDetailTemplateDto.getCategoryId()).get())
                .name(productDetailTemplateDto.getName())
                .price(productDetailTemplateDto.getPrice())
                .displayName(productDetailTemplateDto.getDisplayName())
                .stock(productDetailTemplateDto.getStock())
                .deadline(productDetailTemplateDto.getDeadline())
                .thumbnail(productDetailTemplateDto.getThumbnail())
                .sellerId(productDetailTemplateDto.getSellerId())
                .deliveryFee(productDetailTemplateDto.getDeliveryFee())
                .detailFlag(DetailFlag.TEMPLATE)
                .build();
    }



    private final List<ProductDetail> convertToProductDetail(List<String> detailList){
        List<ProductDetail> tmpList = new ArrayList<>();

        for(int i=0;i<detailList.size();++i){
            ProductDetail pd = ProductDetail.builder()
                    .image(detailList.get(i))
                    .orders(i)
                    .build();

            tmpList.add(pd);
        }

        return tmpList;
    }

    private Product convertToProduct(ProductAndDetailDto productAndDetailDto){
        return Product.builder()
                .category(categoryRepository.findById(productAndDetailDto.getCategoryId()).get())
                .name(productAndDetailDto.getName())
                .price(productAndDetailDto.getPrice())
                .displayName(productAndDetailDto.getDisplayName())
                .stock(productAndDetailDto.getStock())
                .deadline(productAndDetailDto.getDeadline())
                .thumbnail(productAndDetailDto.getThumbnail())
                .sellerId(productAndDetailDto.getSellerId())
                .deliveryFee(productAndDetailDto.getDeliveryFee())
                .detailFlag(DetailFlag.IMG)
                .build();
    }

    public LocalDateTime formatDate(String @NotNull [] strs) {
        String str = strs[0] + "-"
                + twoLength(strs[1]) + "-"
                + twoLength(strs[2]) + "T"
                + twoLength(strs[3]) + ":"
                + twoLength(strs[4]) + ":"
                + twoLength(strs[5]) + "."
                + (int) (Math.random()*(999999-100000) + 100000);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        log.info(str);
        log.info(dateTime.toString());
        return dateTime;
    }

    public String twoLength(String two){
        return two.length()==0 ? "00" : two.length()==1 ? "0"+two : two;
    }
}
