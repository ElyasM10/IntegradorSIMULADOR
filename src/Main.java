//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Clases.*;
import  javax.swing.*;

public class Main {

   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new interfazGrafica().setVisible(true));
    }*/
       public static void main(String[] args) {
           Particion p1 = new Particion(0, 100, 10, true, 1,0);
           Particion p2 = new Particion(5, 200, 15, true, 2,0);
           Particion p3 = new Particion(10, 300, 20, true, 3,0);

           // Imprimir detalles de las particiones
           System.out.println(p1);
           System.out.println(p2);
           System.out.println(p3);

           // Verificación de IDs únicos
           System.out.println("ID de p1: " + p1.getId());
           System.out.println("ID de p2: " + p2.getId());
           System.out.println("ID de p3: " + p3.getId());

           // Comprobar si los IDs son únicos
           if (p1.getId() != p2.getId() && p2.getId() != p3.getId() && p1.getId() != p3.getId()) {
               System.out.println("Todos los IDs son únicos.");
           } else {
               System.out.println("Hay IDs duplicados.");
           }
       }
}

