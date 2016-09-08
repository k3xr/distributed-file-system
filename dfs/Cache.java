// Esta clase para el cliente gestiona una cache LRU de bloques.
// No puede modificarse.
// El enunciado describe su funcionalidad.

package dfs;
import java.util.*;

public class Cache {
	private LinkedHashMap<Long, Bloque> cache;
	private HashMap<Long, Bloque> mod;
	private int tam;
	private long fecha = 0;
	private Bloque expulsado;

	public Cache(int t) {
		tam = t;
		cache = new LinkedHashMap<Long, Bloque>(t+1, (float)1.1, true) {
			protected boolean removeEldestEntry(Map.Entry<Long,Bloque> viejo)
			{
				if (size() > tam) {
					expulsado = viejo.getValue();
					return true;
				}
				else {
					expulsado = null;
					return false;
				}
			}
		};
		mod = new HashMap<Long, Bloque>();

	}
	public Bloque getBloque(long id) {
		return cache.get(id);
	}
	public Bloque putBloque(Bloque b) {
		cache.put(b.obtenerId(), b);
		return expulsado;
	}
	public void activarMod(Bloque b) {
		mod.put(b.obtenerId(), b);
	}
	public void desactivarMod(Bloque b) {
		mod.remove(b.obtenerId());
	}
	public boolean preguntarMod(Bloque b) {
		return mod.containsKey(b.obtenerId());
	}
	public boolean preguntarYDesactivarMod(Bloque b) {
		return (mod.remove(b.obtenerId()) != null);
	}
	public List<Bloque> listaMod() {
		return new LinkedList<Bloque>(mod.values());
	}
	public void vaciarListaMod() {
		mod.clear();
	}
	public void vaciar() {
		cache.clear();
		mod.clear();
	}
	public long obtenerFecha(){
		return fecha;
	}
	public void fijarFecha(long f){
		fecha = f;
	}
}
