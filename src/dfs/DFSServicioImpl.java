
package dfs;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class DFSServicioImpl extends UnicastRemoteObject implements DFSServicio {

	private static final long serialVersionUID = 1L;
	private static final String DFSDir = "DFSDir/";
	//Lista de ficheros abierta, cada elemento contiene los Cliente
	private java.util.List<DFSFicheroServImpl> lFicherosServidor = new ArrayList<DFSFicheroServImpl>();

	public DFSServicioImpl() throws RemoteException {
	}

	@Override
	public synchronized FicheroInfo AbrirFichero(String nombre, String permisos, DFSFicheroCallback paraCallBack) throws RemoteException, IOException{
				
		File fichero = new File(DFSDir+nombre);	
		
		if(permisos.equals("rw")){
			if(fichero.exists()){
				/* Busca en lista */
				for (int i = 0; i < lFicherosServidor.size(); i++) {
					if(lFicherosServidor.get(i).getNombreFichero().equals(nombre)){
						/* invalida clientes y a el mismo */
						lFicherosServidor.get(i).invalidarClientes();
						paraCallBack.invalidaCache(nombre);
						/* Se anade a la lista */
						lFicherosServidor.get(i).anadirALista(paraCallBack);
						/* Devuelve el fichero para lectura/escritura */
						return new FicheroInfo(lFicherosServidor.get(i),fichero.lastModified());
					}
				}
			}
			else{
				fichero.createNewFile();
				DFSFicheroServImpl ficheroServImpl = new DFSFicheroServImpl(nombre, permisos);
				ficheroServImpl.anadirALista(paraCallBack);
				lFicherosServidor.add(ficheroServImpl);
				return new FicheroInfo(ficheroServImpl, fichero.lastModified());
			}
		}
		else{
			if(fichero.exists()){
				
			}
			else{
				throw new IOException();
			}
		}
			
			
		
		if(fichero.exists()){
		
			for (int i = 0; i < lFicherosServidor.size(); i++) {
				if(lFicherosServidor.get(i).getNombreFichero().equals(nombre)){
					lFicherosServidor.get(i).anadirALista(paraCallBack);
					return new FicheroInfo(lFicherosServidor.get(i),fichero.lastModified());
				}
			}
		}

		if (!fichero.exists() && permisos.equals("rw")){
			try {
				fichero.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (!fichero.exists() && permisos.equals("r")){
			throw new IOException();
		}

		//Anadimos el nuevo manejador a la lista, este esta referenciado al fichero
		DFSFicheroServImpl ficheroServImpl = new DFSFicheroServImpl(nombre, permisos);
		ficheroServImpl.anadirALista(paraCallBack);
		lFicherosServidor.add(ficheroServImpl);
		return new FicheroInfo(ficheroServImpl, fichero.lastModified());

	}
}