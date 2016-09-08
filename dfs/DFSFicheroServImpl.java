package dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DFSFicheroServImpl extends UnicastRemoteObject implements DFSFicheroServ {

	private static final long serialVersionUID = 1L;

	private static final String DFSDir = "DFSDir/";
	private RandomAccessFile fichero;
	// r = read rw = read/write 

	public DFSFicheroServImpl(String nombreFichero, String modoApertura) throws RemoteException, FileNotFoundException {
		fichero = new RandomAccessFile(DFSDir+nombreFichero, modoApertura);
	}

	@Override
	public byte[] read(int numBytes, int posicion) throws RemoteException {

		byte[] arrayConLeido =new byte[numBytes];
		try {
			fichero.seek(posicion);
			if(fichero.read(arrayConLeido) == -1){
				// EOF
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayConLeido;
	}

	@Override
	public void write(byte[] b, int posicion) throws RemoteException {
		
		try {
			fichero.seek(posicion);
			fichero.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws RemoteException {
		try {
			fichero.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  

	/**
	 * 	 Main para pruebas en Eclipse
	 *  
	 */
	
	static public void main (String args[])  {

		try {
			System.out.println("abriendo fichero");
			DFSFicheroServImpl ficheroPrueba = new DFSFicheroServImpl("test", "rw");

			System.out.println("escribiendo 6 bytes en pos 0");
			byte[] byteArray = new byte[] {87, 79, 87, 46, 46, 46};
			ficheroPrueba.write(byteArray, 0);

			System.out.println("leyendo 10 bytes desde pos 0");
			
			byte[] byteArrayLeido = new byte[10];
			byteArrayLeido = ficheroPrueba.read(10, 0);
			
			for(int i=0;i<byteArrayLeido.length;i++){
				System.out.println(byteArrayLeido[i]);
			}
			
			System.out.println("-------------------------------");

			String aImprimir;
			try {
				aImprimir = new String(byteArrayLeido, "UTF-8");
				System.out.println(aImprimir);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}


		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.exit(0);

	}
}
