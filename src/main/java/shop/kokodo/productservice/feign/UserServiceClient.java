package shop.kokodo.productservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.kokodo.productservice.dto.UserDto;

@FeignClient(name="user-service")
public interface UserServiceClient {

    @GetMapping("/member/productDetail/{memberId}")
    UserDto findMemberName(@PathVariable long memberId);
}
