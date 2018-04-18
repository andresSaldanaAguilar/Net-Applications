/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.util.LinkedList;

/**
 *
 * @author andressaldana
 */
public class AliveRooms {
    LinkedList<Sala> ll;
    
    public AliveRooms(){
        ll = new LinkedList();
        Sala primersala = new Sala("Global","",5677);
        ll.add(primersala);
    }
    
    void addRooms(Sala sala){
        ll.add(sala);
    }
    
    String[] getRoomInfo(String nombre){
        String arr [] = new String[2];
        for(int i = 0; i < ll.size(); i++){
            if(ll.get(i).getNombre().equals(nombre)){                
                arr[0] = ""+ll.get(i).getPuerto();
                arr[1] = ll.get(i).getContrasenia();
                break;
            }
        }
        return arr;
    }
    
}
