import java.io.*;
public class Dato implements Serializable{ //The serializable class converts an object in a bunch of bytes
	String nombre;
	int edad;
	transient String telefono;
	transient float sueldo;
	public Dato(String n, int e, String t, float s){
		this.nombre = n;
		this.edad = e;
		this.telefono = t;
		this.sueldo = s;
	}
	String getNombre(){
		return this.nombre;
	}	
	int getEdad(){
		return this.edad;
	}
	String getTelefono(){
		return this.telefono;
	}
	float getSueldo(){
		return this.sueldo;
	}
}