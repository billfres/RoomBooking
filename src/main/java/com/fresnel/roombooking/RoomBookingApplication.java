package com.fresnel.roombooking;

import com.fresnel.roombooking.exception.RoleAlreadyExistException;
import com.fresnel.roombooking.model.Role;
import com.fresnel.roombooking.model.User;
import com.fresnel.roombooking.repository.RoleRepository;
import com.fresnel.roombooking.service.IRoleService;
import com.fresnel.roombooking.service.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class RoomBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomBookingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(IRoleService roleService, RoleRepository roleRepository,
                                        IUserService userService) {
        return args -> {
            Role role = new Role();
            role.setId(1L);
            role.setName("user");
            //roleRepository.save(R);
            try {
                roleService.createRole(role);
            }catch (RoleAlreadyExistException ro){
                System.out.println("role already exists"+ ro.getMessage());
            }

            Role role1 = new Role();
            role1.setId(2L);
            role1.setName("Admin");
            try {
                roleService.createRole(role1);
            }catch (RoleAlreadyExistException ro){
                System.out.println("role already exists"+ ro.getMessage());
            }

            /*User user = new User();
            //user.setId(1L);
            user.setFirstName("bill");
            user.setLastName("fres");
            user.setEmail("billy@gmail.com");
            user.setPassword("1234");
            Role userRole = roleRepository.findByName("ROLE_USER").get();
            user.setRoles(Collections.singletonList(userRole));
            userService.registerUser(user);*/

        };
    }
}
