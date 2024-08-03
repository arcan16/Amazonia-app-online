package com.example.Amazonia_Online_Store.services;

import com.example.Amazonia_Online_Store.dto.userDetails.AllDataUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.users.DataToUpdateUserDTO;
import com.example.Amazonia_Online_Store.models.ERole;
import com.example.Amazonia_Online_Store.models.UserDetailsEntity;
import com.example.Amazonia_Online_Store.models.UserEntity;
import com.example.Amazonia_Online_Store.repositories.UserDetailsRepository;
import com.example.Amazonia_Online_Store.repositories.UserRepository;
import com.example.Amazonia_Online_Store.security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public void update(HttpServletRequest request, DataToUpdateUserDTO data) {
        UserEntity user = jwtUtils.getUserFromRequest(request);

        Optional<UserEntity> userCoincidence = userRepository.getReferenceByUsername(user.getUsername(), user.getId());

        if(userCoincidence.isPresent())
            throw new ValidationException("El usuario ya existe, no es posible utilizarlo");

        user.update(data, passwordEncoder);

        userRepository.save(user);

        userDetailsService.update(user, data);

    }

    public List<AllDataUserDetailsDTO> getAll(Pageable page) {
        Page<UserDetailsEntity> userDetails = userDetailsRepository.findAll(page);

        return userDetails.stream().map(AllDataUserDetailsDTO::new).toList();
    }

    public List<AllDataUserDetailsDTO> getAllCustomers(Pageable page) {
        Page<UserDetailsEntity> userDetails = userDetailsRepository.findAllCustomers(page);

        return userDetails.stream().map(AllDataUserDetailsDTO::new).toList();
    }

    public List<AllDataUserDetailsDTO> getAllEmployees(Pageable page) {
        Page<UserDetailsEntity> userDetails = userDetailsRepository.findAllEmployees(page);

        return userDetails.stream().map(AllDataUserDetailsDTO::new).toList();
    }

    public void updateEmployee(DataToUpdateUserDTO data, HttpServletRequest request) {

        UserEntity userRequest = jwtUtils.getUserFromRequest(request);

        if(userRequest.getRole().equals(ERole.SYS)){
            if(data.id() != null){
                UserEntity userToUpdate = userRepository.findById(data.id())
                        .orElseThrow(()-> new UsernameNotFoundException("El usuario indicado no existe"));

                userToUpdate.update(data, passwordEncoder);

                userDetailsService.update(userToUpdate, data);

            }else{
                userRequest.update(data, passwordEncoder);

                userDetailsService.update(userRequest, data);
            }
        }else{
            userRequest.update(data, passwordEncoder);

            userDetailsService.update(userRequest, data);
        }
    }
}
