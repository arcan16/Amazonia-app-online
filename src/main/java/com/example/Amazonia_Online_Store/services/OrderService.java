package com.example.Amazonia_Online_Store.services;

import com.example.Amazonia_Online_Store.dto.orders.AllDataOrderDTO;
import com.example.Amazonia_Online_Store.dto.orders.OrderDetailsDTO;
import com.example.Amazonia_Online_Store.dto.orders.OrderStatusDTO;
import com.example.Amazonia_Online_Store.dto.orders.ProductOrderDetailsDTO;
import com.example.Amazonia_Online_Store.models.*;
import com.example.Amazonia_Online_Store.repositories.OrderDetailRepository;
import com.example.Amazonia_Online_Store.repositories.OrderRepository;
import com.example.Amazonia_Online_Store.repositories.ProductRepository;
import com.example.Amazonia_Online_Store.security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public OrderEntity add(List<OrderDetailsDTO> products, HttpServletRequest request) {

        UserEntity user = jwtUtils.getUserFromRequest(request);

        List<ProductOrderDetailsDTO> productList = getOrderDetailsQuantityList(products);

        isValidStock(productList);

        double total = productList.stream().map(product-> product.price() * product.quantity())
                .mapToDouble(Double::doubleValue).sum();

        OrderEntity order = new OrderEntity(user, total);

        orderRepository.save(order);

        List<OrderDetailEntity> orderDetailList =
                productList.stream().map(pl -> new OrderDetailEntity(order, pl)).toList();

        order.setOrderDetails(orderDetailList);

        updateProductStock(products);

        return order;
    }

    public void isValidStock(List<ProductOrderDetailsDTO> productOrderDetails){
        Map<Long, String> insufficientStock = new HashMap<>();

        productOrderDetails.forEach(pod -> {
            if(pod.quantity() > pod.stock()){
                insufficientStock.put(pod.product().getId()," " + pod.product().getName());
            }
        });

        if(!insufficientStock.isEmpty()){
            throw new ValidationException("Cantidad no disponible para: " + insufficientStock);
        }
    }

    private void updateProductStock(List<OrderDetailsDTO> products) {

        List<Long> productIdList = products.stream().map(OrderDetailsDTO::idProduct).toList();

        List<ProductEntity> productList = productRepository.findAllByIdIn(productIdList);


        productList.forEach(product -> {
            products.stream().filter(ps -> ps.idProduct() == product.getId())
                    .forEach(ps -> {
                        product.setStock(product.getStock() - ps.quantity());
                    });
        });
    }

    public List<Long> getProductIdList(List<OrderDetailEntity> orderDetail){
        return orderDetail.stream().map(OrderDetailEntity::getId).toList();
    }

    public List<ProductEntity> getProductEntityList(List<Long> productIdList){
        return productRepository.findAllByIdIn(productIdList);
    }

    public List<ProductOrderDetailsDTO> getOrderDetailsQuantityList(List<OrderDetailsDTO> products){

        List<Long> productIdList = new ArrayList<>();

        products.forEach(p-> productIdList.add(p.idProduct()));

        List<ProductEntity> productList = getProductEntityList(productIdList);

        if(products.size() == productList.size()){
            return productList.stream()
                    .flatMap(pl -> products.stream()
                            .filter(p -> p.idProduct().equals(pl.getId()))
                            .map(p -> new ProductOrderDetailsDTO(p, pl))
                    )
                    .collect(Collectors.toList());
        }

        productIdList.removeAll(productList.stream().map(ProductEntity::getId).toList());

        if(productIdList.size()>1){
            throw new ValidationException("No se encontraron coincidencias para los siguientes id: "
                    + productIdList);
        }else{
            throw new ValidationException("No se encontraron coincidencias para el siguiente id: "
                    + productIdList);
        }
    }

    public List<AllDataOrderDTO> getAll(HttpServletRequest request) {

        UserEntity user = jwtUtils.getUserFromRequest(request);

        List<OrderEntity> ordersList = orderRepository.findAllByUserId(user.getId());

        return ordersList.stream().map(AllDataOrderDTO::new).toList();
    }

    public AllDataOrderDTO getOrderById(Long id, HttpServletRequest request) {

        UserEntity user = jwtUtils.getUserFromRequest(request);

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(()-> new ValidationException("El registro indicado no existe"));

        System.out.println("Datos para consulta " + user.getUsername() + "\n" +
                " " + order.getId());

        if(user.getRole().equals(ERole.SYS) || user.getRole().equals(ERole.EMPLOYEE)){
            return new AllDataOrderDTO(order);
        }else if(user.getRole().equals(ERole.CUSTOMER) && order.getUser().getId().equals(user.getId())){
            return new AllDataOrderDTO(order);
        }else{
            throw new ValidationException("Solo puedes consultar tus propias ordenes");
        }
    }

    public void delete(Long id, HttpServletRequest request) {
        UserEntity user = jwtUtils.getUserFromRequest(request);

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(()-> new ValidationException("El registro indicado no fue encontrado"));

        if(user.getRole().equals(ERole.SYS) || user.getRole().equals(ERole.EMPLOYEE)){
            deleteOrder(order);
        }else if(user.getId().equals(order.getUser().getId())){
            deleteOrder(order);
        }else{
            throw new ValidationException("Solo es posible eliminar tus propios registros");
        }
    }

    @Transactional
    public void deleteOrder(OrderEntity order){

        updatePlusOrder(order);

        orderRepository.delete(order);
    }

    public void updatePlusOrder(OrderEntity order){

        List<Long> productsId = order.getOrderDetails().stream().map(p-> p.getProduct().getId()).toList();

        List<ProductEntity> productsList = productRepository.findAllByIdIn(productsId);

        productsList.forEach(product ->{
            order.getOrderDetails().stream()
                    .filter(po -> po.getProduct().getId().equals(product.getId()))
                    .forEach(po -> {
                        System.out.println("Producto: " + product.getName());
                        System.out.println("Stock: " + product.getStock());
                        System.out.println("Product Order Quantity: " + po.getQuantity());
                        product.setStock(product.getStock() + po.getQuantity());
                    });
        });

        productRepository.saveAll(productsList);
    }

    public void updateStatus(OrderStatusDTO orderStatus) {
        OrderEntity order = orderRepository.findById(orderStatus.id())
                .orElseThrow(()-> new ValidationException("El registro indicado no existe"));

        if(order.getStatus().equals(EStatus.CREATED) && orderStatus.status().equals(EStatus.SEND))
            order.setStatus(EStatus.SEND);
        if(order.getStatus().equals(EStatus.SEND) && orderStatus.status().equals(EStatus.DELIVERED))
            order.setStatus(EStatus.DELIVERED);
        if(order.getStatus().equals(EStatus.CREATED) && orderStatus.status().equals(EStatus.CANCELED)){
            updatePlusOrder(order);
            order.setStatus((EStatus.CANCELED));
        }
        orderRepository.save(order);
    }

    public void updateCustomerOrderStatus(OrderStatusDTO orderStatus, HttpServletRequest request) {

        UserEntity user = jwtUtils.getUserFromRequest(request);

        OrderEntity order = orderRepository.findById(orderStatus.id())
                .orElseThrow(()-> new ValidationException("El registro indicado no existe"));

        if(!user.getId().equals(order.getUser().getId()))
            throw new ValidationException("Solo es posible modificar tus propias ordenes");


        if(order.getStatus().equals(EStatus.DELIVERED) && orderStatus.status().equals(EStatus.HANDBACK)){
            updatePlusOrder(order);
            order.setStatus(EStatus.HANDBACK);
        }
        if(order.getStatus().equals(EStatus.CREATED) && orderStatus.status().equals(EStatus.CANCELED)){
            updatePlusOrder(order);
            order.setStatus(EStatus.CANCELED);
        }
        if(order.getStatus().equals(EStatus.DELIVERED) && orderStatus.status().equals(EStatus.CANCELED))
            throw new ValidationException("No es posible cancelar cuando ya se ha entregado, solo puedes regresar");

        orderRepository.save(order);
    }
}
