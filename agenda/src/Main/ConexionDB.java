package Main;
import java.sql.*;
/**
 *
 * @author ANDRES
 */
public class ConexionDB {
    
    private Connection conexion;   
   
    //metodo para conectarse a la base de datos
    public ConexionDB conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //aqui especificamos localhost, base de datos, usuario y contraseña
            String BaseDeDatos = "jdbc:mysql://localhost/agenda?user=root&password=Keane1234";
            setConexion(DriverManager.getConnection(BaseDeDatos));
            if(getConexion() != null){
                System.out.println("Conexion Exitosa!");
            }else{
                System.out.println("Conexion Fallida!");                
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
     }
        return this;
    }
    
    public Connection getConexion() {
            return conexion;
    }    
    public void setConexion(Connection conexion) {
            this.conexion = conexion;
    }
    
    //este metodo se encarga de realizar inserts,update y delete (sin retorno)
    public boolean ejecutar(String sql) {
        try {
            Statement sentencia = getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            sentencia.executeUpdate(sql);
            sentencia.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }        return true;
    }
    public ResultSet consultar(String sql) {
        ResultSet resultado;
        try {
            Statement sentencia = getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultado = sentencia.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }        return resultado;
    }

}
