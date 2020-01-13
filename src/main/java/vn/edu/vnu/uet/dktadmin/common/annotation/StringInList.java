package vn.edu.vnu.uet.dktadmin.common.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import vn.edu.vnu.uet.dktadmin.common.validator.StringInListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringInListValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Order(value = Ordered.LOWEST_PRECEDENCE)
public @interface StringInList {
    String[] array() default {};

    boolean allowBlank() default false;

    String message() default "invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}