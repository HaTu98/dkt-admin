package vn.edu.vnu.uet.dktadmin.dto.dao.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Location;
import vn.edu.vnu.uet.dktadmin.dto.repository.LocationRepository;

@Service
public class LocationDaoImpl implements LocationDao{
    private final LocationRepository locationRepository;

    public LocationDaoImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location store(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location getByLocationName(String name) {
        return locationRepository.findByLocationName(name);
    }
}
