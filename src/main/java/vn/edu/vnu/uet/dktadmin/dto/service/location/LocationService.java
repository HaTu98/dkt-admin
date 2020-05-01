package vn.edu.vnu.uet.dktadmin.dto.service.location;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.utilities.Util;
import vn.edu.vnu.uet.dktadmin.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Location;
import vn.edu.vnu.uet.dktadmin.rest.model.location.LocationRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.location.LocationResponse;

@Service
public class LocationService {
    private final LocationDao locationDao;
    private final MapperFacade mapperFacade;

    public LocationService(LocationDao locationDao, MapperFacade mapperFacade) {
        this.locationDao = locationDao;
        this.mapperFacade = mapperFacade;
    }

    public LocationResponse createLocation(LocationRequest locationRequest) {
        Location location = mapperFacade.map(locationRequest, Location.class);

        return mapperFacade.map(locationDao.store(location),LocationResponse.class);
    }

    public Location createLocation(String locationName) {
        Location location = new Location();
        location.setLocationName(locationName);
        location.setLocationCode(Util.camelToSnake(locationName));
        return locationDao.store(location);
    }

    public boolean existLocation(String name) {
        Location location = locationDao.getByLocationName(name);
        return location != null;
    }


}
