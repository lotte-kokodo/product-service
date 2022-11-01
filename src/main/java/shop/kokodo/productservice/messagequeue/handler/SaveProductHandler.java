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
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(readOnly = true)
public class SaveProductHandler{
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public SaveProductHandler(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    @Transactional
    @KafkaListener(topics = "product-save")
    public void saveProduct(String kafkaMessage) {
        log.info("Kafka save product Message : " + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        String[] strs = String.valueOf(map.get("deadline"))
                                .substring(1,String.valueOf(map.get("deadline")).length()-1)
                                .replaceAll(" ","")
                                .split(",");
        LocalDateTime dateTime = formatDate(strs);

        Product product = Product.builder()
                .category(categoryRepository.findById(Long.parseLong(String.valueOf(map.get("categoryId"))))
                        .orElse(Category.builder().name("신규 카테고리- 관리자 문의 필요.").build()))
                .name(String.valueOf(map.get("name")))
                .price(Integer.parseInt(String.valueOf(map.get("price"))))
                .displayName(String.valueOf(map.get("displayName")))
                .stock(Integer.parseInt(String.valueOf(map.get("stock"))))
                .deadline(dateTime)
                .thumbnail(String.valueOf(map.get("thumbnail")))
                .sellerId(Long.parseLong(String.valueOf(map.get("sellerId"))))
                .deliveryFee(Integer.parseInt(String.valueOf(map.get("deliveryFee"))))
                .build();

        log.info("product" + product.toString());

        if(product != null){
            productRepository.save(product);
            log.info("Kafka save product Message Success");
        }
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
