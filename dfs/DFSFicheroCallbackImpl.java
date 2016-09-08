
package dfs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class DFSFicheroCallbackImpl extends UnicastRemoteObject implements DFSFicheroCallback {
	
	private static final long serialVersionUID = 1L;

	public DFSFicheroCallbackImpl()
      throws RemoteException {

    }
}
