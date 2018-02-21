package Main;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente{
    public static void main(String args[]){
        try{
            int pto = 8888;
            String dst = "127.0.0.1";
            Socket cl = new Socket(dst,pto);
            Scanner reader = new Scanner(System.in);
            
            System.out.println("Conexion establecida... Bienvenido, porfavor elija una opcion: \n"
                    + "1.-Insertar Persona \n"
                    + "2.-Actualizar Persona \n"
                    + "3.-Consultar Persona \n"
                    + "4.-Eliminar Persona");
            int op = Integer.parseInt(reader.next());        

            String firstName = "", lastName = "", phone = "", birthDate = "", email = "";
            int accion = 0;
            
            
            if(op ==1){
                //caso en que insertamos un usuario
                System.out.println("Ingresa su nombre: ");
                firstName = reader.next();
                System.out.println("Ingresa su apellido paterno: ");
                lastName = reader.next();
                System.out.println("Ingresa su numero de telefono: ");
                phone = reader.next();
                System.out.println("Ingresa su fecha de nacimiento (yyyy-mm-dd): ");
                birthDate = reader.next();
                System.out.println("Ingresa su email: ");
                email = reader.next();
                accion = 1;
                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());

                Person p1  = new Person(firstName,lastName,phone,birthDate,email,accion); 
                System.out.println("Se enviara el objeto con los datos: \n" + p1.getFirstName() +
                " "+ p1.getLastName() + " " + p1.getPhone() + " " + p1.getBirthDate() + " " + p1.getEmail() + " " + p1.getAction());            
                oos.writeObject(p1);
                oos.flush();

                System.out.println("Peticion enviada ");

                ois.close();
                oos.close();
                cl.close();
            }
            else if(op == 2){
                //actualizacion de usuario
                System.out.println("Ingresa correo: ");
                email = reader.next();

                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());

                Person p1  = new Person(firstName,lastName,phone,birthDate,email,2); 
                oos.writeObject(p1);
                oos.flush();

                System.out.println("Peticion enviada");

                //recibiendo los nuevos valores a actualizar
                String newfirstName, newlastName, newphone, newbirthDate;

                Person p2 = (Person)ois.readObject();
                System.out.println("Nombre actual: " + p2.getFirstName());
                newfirstName = reader.next();
                System.out.println("Apellido actual: " + p2.getLastName());
                newlastName = reader.next();
                System.out.println("Telefono actual: " + p2.getPhone());
                newphone = reader.next();
                System.out.println("Fecha de nacimiento actual: " + p2.getBirthDate());
                newbirthDate = reader.next();


                if(newfirstName.equals("-")){
                    newfirstName = p2.getFirstName();
                }
                if(newlastName.equals("-")){
                    newlastName = p2.getLastName();
                }
                if(newphone.equals("-")){
                    newphone = p2.getPhone();
                }
                if(newbirthDate.equals("-")){
                    newbirthDate = p2.getBirthDate();
                }

                Person p1v2  = new Person(newfirstName,newlastName,newphone,newbirthDate,p2.getEmail(),3); 
                System.out.println("Se enviara el objeto con los datos: \n" + p1.getFirstName() +
                " "+ p1.getLastName() + " " + p1.getPhone() + " " + p1.getBirthDate() + " " + p1.getEmail() + " " + p1.getAction());            
                ObjectOutputStream oos1 = new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois1 = new ObjectInputStream(cl.getInputStream());
                oos1.writeObject(p1v2);
                oos1.flush();

                ois1.close();
                oos1.close();
                ois.close();
                oos.close();
                cl.close();               
            }
            else if(op == 3){
                //consulta de usuario
                System.out.println("Ingresa correo: ");
                email = reader.next();
 
                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());

                Person p1  = new Person(firstName,lastName,phone,birthDate,email,2); 
                oos.writeObject(p1);
                oos.flush();

                System.out.println("Peticion enviada");
                Person p2 = (Person)ois.readObject();
                System.out.println("Nombre: " + p2.getFirstName() +
                "\nApellido: "+ p2.getLastName() + "\nTelefono: " + p2.getPhone() + "\nDia de nacimiento: " + p2.getBirthDate() + "\nCorreo: " + p2.getEmail());

                ois.close();
                oos.close();
                cl.close();               
            }
            else{
                //borrado de usuario
                System.out.println("Ingresa correo: ");
                email = reader.next();
                
                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
                
                Person p1  = new Person(firstName,lastName,phone,birthDate,email,4); 
                oos.writeObject(p1);
                oos.flush();
                System.out.println("Peticion enviada");
                
            }
                              



        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

