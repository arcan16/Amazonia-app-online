package com.example.Amazonia_Online_Store.controllers;

import com.example.Amazonia_Online_Store.dto.orders.AllDataOrderDTO;
import com.example.Amazonia_Online_Store.dto.orders.OrderDetailsDTO;
import com.example.Amazonia_Online_Store.dto.orders.OrderStatusDTO;
import com.example.Amazonia_Online_Store.models.OrderEntity;
import com.example.Amazonia_Online_Store.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    /**
     * Permite registrar pedidos del producto
     * @param products Productos que seran agregados a la orden
     * @param request Metodo proporcionado por Java que nos permite recuperar informacion sobre la peticion
     * @param uriComponentsBuilder Objeto de Java que nos permite crear una url dinamica con el registro creado
     * @return Mensaje con codigo 201 y la url para consultar los datos del registro
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> addOrder(@RequestBody @Valid List<OrderDetailsDTO>  products,
                                      HttpServletRequest request,
                                      UriComponentsBuilder uriComponentsBuilder){

        OrderEntity order = orderService.add(products, request);

        URI url = uriComponentsBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(url).build();
    }

    /**
     * Obtiene todas las ordenes del usuario que realiza la peticion
     * @param request Objeto proporcionado por java con metodos que nos permiten recuperar informacion sobre la peticion
     * @return Retorna los registros encontrados en la base de datos
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getAllOrders(HttpServletRequest request){

        return ResponseEntity.ok(orderService.getAll(request));
    }

    /**
     * Permite consultar un registro basandonos en el usuario y el id del registro
     * @param id Identificador del registro que sera consultado
     * @param request Objeto java que nos permite recuperar informacion sobre la peticion
     * @return Objeto de tipo AllDataOrderDTO con la informacion encontrada en la base de datos
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleOrder(@PathVariable Long id,
                                            HttpServletRequest request){

        AllDataOrderDTO dataOrder = orderService.getOrderById(id, request);

        return ResponseEntity.ok(dataOrder);
    }

    /**
     * Elimina un registro de la base de datos
     * @param id Identificador del registro que se quiere eliminar
     * @param request Objeto Java que nos permite recuperar informacion sobre la base de datos
     * @return Mensaje con codigo 202 y confirmacion de la correcta eliminacion del registro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id,
                                         HttpServletRequest request){

        orderService.delete(id, request);

        return ResponseEntity.accepted().body("{\"message\":\"El registro fue eliminado correctamente\"}");
    }

    /**
     * Permite actualizar el estatus de una orden
     * @param orderStatus Estatus de tipo EStatus que sera asignado al registro
     * @return Mensaje con codigo 200 en caso de exito
     */
    @PutMapping("/status")
    @PreAuthorize("hasAnyRole('SYS','EMPLOYEE')")
    public ResponseEntity<?> updateOrder(@RequestBody @Valid OrderStatusDTO orderStatus){

        orderService.updateStatus(orderStatus);

        return ResponseEntity.ok().build();
    }

    /**
     * Permite actualizar un registro por parte del cliente
     * @param orderStatus Valor de tipo EStatus que sera asignado al registro
     * @param request Objeto de java que nos permite recuperar informacion sobre la peticion
     * @return Codigo 202 en caso de exito
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> updateCustomerOrder(@RequestBody @Valid OrderStatusDTO orderStatus,
                                                 HttpServletRequest request){
        System.out.println();
        orderService.updateCustomerOrderStatus(orderStatus, request);

        return ResponseEntity.accepted().build();
    }
}
