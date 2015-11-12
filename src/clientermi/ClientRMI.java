
package clientermi;

import RMI.Request;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientRMI {

    private void showMenu() throws RemoteException, NotBoundException{
        Request request = new Request();
        Scanner scanIn = new Scanner(System.in);
        System.out.print("Digite el tamaño del disco virtual: ");
        int size = Integer.parseInt(scanIn.nextLine());
        String root = request.getService().create(size);
        while(true){
            String path = request.getService().getPath(root);
            System.out.print(path+">");
            String message = scanIn.nextLine();
            //Quita espacios antes de la entrada y espacios entre palabra se reducen a 1
            String[] params = message.trim().replaceAll("( )+", " ").split(" ");
            params[0] = params[0].toLowerCase();
            switch(params[0]){
                case "cd":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parametros en comando");
                            break;
                        }else{
                            boolean accessed = request.getService().cd(params[1], root);
                            if(!accessed){
                                System.out.println("No se pudo encontrar el path");
                                break;
                            }
                        }
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "ls":
                    try{
                        System.out.print(request.getService().ls(root));
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "pwd":
                    try{
                        System.out.println("El directorio actual es: "+request.getService().getPath(root));
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "mv": //MOVE FILE OR DIRECTORY
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parametros en comando");
                            break;
                        }else{
                            boolean moved = request.getService().mv(params,root);
                            if(moved){
                                System.out.println("Directorio creado exitosamente.");
                            }else{
                                System.out.println("No se puede crear directorio con este nombre."); 
                            }
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "mkdir":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parametros en comando");
                            break;
                        }else{
                            boolean created = request.getService().mkdir(params[1],root);
                            if(created){
                                System.out.println("Directorio creado exitosamente.");
                            }else{
                                System.out.println("No se puede crear directorio con este nombre."); 
                            }
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Comando Invalido");
            }
        }
    }
    
    public static void main(String[] args) throws IOException, RemoteException, NotBoundException {
        ClientRMI client = new ClientRMI();
        client.showMenu();
    } 
}
