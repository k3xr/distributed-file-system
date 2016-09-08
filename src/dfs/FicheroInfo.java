// Esta clase representa informacion de un fichero.
// El enunciado explica mas en detalle el posible uso de esta clase.
// Al ser serializable, puede usarse en las transferencias entre cliente
// y servidor.

package dfs;

import java.io.Serializable;

public class FicheroInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private DFSFicheroServ ficheroRemoto;
	private long fecha;

	public FicheroInfo(DFSFicheroServ ficheroRemoto, long fecha) {
		this.ficheroRemoto = ficheroRemoto;
		this.fecha = fecha;
	}

	public DFSFicheroServ getFicheroRemoto(){
		return ficheroRemoto;
	}

	public long getFecha(){
		return fecha;
	}



}
