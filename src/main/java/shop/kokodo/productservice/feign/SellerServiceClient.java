package shop.kokodo.productservice.feign;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="seller-service")
public interface SellerServiceClient {

    @GetMapping(value="/seller", produces = "application/json")
    Boolean getSeller(@RequestParam(value = "id") long id);
}
