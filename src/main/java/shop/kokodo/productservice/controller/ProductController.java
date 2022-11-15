package shop.kokodo.productservice.controller;


import feign.Param;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kokodo.productservice.dto.OrderSheetProductDto;
import shop.kokodo.productservice.dto.PagingProductDto;
import shop.kokodo.productservice.dto.ProductAndProductDetailDto;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.response.ProductThumbnailDto;
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

    /* 상품 삭제 */
    @DeleteMapping("/delete/{productId}")
    public Response productDelete(@PathVariable("productId") long id){
        productService.deleteProduct(id);
        return Response.success();
    }

    // 단일 상품 조회
    @GetMapping("/productId/{productId}")
    public Response findById(@PathVariable("productId") long id) {
        Optional<Product> optionalProduct = productService.findById(id);
        Product product = optionalProduct.get();

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

    // 상품전체 조회
    @GetMapping("/productAll")
    public Response findAll() {
        return Response.success(productService.findAll());
    }

    /*
    =============== 정렬 정의 ===============
    * 1. 추천순 (유통기한 가까운 것)
    * 2. 높은 가격순
    * 3. 낮은 가격순
    * 4. 신상품순
    */

    // 카테고리별 상품 정렬
    @GetMapping("/categoryId/{categoryId}/{sortingId}/{currentpage}")
    public Response productByCategorySorting(@PathVariable("categoryId") long categoryId,
                                             @PathVariable("sortingId") long sortingId,
                                             @PathVariable("currentpage") int page){
        Page<Product> pageProduct = productService.findProductByCategory(categoryId,page-1);
        return Response.success(sortingDto(pageProduct,sortingId));
    }

    /* main 상품(new, sale, seller) 12개만 보여주기 */

    /* 신상품 */
    @GetMapping("/main/new")
    public Response findByNew() {
        Page<Product> productList = productService.findProductByNew(0);
        return Response.success(pagingDto(productList));
    }

    /* 타임 세일 상품 */
    @GetMapping("/main/sale")
    public Response findBySale() {
        Page<Product> productList = productService.findProductBySale(0);
        return Response.success(pagingDto(productList));
    }

    /* MD추천 */
    @GetMapping("/main/seller")
    public Response findBySeller() {
        Page<Product> productList = productService.findProductBySeller(0);
        return Response.success(pagingDto(productList));
    }

    // sale 상품 정렬 및 상품 전체
    @GetMapping("/main/sale/all/{sortingId}/{currentpage}")
    public Response productBySaleSorting(@PathVariable("sortingId") long sortingId
                                        ,@PathVariable("currentpage") int page){
        Page<Product> pageProduct = productService.findProductBySale(page-1);
        return Response.success(sortingDto(pageProduct,sortingId));
    }

    // MD추천 상품 정렬 및 상품전체
    @GetMapping("/main/seller/all/{sortingId}/{currentpage}")
    public Response productBySellerSorting(@PathVariable("sortingId") long sortingId
                                            ,@PathVariable("currentpage") int page){
        Page<Product> pageProduct = productService.findProductBySeller(page-1);
        return Response.success(sortingDto(pageProduct,sortingId));
    }

    // 전체검색
    @GetMapping("/totalSearch/{totalSearch}/{sortingId}/{currentpage}")
    public Response productByTotalSearch(@PathVariable("totalSearch") String totalSearch
                                         ,@PathVariable("sortingId") long sortingId
                                         ,@PathVariable("currentpage") int page){
        Page<Product> pageProduct = productService.findProductByTotalSearch(totalSearch,page-1);
        return Response.success(sortingDto(pageProduct,sortingId));
    }

    public List<ProductDto> pagingDto(Page<Product> productList){
        List<ProductDto> productDtoList = new ArrayList<>();

        for(Product p : productList){
            ProductDto productDto = new ProductDto(p.getId(),p.getCategory().getId(),
                    p.getName(),p.getPrice(),p.getDisplayName(),
                    p.getStock(),p.getDeadline(),p.getThumbnail(),
                    p.getSellerId(),p.getDeliveryFee());

            productDtoList.add(productDto);
        }

        if(productDtoList.size()>12){
            while(productDtoList.size()>12){
                productDtoList.remove(12);
            }
        }

        return productDtoList;
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

    /* ==================Feign Client ======================= */

    @GetMapping("/detail/{productId}")
    public Response productDetail(@PathVariable long productId){
        ProductAndProductDetailDto pr = productService.findProductDetail(productId);
        System.out.println(pr.toString());
        return Response.success(pr);
    }

    @GetMapping("/detail/name")
    public Response productDetailName(@RequestParam String productName) {
        List<ProductDto> pr = productService.findProductDetailByName(productName);
        return Response.success(pr);
    }

    @GetMapping("seller/stock/{sellerId}/{page}")
    public ResponseEntity findByProductStockLack(@PathVariable long sellerId, @PathVariable int page) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByProductStockLack(sellerId, page-1));
    }

    /* seller 상품 조회 Feign Client */
    @GetMapping
    public ResponseEntity findByProductNameAndStatusAndDate(@Param String productName, @Param Integer status
            , @Param String startDate, @Param String endDate, @Param Long sellerId, @Param Integer page){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        PagingProductDto pagingProductDto = productService.findBy(productName,status, LocalDateTime.parse(startDate, formatter)
                ,LocalDateTime.parse(endDate, formatter),sellerId,page-1);

        return ResponseEntity.status(HttpStatus.OK).body(pagingProductDto);
    }

    @GetMapping("/productSellerId")
    public ResponseEntity getProductSellerId(@RequestParam List<Long> productId){
        System.out.println("productId = " + productId);
        List<Long> productSellerId = productService.getProductSellerId(productId);

        return ResponseEntity.status(HttpStatus.OK).body(productSellerId);
    }

    @GetMapping("/seller/{sellerId}")
    public Response findBySellerId(@PathVariable long sellerId){
        System.out.println("ProductController.findBySellerId");

        List<ProductDto> productList = productService.findBySellerId(sellerId);

        System.out.println(productList.toString());

        return  Response.success(productList);
    }

    @GetMapping("/feign/id")
    public ResponseEntity findProductById(@RequestParam Long productId){
        boolean flag = productService.findProductOpById(productId).isPresent()? true: false;

        return ResponseEntity.status(HttpStatus.OK).body(flag);
    }

    // [주문관련 상품 조회] 주문서 상품 조회
    @GetMapping("/ordersheet")
    public Response getOrderProducts(@RequestParam List<Long> productIds) {

        Map<Long, OrderSheetProductDto> orderProductMap = productService.getOrderProducts(productIds);
        return Response.success(orderProductMap);
    }
}