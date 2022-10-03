package shop.kokodo.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * null 값을 가지는 필드는, Json 응답에 포함되지 않도록 설정
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * static factory method를 사용하므로 success, failure의 접근 제어레벨을 priavte로 설정한다.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/**
 * 응답 객체를 Json으로 변환하기 위해 Getter가 필요하다.
 */
@Getter
public class Response {
    private boolean success;
    private int code;
    private Result result;

    /**
     * 요청이 성공하고 응답 데이터가 필요 없을 경우 사용
     */
    public static Response success(){
        return new Response(true, HttpStatus.OK.value(), null);
    }

    /**
     * 성공했을 경우 응답데이터가 있을 경우 사용
     */
    public static <T> Response success(T data){
        return new Response(true, 200, new Success<>(data));
    }

    /**
     * 실패할 경우 실패 메시지와 같이 반환
     */
    public static Response failure(int code, String msg){
        return new Response(false, code, new Failure(msg));
    }
}