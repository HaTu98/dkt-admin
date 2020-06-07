package vn.edu.vnu.uet.dktadmin.dto.dao.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Admin;
import vn.edu.vnu.uet.dktadmin.dto.repository.AdminRepository;

@Service
public class AdminDaoImpl implements AdminDao{
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin getByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin getByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }
}
