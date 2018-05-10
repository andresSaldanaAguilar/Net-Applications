
import java.util.concurrent.Semaphore;

public class Restaurante {

    private Semaphore mesas;
    private int turno;

    public Restaurante(int mesasCount) {
        // Create a semaphore using number of mesas we have
        this.mesas = new Semaphore(mesasCount);
        turno = 0;
    }

    public void getMesa(int customerID) {
        try {
            System.out.println("Cliente #" + customerID + " esta intentando obtener una mesa.");
            // Acquire a permit for a table
            mesas.acquire();
            System.out.println("Cliente #" + customerID + " consiguio una mesa.");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void returnMesa(int customerID) {
        System.out.println("Cliente #" + customerID + " devolvio mesa.");
        mesas.release();
    }
    
    public int añadirTurno(){
        turno++;
        return turno;
    }

    public static void main(String[] args) {
        // Create a restaurant with two dining mesas
        Restaurante r = new Restaurante(2);
        // Create five customers
        for (int i = 1; i <= 5; i++) {
            Cliente c = new Cliente(r, i);
            c.start();
        }
    }
}
