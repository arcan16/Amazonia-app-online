package com.example.Amazonia_Online_Store.services;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.userDetails.AllDataUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.userDetails.validations.AddCustomerValidations;
import com.example.Amazonia_Online_Store.dto.userDetails.validations.PhoneValidation;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository customersRepository;

    @Autowired
    private List<AddCustomerValidations> validations = new ArrayList<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PhoneValidation phoneValidation;

    @Transactional
    public AllDataUserDetailsDTO register(AddUserDetailsDTO addUserDetailsDTO) {

        validations.forEach(v-> v.validate(addUserDetailsDTO));

        UserEntity user = userRepository.save(new UserEntity(addUserDetailsDTO, passwordEncoder));

        UserDetailsEntity customer = customersRepository.save(new UserDetailsEntity(user, addUserDetailsDTO));

        return new AllDataUserDetailsDTO(user, customer);
    }

    public AllDataUserDetailsDTO getData(HttpServletRequest request) {
        UserEntity user = jwtUtils.getUserFromRequest(request);

        UserDetailsEntity userDetails = userDetailsRepository.findByUserId(user.getId())
                .orElseThrow(()-> new ValidationException("Ocurrio un error al buscar los datos del cliente"));

        return new AllDataUserDetailsDTO(user, userDetails);

    }

    public void deleteUser(HttpServletRequest request) {
        UserEntity user = jwtUtils.getUserFromRequest(request);

        userRepository.deleteById(user.getId());
    }

    public void update(UserEntity user, DataToUpdateUserDTO data) {

        if(data.phone() != null)
            phoneValidation.validate(data.phone());

        UserDetailsEntity customer = userDetailsRepository.findByUserId(user.getId())
                .orElseThrow(()-> new ValidationException("Los datos del empleado no fueron encontrados"));

        customer.update(data);
        userDetailsRepository.save(customer);
    }

    public UserEntity registerEmployee(AddUserDetailsDTO addUserDetailsDTO) {

        validations.forEach(v-> v.validate(addUserDetailsDTO));

        UserEntity user = new UserEntity(addUserDetailsDTO, passwordEncoder);

        user.setRole(ERole.EMPLOYEE);

        userRepository.save(user);

        customersRepository.save(new UserDetailsEntity(user, addUserDetailsDTO));

        return user;
    }
}
