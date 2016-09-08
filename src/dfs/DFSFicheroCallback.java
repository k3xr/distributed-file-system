
package dfs;

import java.rmi.Remote;

public interface DFSFicheroCallback extends Remote  {
	
	  public void invalidaCache(String nombreFichero);
	
}
