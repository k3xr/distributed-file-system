package dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

public class DFSFicheroCliente  {

	private DFSServicio dfsServicio;
	private DFSFicheroServ dfsFichero;
	private int posicion;
	private String modoApertura;
	private int tamBloque;
	private Cache cacheFichero;
	private long fecha;
	private boolean abierto = true;

	public DFSFicheroCliente(DFSCliente dfs, String nom, String modo) throws RemoteException, IOException, FileNotFoundException {
		dfsServicio =dfs.getDFSServicio();
		modoApertura=modo;
		tamBloque = dfs.getTamBloque();
		cacheFichero = dfs.devuelveCache(nom);
		FicheroInfo ficheroInfo = dfsServicio.AbrirFichero(nom, modo);
		dfsFichero = ficheroInfo.getFicheroRemoto();
		fecha = ficheroInfo.getFecha();
		
		if(cacheFichero.obtenerFecha()<fecha){
			cacheFichero.vaciar();
		}
	}

	public int read(byte[] b) throws RemoteException, IOException {
		//Si el fichero ha sido cerrado error
		if(!abierto){
			throw new IOException();
		}
		
		int numBloquesALeer = b.length/tamBloque;
		byte[] arrayRecibido = new byte[b.length];

		for(int i=0;i<numBloquesALeer;i++){
			int posicionBloque= posicion+tamBloque*i;
			Bloque blq = cacheFichero.getBloque(posicionBloque);
			byte[] arrayALeer = new byte[tamBloque];

			if(blq == null){
				// Peticion a servidor
				System.out.println("llamada al servidor(bloque "+i+" no esta en cache)");        

				arrayALeer=dfsFichero.read(tamBloque, posicionBloque);    

				if(arrayALeer!=null){
					System.arraycopy(arrayALeer, 0, arrayRecibido, i*tamBloque, tamBloque);
				}
				else{
					// EOF
					break;
				}

				Bloque nuevoBloque = new Bloque(posicionBloque, arrayRecibido);
				Bloque posibleBloqueExpulsado = cacheFichero.putBloque(nuevoBloque);

				if(posibleBloqueExpulsado!=null){
					// Se expulsa un bloque
					if(cacheFichero.preguntarMod(posibleBloqueExpulsado)){
						// Bloque expulsado modificado (escribir en servidor)
						dfsFichero.write(posibleBloqueExpulsado.obtenerContenido(), (int)posibleBloqueExpulsado.obtenerId());
						// Marca bloque traido del servidor como no modificado
						cacheFichero.desactivarMod(nuevoBloque);
					}
				}				
			}
			else{
				// Esta en cache
				System.out.println("bloque "+i+" si esta en cache");

				arrayALeer = blq.obtenerContenido();
				System.arraycopy(arrayALeer, 0, arrayRecibido, i*tamBloque, tamBloque);
			}
		}

		System.arraycopy(arrayRecibido, 0, b, 0, arrayRecibido.length);

		// Calculo del numero de bytes leidos (comprobando si byte es 0 por EOF???)
		int bytesLeidos=0;
		for(int i=0;i<arrayRecibido.length;i++){
			if(arrayRecibido[i]!=0){
				bytesLeidos++;
			}
		}
		posicion = posicion +bytesLeidos;
		return (bytesLeidos);

	}

	public void write(byte[] b) throws RemoteException, IOException {

		if(!abierto){
			throw new IOException();
		}
		System.out.println("llamada a write con pos: "+ posicion);    	

		if(modoApertura.equals("r")){
			throw new IOException();
		}

		int numBloquesAEscribir = b.length/tamBloque;

		for(int i=0;i<numBloquesAEscribir;i++){

			byte[] arrayAEscribir = new byte[tamBloque];	
			System.arraycopy(b, i*tamBloque, arrayAEscribir, 0, tamBloque);			
			Bloque nuevoBloque = new Bloque(posicion, arrayAEscribir);

			Bloque posibleBloqueExpulsado = cacheFichero.putBloque(nuevoBloque);
			if(posibleBloqueExpulsado!=null){
				//Se expulsa un bloque
				if(cacheFichero.preguntarMod(posibleBloqueExpulsado)){
					// Bloque expulsado modificado (escribir en servidor)
					dfsFichero.write(posibleBloqueExpulsado.obtenerContenido(), (int)posibleBloqueExpulsado.obtenerId());
				}
			} 
			cacheFichero.activarMod(nuevoBloque);
			posicion = posicion +tamBloque;
		}
	}

	public void seek(long p) throws RemoteException, IOException {
		posicion =(int)p;
	}

	public void close() throws RemoteException, IOException {
		System.out.println("llamada a close");    	

		List<Bloque> bloquesModificados = cacheFichero.listaMod();
		Iterator<Bloque> iterador = bloquesModificados.iterator();
		while(iterador.hasNext()){
			Bloque aEscribirEnServidor = iterador.next();

			dfsFichero.write(aEscribirEnServidor.obtenerContenido(), (int)aEscribirEnServidor.obtenerId());
			cacheFichero.desactivarMod(aEscribirEnServidor);
		}
		cacheFichero.fijarFecha(System.currentTimeMillis());
		dfsFichero.close();
		//Se ha cerrado el fichero si quiere volver a ser accedido debera abrirse de nuevo
		abierto = false;
	}
}
