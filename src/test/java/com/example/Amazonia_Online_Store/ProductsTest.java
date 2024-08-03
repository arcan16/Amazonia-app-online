package com.example.Amazonia_Online_Store;

import com.example.Amazonia_Online_Store.controllers.ProductsController;
import com.example.Amazonia_Online_Store.dto.products.AllDataProductDTO;
import com.example.Amazonia_Online_Store.dto.products.DataProductDTO;
import com.example.Amazonia_Online_Store.models.ProductEntity;
import com.example.Amazonia_Online_Store.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductsTest {

    @Autowired
    private ProductsController productsController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private UriComponentsBuilder uriComponentsBuilder;


    @MockBean
    private HttpServletRequest request;

    private DataProductDTO dataProductDTO;
    private AllDataProductDTO allDataProductDTO;

    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        dataProductDTO = new DataProductDTO("Test Product", "Description", 100.0, 10);
        allDataProductDTO = new AllDataProductDTO(1L, "Test Product", "Description", 100.0, 10);
        productEntity = new ProductEntity(1L, "Test Product", "Description", 100.0, 10);
        allDataProductDTO = new AllDataProductDTO(productEntity);
    }

    @Test
    @DisplayName("Test - Add new Product")
    @WithMockUser(username = "test",roles = {"EMPLOYEE"})
    void testAddNewProduct() throws Exception {
        when(productService.add(any(DataProductDTO.class), any(HttpServletRequest.class)))
                .thenReturn(allDataProductDTO);

        mvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Product\", \"description\": \"Description\", \"price\": 100.0, \"stock\": 10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    @DisplayName("Test - Delete Product")
    @WithMockUser(username = "lenoardo",roles = {"SYS"})
    void deleteProduct() throws Exception {
        doNothing().when(productService).delete(anyLong());

        mvc.perform(MockMvcRequestBuilders.delete("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().json("{\"message\":\"Producto eliminado con exito\"}"));
    }

    @Test
    @DisplayName("Test - Get product data")
    @WithMockUser(username = "test", roles = {"SYS"})
    void getProductData() throws Exception {
        when(productService.findProduct(anyLong())).thenReturn(productEntity);

        mvc.perform(MockMvcRequestBuilders.get("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.stock").value(10));
    }
}
