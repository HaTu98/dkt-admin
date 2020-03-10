package vn.edu.vnu.uet.dktadmin.rest.model.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
    @JsonProperty(value = "LocationName")
    private String locationName;

    @JsonProperty(value = "LocationCode")
    private String locationCode;

    @JsonProperty(value = "Description")
    private String description;
}
