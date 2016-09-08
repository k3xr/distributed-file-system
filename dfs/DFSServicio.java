
package dfs;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DFSServicio extends Remote {
	
	FicheroInfo AbrirFichero(String nombre,String permisos) throws RemoteException, IOException;

}       
