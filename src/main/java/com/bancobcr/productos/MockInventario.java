package com.bancobcr.productos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MockInventario {

    private List<MockProducto> products;
    private int total;
    private int skip;
    private int limit;
}
