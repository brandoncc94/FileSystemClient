
package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import maininterface.Connection;
import maininterface.IFunctions;

public class Request {
    Registry registry;
    IFunctions service;
    
    public Request() throws RemoteException, NotBoundException{
        setRegistry(Connection.RMI_IP, Connection.RMI_PORT);
        setService(Connection.RMI_ID);
    }

    public Registry getRegistry() {
        return registry;
    }

    public IFunctions getService() {
        return service;
    }

    public void setRegistry(String pIp, int pPort) throws RemoteException {
        this.registry = LocateRegistry.getRegistry(pIp, pPort);
    }

    public void setService(String pService) throws RemoteException, NotBoundException {
        this.service = (IFunctions) registry.lookup(pService);
    }
}
