package Main;
import java.net.*;
import java.io.*;
import java.sql.ResultSet;

public class Servidor {

    public static void main(String[] args)throws Exception{
        ServerSocket ss = new ServerSocket(8888);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            Socket cl = ss.accept();
            System.out.println("Cliente conectado desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            //response
            while(true){
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Person p1 = (Person)ois.readObject();
            System.out.println("objeto recibido con los datos: \n " + p1.getFirstName() +
             " "+ p1.getLastName() + " " + p1.getPhone() + " " + p1.getBirthDate() + " " + p1.getEmail());
            
            String firstName = "", lastName = "", phone = "", birthDate = "", email = "";
            int accion = 0;
            
            int op = p1.getAction();
            ConexionDB db = new ConexionDB();
            db.conectar();
            
            switch (op) {
                //caso en que insertamos un usuario
                case 1:
                        System.out.println("insercion");
                        db.ejecutar("insert into persons(firstname, lastname, phone, birthday, email) values('" +
                        p1.getFirstName() + "','" + p1.getLastName() + "','" + p1.getPhone() +
                        "','" + p1.getBirthDate() + "','" + p1.getEmail() + "');");
                        System.out.println("Insercion exitosa");
                        break;
                //caso de consulta
                case 2:
                        System.out.println("consulta");
                        ResultSet rs = db.consultar("select * from persons where email = '" + p1.getEmail()+
                         "'");
                        if (rs.next() ){
                        firstName = rs.getString("firstname");
                        lastName = rs.getString("lastname");
                        phone = rs.getString("phone");
                        birthDate = rs.getString("birthday");
                        email = rs.getString("email");
                        }
                        //enviamos la respuesta
                        System.out.println("datos a enviar: " + firstName + lastName + phone + birthDate + email);
                        Person p2  = new Person(firstName,lastName,phone,birthDate,email,accion); 
                        oos.writeObject(p2);
                        oos.flush();
                        System.out.println("Respuesta enviada");
                        break;
                //caso de actualizacion
                case 3:
                        System.out.println("actualizacion");
                        db.ejecutar("update persons set firstname = '" +
                        p1.getFirstName() + "', lastname = '" + p1.getLastName() + "', phone = '" + p1.getPhone() +
                        "', birthday = '" + p1.getBirthDate() + "', email = '" + p1.getEmail() + "' where email = '" + p1.getEmail() + "';");
                        System.out.println("Actualizacion exitosa");
                        break;
                //caso de borrado
                case 4:
                        System.out.println("borrado");
                        db.ejecutar("delete from persons where email = '" + p1.getEmail()+ "'");
                        break;
                    
            }
            
            
            }
            
        }
	}
}