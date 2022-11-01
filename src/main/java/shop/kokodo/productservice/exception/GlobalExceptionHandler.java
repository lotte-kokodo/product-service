package shop.kokodo.productservice.exception;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.kokodo.productservice.dto.response.Failure;
import shop.kokodo.productservice.dto.response.Response;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Response handleIllegalArgumentException(final IllegalArgumentException e) {
        log.debug("[GlobalExceptionHandler] handle exception (cause = {})",
            Arrays.toString(e.getStackTrace()));

        return Response.failure(500, e.getMessage());
    }

    @ExceptionHandler(NoSellerServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Failure handleNoSellerServiceException(NoSellerServiceException e){

        log.debug("[NoSellerServiceException] 존재하지 않은 Seller ID");
        return new Failure("존재하지 않은 Seller ID");
    }

}
