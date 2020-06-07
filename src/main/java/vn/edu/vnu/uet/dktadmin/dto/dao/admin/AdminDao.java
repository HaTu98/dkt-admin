package vn.edu.vnu.uet.dktadmin.dto.dao.admin;

import vn.edu.vnu.uet.dktadmin.dto.model.Admin;

public interface AdminDao {
    Admin getByUsername(String username);
    Admin getByEmail(String email);
    Admin save(Admin admin);
    Admin getById(Long id);
}
