
package dfs;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class DFSServicioImpl extends UnicastRemoteObject implements DFSServicio {

	private static final long serialVersionUID = 1L;
	private static final String DFSDir = "DFSDir/";

	public DFSServicioImpl() throws RemoteException {
	}

	@Override
	public FicheroInfo AbrirFichero(String nombre, String permisos) throws RemoteException, IOException{
		File fichero = new File(DFSDir+nombre);

		if (!fichero.exists() && permisos.equals("rw")){
			try {
				fichero.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (!fichero.exists() && permisos.equals("r")){
			throw new IOException();
		}


		DFSFicheroServImpl ficheroServImpl = new DFSFicheroServImpl(nombre, permisos);
		return new FicheroInfo(ficheroServImpl, fichero.lastModified());

	}
}