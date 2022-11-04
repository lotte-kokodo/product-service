package shop.kokodo.productservice.controller;


import feign.Param;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.service.CategoryService;
import shop.kokodo.productservice.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @DeleteMapping("/delete/{productId}")
    public Response productDelete(@PathVariable("productId") long id){
        productService.deleteProduct(id);
        return Response.success();
    }

    /*
    main 상품(new, sale, seller) 12개만 보여주기
     */

    // main new상품 
    @GetMapping("/main/new")
    public Response findByNew() {
        List<ProductDto> productDtoList = productService.findProductByNew();
        if(productDtoList.size()>12){
            while(productDtoList.size()>12){
                productDtoList.remove(12);
            }
        }else if(productDtoList.size() == 0) {
            return Response.failure(0,"상품이 존재하지 않습니다.");
        }

        return Response.success(productDtoList);
    }

    // main sale상품
    @GetMapping("/main/sale")
    public Response findBySale() {
        List<ProductDto> productDtoList = productService.findProductBySale();
        if(productDtoList.size()>12){
            while(productDtoList.size()>12){
                productDtoList.remove(12);
            }
        }else if(productDtoList.size() == 0) {
            return Response.failure(0,"상품이 존재하지 않습니다.");
        }

        return Response.success(productDtoList);
    }

    //sale 상품 정렬 및 상품 전체
    @GetMapping("/main/sale/all/{sortingId}")
    public Response productBySaleSorting(@PathVariable("sortingId") long sortingId){
        List<ProductDto> productDtoList = productService.findProductBySale();

        if(sortingId == 1){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getDeadline)).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 2){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getPrice).reversed()).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 3){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getPrice)).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 4){
            productDtoList = productService.findProductBySaleSortingNew();
        }else if(sortingId == 5){
            productDtoList = productService.findProductBySaleSortingReview();
        }

        return Response.success(productDtoList);
    }

    // main MD추천 상품
    @GetMapping("/main/seller")
    public Response findBySeller() {
        List<ProductDto> productDtoList = productService.findProductBySeller();
        if(productDtoList.size()>12){
            while(productDtoList.size()>12){
                productDtoList.remove(12);
            }
        }else if(productDtoList.size() == 0) {
            return Response.failure(0,"상품이 존재하지 않습니다.");
        }

        return Response.success(productDtoList);
    }

    // MD추천 상품 정렬 및 상품전체
    @GetMapping("/main/seller/all/{sortingId}")
    public Response productBySellerSorting(@PathVariable("sortingId") long sortingId){
        List<ProductDto> productDtoList = productService.findProductBySeller();

        if(sortingId == 1){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getDeadline)).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 2){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getPrice).reversed()).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 3){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getPrice)).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 4){
            productDtoList = productService.findProductBySellerSortingNew();
        }else if(sortingId == 5){
            productDtoList = productService.findProductBySellerSortingReview();
        }

        return Response.success(productDtoList);
    }

    // 상품별
    @GetMapping("/productId/{productId}")
    public Response findById(@PathVariable("productId") long id) {
        Product product = productService.findById(id);

        if(product == null){
            return Response.failure(0,"해당 상품이 존재하지 않습니다.");
        }

        ProductDto productDto = ProductDto.builder().id(product.getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .price(product.getPrice())
                .displayName(product.getDisplayName())
                .stock(product.getStock())
                .deadline(product.getDeadline())
                .thumbnail(product.getThumbnail())
                .sellerId(product.getSellerId())
                .deliveryFee(product.getDeliveryFee())
                .build();

        return Response.success(productDto);
    }

    // 상품전체
    @GetMapping("/productAll")
    public Response findAll() {
        return Response.success(productService.findAll());
    }

    // 카테고리별 상품
    @GetMapping("/categoryId/{categoryId}")
    public Response productByCategory(@PathVariable("categoryId") long categoryId){
        return Response.success(productService.findProductByCategory(categoryId));
    }

    // 카테고리별 상품 정렬
    @GetMapping("/categoryId/{categoryId}/{sortingId}")
    public Response productByCategorySorting(@PathVariable("categoryId") long categoryId,
                                      @PathVariable("sortingId") long sortingId){
        List<ProductDto> productDtoList = productService.findProductByCategory(categoryId);

        if(sortingId == 1){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getDeadline)).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 2){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getPrice).reversed()).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 3){
            ProductDto[] pr = productDtoList.stream().sorted(Comparator.comparing(ProductDto::getPrice)).toArray(ProductDto[]::new);
            productDtoList = Arrays.asList(pr);
        }else if(sortingId == 4){
            productDtoList = productService.findProductByCategorySortingNew(categoryId);
        }else if(sortingId == 5){
            productDtoList = productService.findProductByCategorySortingReview(categoryId);
        }

        return Response.success(productDtoList);
    }

    // 전체검색
    @GetMapping("/totalSearch/{totalSearch}")
    public Response productByTotalSearch(@PathVariable("totalSearch") String totalSearch){
        return Response.success(productService.findProductByTotalSearch(totalSearch));
    }

    @GetMapping("/detail/{productId}")
    public Response productDetail(@PathVariable long productId){
        return Response.success(productService.findProductDetail(productId));
    }

    @GetMapping
    public ResponseEntity findByProductNameAndStatusAndDate(@Param String productName, @Param Integer status
            , @Param String startDate, @Param String endDate, @Param Long sellerId, @Param int page){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<ProductDto> list = productService.findBy(productName,status, LocalDateTime.parse(startDate, formatter)
                ,LocalDateTime.parse(endDate, formatter),sellerId,page);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/productSellerId")
    public ResponseEntity getProductSellerId(@RequestParam List<Long> productId){
        System.out.println("productId = " + productId);
        List<Long> productSellerId = productService.getProductSellerId(productId);

        return ResponseEntity.status(HttpStatus.OK).body(productSellerId);
    }

    @GetMapping("/seller/{sellerId}")
    public Response findBySellerId(@PathVariable long sellerId){

        List<ProductDto> productList = productService.findBySellerId(sellerId);

        return  Response.success(productList);
    }

    @GetMapping("/feign/id")
    public ResponseEntity findProductById(@RequestParam Long productId){
        boolean flag = productService.findProductOpById(productId).isPresent()? true: false;

        return ResponseEntity.status(HttpStatus.OK).body(flag);

    }
}