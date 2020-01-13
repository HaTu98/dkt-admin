package vn.edu.vnu.uet.dktadmin.common.validator;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonRootName(value="error")
public class CustomError {
    private String message;
}
