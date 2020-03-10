package vn.edu.vnu.uet.dktadmin.rest.controller.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.dto.service.location.LocationService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.location.LocationRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.location.LocationResponse;

@RestController
@RequestMapping("/admin")
public class LocationController extends BaseController {
    @Autowired
    private LocationService locationService;

    @PostMapping("/locations")
    public ApiDataResponse<LocationResponse> createLocation(@RequestBody LocationRequest request){
        try {
            return ApiDataResponse.ok(locationService.createLocation(request));
        }catch (Exception e) {
            return ApiDataResponse.error();
        }
    }
}
