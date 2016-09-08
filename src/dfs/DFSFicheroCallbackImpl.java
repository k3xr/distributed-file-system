
package dfs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class DFSFicheroCallbackImpl extends UnicastRemoteObject implements DFSFicheroCallback {
	
	private static final long serialVersionUID = 1L;
	private DFSCliente cliente;

	public DFSFicheroCallbackImpl(DFSCliente cliente) throws RemoteException {
		this.cliente=cliente;
		
    }

	@Override
	public void invalidaCache(String nombreFichero) {
		cliente.invalidaCacheDeFichero(nombreFichero);
		
	}
}
