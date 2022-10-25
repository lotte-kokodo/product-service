package shop.kokodo.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.ProductInquireRequestDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.service.ProductInquireService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-inquire")
public class ProductInquireController {

    private final ProductInquireService productInquireService;


    @GetMapping("/{productId}")
    public Response findByProductId(@PathVariable long productId){

        return Response.success(productInquireService.findByProductId(productId));
    }

    @PostMapping("/question/{productId}")
    public Response saveQuestion(@PathVariable long productId, @RequestBody ProductInquireRequestDto dto, @RequestHeader long memberId){
        dto.setMemberId(memberId);
        dto.setProductId(productId);

        long id = productInquireService.saveProductInquire(dto);

        return Response.success(id);
    }

    @PostMapping("/answer/{inquireId}")
    public Response saveAnswer(@PathVariable long inquireId, @RequestParam String answer){
        long id= productInquireService.saveAnswer(inquireId,answer);

        return Response.success(id);
    }

    @GetMapping("/{sellerId}/no-answer")
    public Response findNoAnswer(@PathVariable long sellerId){
        return Response.success(productInquireService.findNotAnswerInquire(sellerId));
    }

    @GetMapping("/{sellerId}/answer")
    public Response findAnswer(@PathVariable long sellerId){
        return Response.success(productInquireService.findAnswerInquire(sellerId));
    }

    @GetMapping("/member")
    public Response findByMemberId(@RequestHeader long memberId){

        return Response.success();
    }
}
