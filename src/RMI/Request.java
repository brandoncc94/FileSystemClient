
package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import maininterface.IFunctions;

public class Request {
    Registry registry;
    IFunctions service;

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
