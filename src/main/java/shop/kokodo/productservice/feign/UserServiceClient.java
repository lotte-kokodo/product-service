package shop.kokodo.productservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="user-service")
public interface UserServiceClient {

//    @GetMapping("/user-service/{memberId}")
//    String findMemberName(@PathVariable long memberId);
}
