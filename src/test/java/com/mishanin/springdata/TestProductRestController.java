package com.mishanin.springdata;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.errors.AccessDeniedProductException;
import com.mishanin.springdata.services.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestProductRestController {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productsService;

    // https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html

    @Test
    public void exampleTest(){
        List<Product> list = this.restTemplate.getForObject("/api/v1/products/",List.class);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void testMethodCreateProduct(){

        Product product = new Product();
        product.setTitle("product");
        product.setPrice(new BigDecimal(100));

        ResponseEntity<Product> response = this.restTemplate.postForEntity("/api/v1/products/", product, Product.class);
        Assert.assertNull(product.getId());

        assertThat(response.getBody().getId()).isGreaterThan(0);
        assertThat(response.getBody().getTitle())
                .as("title for product is %s", response.getBody().getTitle())
                .isEqualTo(product.getTitle());
        assertThat(response.getBody().getPrice())
                .as("price for product is %d", response.getBody().getPrice().intValue())
                .isEqualTo(new BigDecimal(100));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        product.setId(1L);

        assertThat(this.restTemplate.postForEntity("/api/v1/products/", product, Product.class).getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Test
    public void getAllProductsTest() throws Exception {
        List<Product> allProducts = Arrays.asList(
                new Product(1L, "Milk", new BigDecimal(90)),
                new Product(2L, "Bread", new BigDecimal(25)),
                new Product(3L, "Cheese", new BigDecimal(320))
        );

        given(productsService.findAll()).willReturn(allProducts);

        mvc.perform(get("/api/v1/products/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title", is(allProducts.get(0).getTitle())))
                .andExpect(jsonPath("$[:2]", hasSize(2)))
                .andExpect(jsonPath("$[?(@.price < 50)]", hasSize(1)))
                .andExpect(jsonPath("$[-1:].title").isNotEmpty());
    }
}
