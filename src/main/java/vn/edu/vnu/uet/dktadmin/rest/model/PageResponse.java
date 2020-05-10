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

    @JsonProperty("Total")
    private int total;

    public PageResponse(int page, int size, int total) {
        this.size = size;
        this.page = page;
        this.total = total;
    }
}
