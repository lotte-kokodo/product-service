package shop.kokodo.productservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisCommandTimeoutException;
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
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.dto.PagingProductDto;
import shop.kokodo.productservice.dto.ProductDetailTemplateDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.kafka.ProductAndDetailDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.CategoryService;
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
    private CategoryService categoryService;
    private ObjectMapper objectMapper;

    @Autowired
    public RedisController(RedisTemplate<String, String> redisTemplate, ProductService productService, CategoryService categoryService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.productService = productService;
        this.categoryService = categoryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/category/all")
    public ResponseEntity all() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        String key = "categoryAll";

        try {
            ValueOperations<String, String> vop = redisTemplate.opsForValue();

            if(vop.get(key) == null){
                log.info("Category All - no Cache");
                categoryDtoList = categoryService.findAll();
                vop.set(key,objectMapper.writeValueAsString(categoryDtoList));
            } else{
                categoryDtoList = objectMapper.readValue(vop.get(key), new TypeReference<List<CategoryDto>>() {});
            }
        } catch (RedisCommandTimeoutException e){
            log.warn("Redis Timeout : " + e);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(categoryDtoList == null || categoryDtoList.size() == 0){
                categoryDtoList = categoryService.findAll();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(categoryDtoList);
    }

    @GetMapping("/product/categoryId/{categoryId}/{sortingId}/{currentpage}")
    public Response productByCategorySorting(@PathVariable("categoryId") long categoryId,
                                             @PathVariable("sortingId") long sortingId,
                                             @PathVariable("currentpage") int page){
        String key = "categoryProduct-" + Long.toString(categoryId) + Long.toString(sortingId) + Integer.toString(page);
        PagingProductDto pagingProductDto = new PagingProductDto(new ArrayList<>(),0);

        try {
            ValueOperations<String, String> vop = redisTemplate.opsForValue();

            if(vop.get(key) == null){
                log.info("Category Product - no Cache");
                pagingProductDto = sortingDto(productService.findProductByCategory(categoryId,page-1),sortingId);
                vop.set(key,objectMapper.writeValueAsString(pagingProductDto));
            } else{
                pagingProductDto = objectMapper.readValue(vop.get(key), new TypeReference<PagingProductDto>() {});
            }
        } catch (RedisCommandTimeoutException e){
            log.warn("Redis Timeout : " + e);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(pagingProductDto == null || pagingProductDto.getTotalCount() ==0){
                pagingProductDto = sortingDto(productService.findProductByCategory(categoryId,page-1),sortingId);
            }
        }

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