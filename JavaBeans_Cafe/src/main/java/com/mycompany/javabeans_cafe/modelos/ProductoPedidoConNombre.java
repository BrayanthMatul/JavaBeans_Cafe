package com.mycompany.javabeans_cafe.modelos;

public class ProductoPedidoConNombre {

    private ProductoPedido productoPedido;
    private String nombreProducto;

    public ProductoPedidoConNombre(ProductoPedido productoPedido, String nombreProducto) {
        this.productoPedido = productoPedido;
        this.nombreProducto = nombreProducto;
    }

    public ProductoPedido getProductoPedido() {
        return productoPedido;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setProductoPedido(ProductoPedido productoPedido) {
        this.productoPedido = productoPedido;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

}
