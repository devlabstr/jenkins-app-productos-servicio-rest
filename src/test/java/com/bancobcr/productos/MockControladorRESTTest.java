package com.bancobcr.productos;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MockControladorRESTTest {
    @Test
    void shouldReturnProductsFromWellFormedJsonString() {
        int limit = 35;

        MockInventario inventario = given()
                .when()
                .get("/mock/productos?limit="+limit+"&skip=0")
                .then()
                .statusCode(200)
                .extract()
                .as(MockInventario.class);

        assertEquals(inventario.getProducts().size(), limit);
    }
}