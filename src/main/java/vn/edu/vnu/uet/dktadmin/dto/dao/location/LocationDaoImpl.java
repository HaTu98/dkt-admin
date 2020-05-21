package vn.edu.vnu.uet.dktadmin.dto.dao.location;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Location;
import vn.edu.vnu.uet.dktadmin.dto.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationDaoImpl implements LocationDao {
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

    @Override
    public Location getByLocationCode(String code) {
        return locationRepository.findByLocationCode(code);
    }

    @Override
    public Location getById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Location> getAll() {
        List<Location> locations = locationRepository.findAll();
        if (CollectionUtils.isEmpty(locations)) {
            return new ArrayList<>();
        }
        return locations;
    }
}
