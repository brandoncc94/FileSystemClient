
package clientermi;

import RMI.Request;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientRMI {

    private void showMenu() throws RemoteException, NotBoundException{
        Request request = new Request();
        Scanner scanIn = new Scanner(System.in);
        System.out.print("Digite el tamaño(Kb) del disco virtual: ");
        int size = Integer.parseInt(scanIn.nextLine());
        String root = request.getService().create(size);
        while(true){
            String path = request.getService().getActualPath(root);
            System.out.print(path+">");
            String message = scanIn.nextLine();
            //Quita espacios antes de la entrada y espacios entre palabra se reducen a 1
            String[] params = message.trim().replaceAll("( )+", " ").split(" ");
            params[0] = params[0].toLowerCase();
            switch(params[0]){
                case "cd":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parámetros en comando");
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
                        System.out.println("El directorio actual es: "+request.getService().getActualPath(root));
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "mv": //MOVE FILE OR DIRECTORY
                    try{
                        if(!(params.length >= 3)){
                            System.out.println("Faltan parámetros en comando");
                            break;
                        }else{
                            boolean moved = request.getService().mv(params,root);
                            if(moved){
                                System.out.println("Elemento movido correctamente.");
                            }else{
                                System.out.println("No se ha podido mover el elemento."); 
                            }
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "du": //Size of file or directory
                    try{
                        if(!(params.length >= 2)){
                            System.out.println("Faltan parámetros en comando du");
                            break;
                        }else{
                            int sizeElement = request.getService().du(params[1],root);
                            if(sizeElement == -1){
                                System.out.println("Elemento no existe.");
                            }else{
                                System.out.println("El tamaño de "+ params[1] + " es : " + sizeElement + " KB."); 
                            }
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "tree": //Tree of current directory
                    try{
                        String tree = request.getService().tree(root);
                        System.out.println(tree);
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "mkdir":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parámetros en comando");
                            break;
                        }else{
                            int created = request.getService().mkdir(params[1],root,false);
                            if(created>0){
                                System.out.println("Directorio creado exitosamente.");
                            }else{
                                System.out.println("Existe un directorio con el mismo nombre.¿Desea sobreescribirlo? Si/No");
                                message = scanIn.nextLine();
                                message = message.toLowerCase();
                                if(message.equals("si")){
                                    request.getService().mkdir(params[1],root,true);
                                    System.out.println("Directorio sobreescribido exitosamente.");
                                }else{
                                    System.out.println("No se puede crear el directorio con este nombre."); 
                                }
                            }
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "file":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parámetros en comando");
                            break;
                        }else{
                            String filenamePath = params[1];
                            String[] content = Arrays.copyOfRange(params, 2, params.length);
                            String joinedContent = String.join(" ", content);
                            int created = request.getService().createFile(filenamePath, 
                                    joinedContent, request.getService().getActualPath(root), root,false);
                            if(created==1){
                                System.out.println("Archivo creado exitosamente.");
                                break;
                            }else{
                                if(created==0){
                                    System.out.println("Existe un archivo con el mismo nombre.¿Desea sobreescribirlo? Si/No");
                                    message = scanIn.nextLine();
                                    message = message.toLowerCase();
                                    if(message.equals("si")){
                                        request.getService().createFile(filenamePath, 
                                                joinedContent, request.getService().getActualPath(root), root,true);
                                        System.out.println("Archivo sobreescribido exitosamente.");
                                        break;
                                    }else{
                                        System.out.println("No se puede crear el archivo con este nombre."); 
                                        break;
                                    }    
                                }
                                if(created == -1){
                                    System.out.println("No existe espacio disponible para crear el archivo");
                                    break;
                                }else{
                                    System.out.println("No se puede crear el archivo con ese directorio."); 
                                    break;
                                }
                            }
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "cat":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parámetros en el comando.");
                            break;
                        }else{
                            String[] filenames = Arrays.copyOfRange(params, 1, params.length);
                            String content = request.getService().cat(filenames, root);
                            if(content.equals(""))
                                System.out.println("No se encuentra el archivo con el nombre: " + params[1]);
                            else 
                                System.out.println(content);
                            
                        }
                    }catch(RemoteException | NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "rm":
                    try{
                        if(!(params.length > 1)){
                            System.out.println("Faltan parámetros en el comando.");
                            break;
                        }else{
                            boolean dir = false;
                            String[] filenames = Arrays.copyOfRange(params, 1, params.length);
                            if(filenames[0].equals("-r"))
                                dir = true;
                            boolean removed = request.getService().rm(filenames, dir ,root);
                            if(!removed)
                                System.out.println("Hubo un problema al eliminar uno o más archivos o directorios.");
                            
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "find":
                    try{
                        if(!(params.length == 2)){
                            System.out.println("Faltan parámetros en el comando o se insertaron más de 2.");
                            break;
                        }else{
                            String content = request.getService().find(params[1], root);
                            if(content.equals(""))
                                System.out.println("No se encuentra el archivo o directorio con el nombre: " + params[1]);
                            else 
                                System.out.println(content);
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "cpy":
                    try{
                        if(!(params.length == 3 || params.length == 4)){
                            System.out.println("Faltan parámetros en el comando o se insertaron más de 3 o 4.");
                            break;
                        }else{
                            int type = 0;
                            String[] paths = Arrays.copyOfRange(params, 2, params.length);;
                            if(params.length == 3){
                                type = 1;
                                paths = Arrays.copyOfRange(params, 1, params.length);
                            }
                            else if(params[1].equals("rv"))
                                type = 2;
                            else if(params[1].equals("vr"))
                                type = 3;
                            
                            if(type ==0)
                                System.out.println("No se reconoce el valor insertado en el comando CPY.");
                            else{                                
                                boolean copied = request.getService().cpy(paths, type, root);
                                if(!copied)
                                    System.out.println("No se pueden copiar las rutas indicadas.");
                                else 
                                    System.out.println("Archivos/Directorios copiados correctamente.");
                            }
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Error: " + e.getLocalizedMessage());
                    }
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Comando Inválido");
            }
        }
    }
    
    public static void main(String[] args) throws IOException, RemoteException, NotBoundException {
        ClientRMI client = new ClientRMI();
        client.showMenu();
    } 
}
