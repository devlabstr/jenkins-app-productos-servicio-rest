package com.bancobcr.productos;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.ArrayList;

@Path("/productos")
public class ProductosControladorREST {

    private ArrayList<Producto> inventario = new ArrayList<>();

    @PostConstruct
    public void inicializador() {
        inventario.add(new Producto(1, "Producto 1", 10));
        inventario.add(new Producto(2, "Producto 2", 20));
        inventario.add(new Producto(3, "Producto 3", 30));
    }

    @GET
    public String obtenerProductos() {
        return "Lista de productos : " + inventario.toString();
    }

    @GET
    @Path("/{id}")
    public String obtenerProducto(@PathParam("id") int identificador) {
        Producto referencia = new Producto(identificador, "", 0);
        int indice = inventario.indexOf(referencia);
        if (indice != -1)
            return inventario.get(indice).toString();

        return "No existe un producto con id: " + identificador;
    }




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
            @APIResponse(responseCode = "201",
                          description = "Created",
                          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @APIResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensajeError.class)))
    }
    )
    @Tag(name = "CRUD de productos", description = "Contiene todas las operaciones del CRUD de productos")
    public Response agregaProducto(Producto producto) {
        if (producto != null && !inventario.contains(producto)) {
            return inventario.add(producto)?
                    Response.status(201)
                            .entity(producto)
                            .build() :
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new MensajeError("Producto con id = " + producto.getId() + " NO incluido!","ERROR"))
                            .build();

                    //"Producto con id = " + producto.getId() + " incluido!":
                    //"Producto con id = " + producto.getId() + " NO incluido!";
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new MensajeError("Producto con id = " + producto.getId() + " NO incluido!","ERROR"))
                .build();

        //return "Imposible incluir producto: " + producto;
    }





    @DELETE
    @Path("/{id}")
    @Tag(name = "Operaciones de Borrado", description = "Contiene todas las operaciones de borrado")
    public String borrarProducto(@PathParam("id") int identificador) {
       Producto producto = new Producto(identificador,"",0);

       return inventario.remove(producto)?
               "Producto con id = " + identificador + " eliminado con exito!":
               "No se pudo eliminar el producto con id = " + identificador;
    }



    @PUT
    @Path("/{id}")
    @Tag(name = "CRUD de productos", description = "Contiene todas las operaciones del CRUD de productos")
    public String actualizarProducto(@PathParam("id") int identificador, Producto productoActualizar){
        Producto producto = new Producto(identificador,"",0);

        int indiceProducto = inventario.indexOf(producto);

        if(productoActualizar!=null){
            if(indiceProducto!=-1){
                inventario.set(indiceProducto, productoActualizar);
                return "Producto con id = " + identificador + " actualizado con exito PUT";
            }else{
                return agregaProducto(productoActualizar).toString();
            }
        }
        return "No es posible actualizar el producto PUT";
    }

    @PATCH
    @Path("/{id}")
    public String actualizarProductoParcialmente(@PathParam("id") int identificador, Producto productoActualizar) {
        Producto producto = new Producto(identificador, "", 0);

        int indiceProducto = inventario.indexOf(producto);

        if (productoActualizar != null && indiceProducto != -1) {
            if(productoActualizar.getId()!=0){
                inventario.get(indiceProducto).setId(productoActualizar.getId());
            }
            if(productoActualizar.getCantidad()!=0){
                inventario.get(indiceProducto).setCantidad(productoActualizar.getCantidad());
            }
            if(productoActualizar.getNombre()!=null){
                inventario.get(indiceProducto).setNombre(productoActualizar.getNombre());
            }
            return "Producto con id = " + identificador + " actualizado con exito PATCH";
        }
        return "No es posible actualizar el producto PATCH";
    }





}
