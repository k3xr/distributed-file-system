
package dfs;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class DFSCliente {

	private DFSServicio srv;
	private DFSFicheroCallback cliente;
	private HashMap<String, Cache> coleccionCaches;
	private int tamCache;
	private int tamBloque;

	public DFSCliente(int tamBloque, int tamCache) {

		this.tamBloque = tamBloque;
		this.tamCache = tamCache;
		coleccionCaches = new HashMap<String,Cache>();

		String servidor = System.getenv("SERVIDOR");
		String puerto = System.getenv("PUERTO");

		//Conexion con el servidor
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}

		try {
			srv = (DFSServicio) Naming.lookup("//"+servidor+ ":" + puerto +"/DFS"); 
		} catch (MalformedURLException e) { 
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		
		//Para poder realizar el callback desde servidor
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());

		try {
			cliente = new DFSFicheroCallbackImpl(this);
			Naming.rebind("rmi://localhost:" + puerto + "/callback", cliente);
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

	public DFSServicio getDFSServicio(){
		return srv;
	}
	
	public DFSFicheroCallback getDFSFicheroCallbackImpl(){
		return cliente;
	}

	public int getTamBloque(){
		return tamBloque;
	}
	
	public void invalidaCacheDeFichero(String nombreFichero){
		coleccionCaches.remove(nombreFichero);
	}

	public Cache devuelveCache(String nombreFichero){

		if(!coleccionCaches.containsKey(nombreFichero)){
			coleccionCaches.put(nombreFichero, new Cache(tamCache));
		}
		return coleccionCaches.get(nombreFichero);
	}
}

