package vn.edu.vnu.uet.dktadmin.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse {
    @JsonProperty("Size")
    private int size;

    @JsonProperty("Page")
    private int page;

    @JsonProperty("Count")
    private int count;

    public PageResponse(int page, int size, int count) {
        this.size = size;
        this.page = page;
        this.count = count;
    }
}
