package vn.edu.vnu.uet.dktadmin.common.validator;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import vn.edu.vnu.uet.dktadmin.common.annotation.StringInList;
import vn.edu.vnu.uet.dktadmin.common.utilities.Util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class StringInListValidator implements ConstraintValidator<StringInList, String> {
   private StringInList _annotation;

   @Override
   public void initialize(StringInList stringInList) {
      this._annotation = stringInList;
   }

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      boolean isValid = false;
      if (_annotation.allowBlank()) {
         if (StringUtils.isEmpty(value)) {
            isValid = true;
         }
      }

      if (!isValid) {
         isValid = ArrayUtils.contains(_annotation.array(), value);
      }

      if (!isValid) {
         context.disableDefaultConstraintViolation();

         String message = String.format("is not in [%s]",
                 Util.joinList(Arrays.asList(_annotation.array())));

         context.buildConstraintViolationWithTemplate(message)
                 .addConstraintViolation();
      }
      return isValid;
   }
}
