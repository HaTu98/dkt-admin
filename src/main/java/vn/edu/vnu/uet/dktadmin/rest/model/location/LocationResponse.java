package vn.edu.vnu.uet.dktadmin.rest.model.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse {
    private Long id;

    private String locationName;

    private String locationCode;

    private String description;
}
