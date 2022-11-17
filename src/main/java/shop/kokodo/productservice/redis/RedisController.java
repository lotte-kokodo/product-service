package shop.kokodo.productservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.PagingProductDto;
import shop.kokodo.productservice.dto.ProductDetailTemplateDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.kafka.ProductAndDetailDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    private RedisTemplate<String, String> redisTemplate;
    private ProductService productService;
    private ObjectMapper objectMapper;

    @Autowired
    public RedisController(RedisTemplate<String, String> redisTemplate, ProductService productService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/categoryId/{categoryId}/{sortingId}/{currentpage}")
    public Response productByCategorySorting(@PathVariable("categoryId") long categoryId,
                                             @PathVariable("sortingId") long sortingId,
                                             @PathVariable("currentpage") int page){
        log.info("Category - Product redis start");

        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String key = "categoryProduct-" + Long.toString(categoryId) + Long.toString(sortingId) + Integer.toString(page);
        PagingProductDto pagingProductDto = new PagingProductDto();
        String jsonInString = "";

        if(vop.get(key) == null){
            log.info("Category - Product no Cache");
            Page<Product> pageProduct = productService.findProductByCategory(categoryId,page-1);

            try{
                jsonInString = objectMapper.writeValueAsString(sortingDto(pageProduct,sortingId));
                vop.set(key,jsonInString);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }

        try {
            pagingProductDto = objectMapper.readValue(vop.get(key), new TypeReference<PagingProductDto>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info(pagingProductDto.toString());

        return Response.success(pagingProductDto);
    }

    public PagingProductDto sortingDto(Page<Product> pageProduct, long sortingId){
        List<Product> pr = new ArrayList<>();
        List<ProductDto> productDtoList = new ArrayList<>();

        if(sortingId == 1){
            pr = Arrays.asList(pageProduct.stream().sorted(Comparator.comparing(Product::getDeadline)).toArray(Product[]::new));
        }else if(sortingId == 2){
            pr = Arrays.asList(pageProduct.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).toArray(Product[]::new));
        }else if(sortingId == 3){
            pr = Arrays.asList(pageProduct.stream().sorted(Comparator.comparing(Product::getPrice)).toArray(Product[]::new));
        }else if(sortingId == 4){
            pr = Arrays.asList(pageProduct.stream().sorted(Comparator.comparing(Product::getCreatedDate).reversed()).toArray(Product[]::new));
        }

        for(Product p : pr){
            ProductDto productDto = new ProductDto(p.getId(),p.getCategory().getId(),
                    p.getName(),p.getPrice(),p.getDisplayName(),
                    p.getStock(),p.getDeadline(),p.getThumbnail(),
                    p.getSellerId(),p.getDeliveryFee());

            productDtoList.add(productDto);
        }

        PagingProductDto pagingProductDto = PagingProductDto.builder()
                .productDtoList(productDtoList)
                .totalCount(pageProduct.getTotalElements())
                .build();

        return pagingProductDto;
    }
}