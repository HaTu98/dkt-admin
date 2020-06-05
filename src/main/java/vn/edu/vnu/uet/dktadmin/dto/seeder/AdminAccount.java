package vn.edu.vnu.uet.dktadmin.dto.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.vnu.uet.dktadmin.dto.model.Admin;
import vn.edu.vnu.uet.dktadmin.dto.repository.AdminRepository;

import java.time.Instant;

@Component
public class AdminAccount {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void adminSeeder(ContextRefreshedEvent event) {
        Admin admin = adminRepository.findByUsername("admin");
        if (admin != null) return;
        admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin@123"));
        admin.setFullName("Admin");
        admin.setEmail("dkt.tuhv@gmail.com");
        admin.setCreatedAt(Instant.now());
        adminRepository.save(admin);
    }
}
