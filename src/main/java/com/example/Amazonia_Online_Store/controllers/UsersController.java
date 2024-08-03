package com.example.Amazonia_Online_Store.controllers;

import com.example.Amazonia_Online_Store.dto.userDetails.AddUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.userDetails.AllDataUserDetailsDTO;
import com.example.Amazonia_Online_Store.dto.users.DataToUpdateUserDTO;
import com.example.Amazonia_Online_Store.models.UserEntity;
import com.example.Amazonia_Online_Store.services.UserDetailsService;
import com.example.Amazonia_Online_Store.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;


    /**
     * Permite que los clientes se registren en nuestra aplicacion
     * @param addUserDetailsDTO Informacion proporcionada por el cliente para realizar el registro
     * @return Objeto de tipo AllDataUserCustomerDTO con la inforamcion del registro creado
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid AddUserDetailsDTO addUserDetailsDTO){


        AllDataUserDetailsDTO data = userDetailsService.register(addUserDetailsDTO);

        return ResponseEntity.ok(data);
    }

    /**
     * Permite obtener la informacion del registro del cliente
     * @param request Objeto java que nos permite obtener informacion sobre la peticion
     * @return Mensaje con codigo 200 acompañado de la informacion encontrada en el registro
     * dentro de un objeto AllDataUserDetailsDTO
     */
    @GetMapping
    public ResponseEntity<?> getCustomerData(HttpServletRequest request){

        AllDataUserDetailsDTO dataUserCustomerDTO = userDetailsService.getData(request);

        return ResponseEntity.ok(dataUserCustomerDTO);
    }

    /**
     * Permite eliminar el registro del usuario que realiza la solicitud
     * @param request Objeto de java que nos permite recuperar inforamcion sobre la peticion
     * @return Mensaje con codigo 202 y confiramcion de la eliminacion
     */
    @DeleteMapping
    public ResponseEntity<?> autoDelete(HttpServletRequest request){

        userDetailsService.deleteUser(request);

        return ResponseEntity.accepted().body("{\"message\":\"Usuario eliminado correctamente\"}");
    }

    /**
     * Permite actualizar los datos de un registro
     * @param data Informacion recibida a traves de la peticion para realizar la actualizacion de datos
     * @param request Objeto de java que nos permite recuperar informacion sobre la peticion
     * @return Mensaje con codigo 200 en caso de exito
     */
    @PutMapping
    public ResponseEntity<?> updataUser(@RequestBody @Valid DataToUpdateUserDTO data,
                                        HttpServletRequest request){

        userService.update(request, data);

        return ResponseEntity.ok().build();
    }

    /**
     * Permite registrar un nuevo empleado en la base de datos
     * @param addUserDetailsDTO Informacion recibida en la peticion para realizar el registro de informacion
     * @param uriComponentsBuilder Objeto de java que nos permite crear la url dinamica
     * @return Mensaje de confirmacion mas la url en la que se podran consultar los datos del registro creado
     */

    @PostMapping("/registeremployees")
    @PreAuthorize("hasAnyRole('SYS')")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid AddUserDetailsDTO addUserDetailsDTO,
                                         UriComponentsBuilder uriComponentsBuilder){
        System.out.println("Llegando al registro");
        UserEntity user = userDetailsService.registerEmployee(addUserDetailsDTO);

        URI url = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(url).build();
    }

    /**
     * Permite obtener la informacion de todos los usuarios registrados en la base de datos
     * @param page Objeto de java que nos permite definir las caracteristicas de la pagina, como
     *             tamaño por ejemplo
     * @return Mensaje con codigo 200 y la informacion encontrada en caso de exito
     */
    @GetMapping("/allusers")
    @PreAuthorize("hasAnyRole('SYS')")
    public ResponseEntity<?> getAllUsers(@PageableDefault(size = 30) Pageable page){

        List<AllDataUserDetailsDTO> dataList = userService.getAll(page);

        return ResponseEntity.ok(dataList);

    }

    /**
     * Obtiene la informacion de todos los usuarios de tipo CUSTOMER registrados
     * @param page Objeto que nos permmite definir el tamaño de la pagina que sera creada
     *             con la informacion encontrada en la base de datos
     * @return Mensaje con codigo 200 y la informacion encontrada
     */
    @GetMapping("/allcustomers")
    @PreAuthorize("hasAnyRole('SYS')")
    public ResponseEntity<?> getAllCusomers(@PageableDefault(size = 30) Pageable page){

        List<AllDataUserDetailsDTO> dataList = userService.getAllCustomers(page);

        return ResponseEntity.ok(dataList);

    }

    /**
     * Permite consultar todos los registro de los empelados
     * @param page Objeto que nos permite definir el tamaño de la pagina que sera creada con los registros
     *             encontrados
     * @return Mensaje con codigo 200 y la informacion encontrada en la base de datos
     */
    @GetMapping("/allemployees")
    @PreAuthorize("hasAnyRole('SYS')")
    public ResponseEntity<?> getAllEmployees(@PageableDefault(size = 30) Pageable page){

        List<AllDataUserDetailsDTO> dataList = userService.getAllEmployees(page);

        return ResponseEntity.ok(dataList);

    }

    /**
     * Permite actualizar la informacion de un registro en la base de datos
     * @param dataToUpdateUserDTO Informacion recibida en la peticion realizada por el cliente
     * @param request Objeto de java que nos permite obtener inforamcion sobre la peticion recibida
     * @return Mensjae con codigo 200 en caso de exito
     */
    @PutMapping("/employees")
    @PreAuthorize("hasAnyRole('SYS','EMPLOYEE')")
    public ResponseEntity<?> updateEmployee(@RequestBody DataToUpdateUserDTO dataToUpdateUserDTO,
                                            HttpServletRequest request){

        userService.updateEmployee(dataToUpdateUserDTO, request);

        return ResponseEntity.ok().build();

    }
}