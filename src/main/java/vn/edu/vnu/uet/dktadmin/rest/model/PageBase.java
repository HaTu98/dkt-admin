package vn.edu.vnu.uet.dktadmin.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageBase {
    @JsonProperty("Size")
    private Integer size;

    @JsonProperty("Page")
    private Integer page;

    public PageBase(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
}
