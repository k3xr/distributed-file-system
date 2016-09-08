
package dfs;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class DFSCliente {
	
	private DFSServicio srv;
	private HashMap<String, Cache> coleccionCaches;
	private int tamCache;
	private int tamBloque;
	
	public DFSCliente(int tamBloque, int tamCache) {
		
		this.tamBloque = tamBloque;
		this.tamCache = tamCache;
		coleccionCaches = new HashMap<String,Cache>();

		String servidor = System.getenv("SERVIDOR");
		String puerto = System.getenv("PUERTO");

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
	}
		
	public DFSServicio getDFSServicio(){
		return srv;
	}
	
	public int getTamBloque(){
		return tamBloque;
	}
	
	public Cache devuelveCache(String nombreFichero){
		
		if(!coleccionCaches.containsKey(nombreFichero)){
			coleccionCaches.put(nombreFichero, new Cache(tamCache));
		}
		return coleccionCaches.get(nombreFichero);
	}
}

