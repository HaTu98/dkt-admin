package vn.edu.vnu.uet.dktadmin.dto.dao.location;

import vn.edu.vnu.uet.dktadmin.dto.model.Location;

public interface LocationDao {
    Location store(Location location);
    Location getByLocationName(String name);
    Location getByLocationCode(String code);
}
