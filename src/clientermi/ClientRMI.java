
package clientermi;

import RMI.Request;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientRMI {

    private void showMenu(){
        while(true){
            System.out.println("****************************");
            System.out.println("\t    Menú");
            System.out.println("1.CREATE.");
            System.out.println("2.MKDIR.");
            System.out.println("3.FILE.");
            System.out.println("4.CD.");
            System.out.println("5.LS.");
            System.out.println("6.DU.");
            System.out.println("7.PWD.");
            System.out.println("8.CAT.");
            System.out.println("9.CPY.");
            System.out.println("10.MV.");
            System.out.println("11.RM.");
            System.out.println("12.FIND.");
            System.out.println("13.TREE.");
            System.out.println("14.EXIT.");
            System.out.println("****************************\n");
            
            System.out.print("Digite una opción: ");
            Scanner scanIn = new Scanner(System.in);
            String opc = scanIn.nextLine();
            
            switch(opc){
                case "1":
                    try{
                        Request request = new Request();
                        request.setRegistry("127.0.0.1", 1099);
                        request.setService("CREATE");
                        System.out.print("Digite el tamaño del disco virtual: ");
                        int size = Integer.parseInt(scanIn.nextLine());
                        request.getService().create(size);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    try{
                        Request request = new Request();
                        request.setRegistry("127.0.0.1", 1099);
                        request.setService("MKDIR");
                        System.out.print("Digite el nombre del directorio: ");
                        String dirName = scanIn.nextLine();
                        request.getService().MKDIR(dirName);
                    }catch(RemoteException | NotBoundException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "14":
                    System.exit(0);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        ClientRMI client = new ClientRMI();
        client.showMenu();
    } 
}
