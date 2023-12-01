package pl.edu.pw.mwoproj.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private boolean wasSuccessful;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public static <T> ServiceResponse<T> createInvalidResponse(Set<ConstraintViolation<T>> violations) {
        var message = new StringBuilder();

        for (var v : violations) {
            message.append(v.getMessage()).append(" ");
        }

        return ServiceResponse.<T>builder()
                .message(message.toString())
                .build();
    }
}
