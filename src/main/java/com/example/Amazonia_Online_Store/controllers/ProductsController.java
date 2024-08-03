package com.example.Amazonia_Online_Store.controllers;

import com.example.Amazonia_Online_Store.dto.products.AllDataProductDTO;
import com.example.Amazonia_Online_Store.dto.products.DataProductDTO;
import com.example.Amazonia_Online_Store.dto.products.DataToUpdateProductDTO;
import com.example.Amazonia_Online_Store.models.ProductEntity;
import com.example.Amazonia_Online_Store.repositories.ProductRepository;
import com.example.Amazonia_Online_Store.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/products")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'SYS')")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Permite agregar un nuevo producto a la base de datos
     * @param dataProductDTO Informacion proporcionada dentro de la peticion
     * @return Objeto de tipo AllDataProductDTO con la informacion del registro creado, en caso de exito
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody @Valid DataProductDTO dataProductDTO,
                                        UriComponentsBuilder uriComponentsBuilder,
                                        HttpServletRequest request){

        AllDataProductDTO newProduct = productService.add(dataProductDTO, request);

        URI url = uriComponentsBuilder.path("/products/{id}").buildAndExpand(newProduct.id()).toUri();

        return ResponseEntity.created(url).body(newProduct);
    }

    /**
     * Obtiene una pagina con maximo 30 de los registros de la tabla Products
     * @param page Interfaz de Java que nos permite definir las dimensiones maximas para el contenido de la consulta
     * @return Informacion encontrada en la tabla
     */
    @GetMapping
    public ResponseEntity<?> getAllProducts(@PageableDefault(size = 30) Pageable page){
        Page<AllDataProductDTO> data = productRepository.findAll(page).map(AllDataProductDTO::new);
        if(data.isEmpty())
            throw new ValidationException("No hay productos que mostrar");
        return ResponseEntity.ok(data);
    }

    /**
     * Obtiene la inforamcion del registro que conicida con el id recibido en la peticion
     * @param id Identificador del registro que sera buscado en el banco de datos
     * @return Informacion con la estructura definida en el record AllDataProductDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){

        return ResponseEntity.ok(new AllDataProductDTO(productService.findProduct(id)));
    }

    /**
     * Permite eliminar un registro de la tabla Products
     * @param id Identificador del registro que se requiere eliminar
     * @return Mensaje de confirmacion en caso de exito
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){

        productService.delete(id);

        return ResponseEntity.accepted().body("{\"message\":\"Producto eliminado con exito\"}");
    }

    /**
     * Permite actualizar la informacion del producto indicado en la peticion
     * @param data Informacion recibida en la peticion con los datos que seran actualziados
     * @param request Metodo de Java que nos proporciona metodos para recuperar informacion de la peticion
     * @return Objeto de tipo AllDataProductDTO con la inforamcion completa del registro actualizado
     */
    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody @Valid DataToUpdateProductDTO data,
                                           HttpServletRequest request){

        AllDataProductDTO productUpdated = productService.update(data, request);

        return ResponseEntity.ok(productUpdated);
    }
}
