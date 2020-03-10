package vn.edu.vnu.uet.dktadmin.dto.service.location;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Location;
import vn.edu.vnu.uet.dktadmin.rest.model.location.LocationRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.location.LocationResponse;

@Service
public class LocationService {
    @Autowired
    private LocationDao locationDao;

    @Autowired
    private MapperFacade mapperFacade;

    public LocationResponse createLocation(LocationRequest locationRequest) {
        Location location = mapperFacade.map(locationRequest, Location.class);

        return mapperFacade.map(locationDao.store(location),LocationResponse.class);
    }
}
