package com.serpes.springboottodoapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.serpes.springboottodoapp.models.AppUser;
import com.serpes.springboottodoapp.repositories.AppUserRepository;

@Component
public class AppUserDataLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(AppUserDataLoader.class);
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserDataLoader(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        loadUsers();
    }

    private void loadUsers() {
        if (appUserRepository.count() == 0) {
            AppUser admin = new AppUser("admin", passwordEncoder.encode("admin123"), "ADMIN");
            AppUser user = new AppUser("user", passwordEncoder.encode("user123"), "USER");
            appUserRepository.save(admin);
            appUserRepository.save(user);
            logger.info("Seeded default users: admin/admin123 and user/user123");
        }
    }
}
