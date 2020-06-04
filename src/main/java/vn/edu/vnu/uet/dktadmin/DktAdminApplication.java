package vn.edu.vnu.uet.dktadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import vn.edu.vnu.uet.dktadmin.dto.seeder.AdminAccount;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

@SpringBootApplication
public class DktAdminApplication {
    ContextRefreshedEvent event;
    @Autowired
    private AdminAccount adminAccount;
    public static void main(String[] args) {
        SpringApplication.run(DktAdminApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            adminAccount.adminSeeder(event);
        };
    }

}
