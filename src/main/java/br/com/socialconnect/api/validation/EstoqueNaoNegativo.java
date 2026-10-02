package br.com.socialconnect.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Violação desta regra é de negócio: o GlobalExceptionHandler responde 422, e não 400
@Documented
@Constraint(validatedBy = EstoqueNaoNegativoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface EstoqueNaoNegativo {
    String message() default "{EstoqueNaoNegativo.estoque}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
