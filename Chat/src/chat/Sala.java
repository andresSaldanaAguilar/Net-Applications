package chat;

import java.io.Serializable;

/**
 *
 * @author andressaldana
 */
public class Sala implements Serializable{
    private String nombre;
    private String contrasenia;
    private int puerto;
    
    public Sala(String nombre, String contrasenia, int puerto){
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.puerto = puerto;
    }
    String getNombre(){
        return this.nombre;
    }
    
    String getContrasenia(){
        return this.contrasenia;
    }
    
    int getPuerto(){
        return this.puerto;
    }
}
