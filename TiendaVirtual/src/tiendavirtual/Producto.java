package tiendavirtual;

import java.util.LinkedList;

/**
 *
 * @author andressaldana
 */
public class Producto {
    int id;
    String nombre;
    String descripcion;
    float precio;
    int existencia;
    float descuento;
    LinkedList<String> imagenes;
    
    public Producto(int id, String nombre, String descripcion, float precio,int existencia, float descuento, LinkedList<String> imagenes){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.descuento = descuento;
        this.existencia = existencia;
        this.imagenes = imagenes;
    }
    
    String getNombre(int id){
        return this.nombre;
    }
    
    String getDescripcion(int id){
        return this.descripcion;
    }
    
    float getPrecio(int id){
        return this.precio;
    }
    
    int getExistencia(int id){
        return this.existencia;
    }
    
    LinkedList<String> getImagenes(){
        return this.imagenes;
    }
            
}
