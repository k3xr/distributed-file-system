
package dfs;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DFSFicheroServ extends Remote  {
	
	byte[] read(int NumBytes, int posicion) throws RemoteException;
	
	void write(byte[] b, int posicion) throws RemoteException;
	
	void close() throws RemoteException;
}
