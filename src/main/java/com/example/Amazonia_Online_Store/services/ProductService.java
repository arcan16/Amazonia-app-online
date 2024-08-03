package com.example.Amazonia_Online_Store.services;

import com.example.Amazonia_Online_Store.dto.products.AllDataProductDTO;
import com.example.Amazonia_Online_Store.dto.products.DataProductDTO;
import com.example.Amazonia_Online_Store.dto.products.DataToUpdateProductDTO;
import com.example.Amazonia_Online_Store.dto.products.validations.AddProductValidation;
import com.example.Amazonia_Online_Store.models.ProductEntity;
import com.example.Amazonia_Online_Store.models.UserEntity;
import com.example.Amazonia_Online_Store.repositories.ProductRepository;
import com.example.Amazonia_Online_Store.security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private List<AddProductValidation> addProductValidations = new ArrayList<>();

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public AllDataProductDTO add(DataProductDTO dataProductDTO, HttpServletRequest request) {

        UserEntity user = jwtUtils.getUserFromRequest(request);
        if(user != null)
            addProductValidations.forEach(v-> v.validate(dataProductDTO));

        return new AllDataProductDTO(productRepository.save(new ProductEntity(dataProductDTO, user)));

    }


    public ProductEntity findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ValidationException("El producto con id: " + id +" no fue encontrado"));
    }

    public void delete(Long id) {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
        }else {
            throw new ValidationException("El producto con id: " + id + " no fue encontrado");
        }
    }

    public AllDataProductDTO update(DataToUpdateProductDTO data, HttpServletRequest request) {

        UserEntity author = jwtUtils.getUserFromRequest(request);

        ProductEntity productToUpdate = productRepository.findById(data.id())
                        .orElseThrow(()-> new ValidationException("El producto con id: " + data.id() +" no fue encontrado"));

        productToUpdate.update(data, author);

        productRepository.save(productToUpdate);

        return new AllDataProductDTO(productToUpdate);
    }
}
