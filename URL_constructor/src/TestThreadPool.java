import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andressaldana
 */
public class TestThreadPool {  
    
    //public void 
    
    public static void main(String[] args) {
        System.out.println("Enter webpage to download: ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);
        String url = "";
        try {
            url = br.readLine();
        } catch (IOException ex) {}
        
        Getter g = new Getter(url);
        g.run();
        LinkedList<String> li = g.getQueue();
        for(String item:li){
            System.out.println("queue element: "+ item);
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads  
        for (int i = 0; i < 5; i++) {  
            //WorkerThread worker = new WorkerThread("" + i);
            Getter g1 = new Getter(li.get(i));
            //g1.run();
            executor.execute(g1);//calling execute method of ExecutorService
            //li.add(worker.getMessage());
        }  
        executor.shutdown();  
        while (!executor.isTerminated()) {}    
        System.out.println("Finished all threads");

    }  
 }  