import java.rmi.*;
import java.rmi.server.*;
import dfs.*;

class ServidorDFS  {

    static public void main (String args[])  {
        if (args.length!=1) {
            System.err.println("Uso: ServidorDFS numPuertoRegistro");
            return;
        }
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            DFSServicio srv = new DFSServicioImpl();
            Naming.rebind("rmi://localhost:" + args[0] + "/DFS", srv);
        }
        catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1);
        }
        catch (Exception e) {
            System.err.println("Excepcion en ServidorDFS:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
