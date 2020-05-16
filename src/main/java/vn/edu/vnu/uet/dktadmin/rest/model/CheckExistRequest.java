package vn.edu.vnu.uet.dktadmin.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckExistRequest {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "Code")
    private String code;

    @JsonProperty(value = "Mode")
    private String mode;
}
