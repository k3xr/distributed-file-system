import java.util.*;
import dfs.*;
import java.rmi.*;

class Test {
    static byte val = 'A'; // a escribir en fichero; incrementado en cada op.

    static private void doRead(DFSFicheroCliente f, int tam) {
        try {
            byte [] buf = new byte[tam];
            int leido = f.read(buf);
            System.out.println("Leidos " + leido + " bytes: ");
            if (leido>0) {
                System.out.write(buf, 0, leido);
                System.out.println();
            }
        }
        catch (java.io.IOException e) {
            System.err.println("Error en la operacion read");
        }
    }

    static private void doWrite(DFSFicheroCliente f, int tam) {
        try {
            byte [] buf = new byte[tam];
            for (int i=0; i<tam; i++) buf[i] = val;
            f.write(buf);
            System.out.print("Escritos " +tam+ " bytes con valor: ");
            System.out.write(val++);
            System.out.println();
        }
        catch (java.io.IOException e) {
            System.err.println("Error en la operacion write");
        }
    }
    static private void doSeek(DFSFicheroCliente f, int pos) {
        try {
            f.seek(pos);
            System.out.println("Puntero colocado en posicion " + pos);
        }
        catch (java.io.IOException e) {
            System.err.println("Error en la operacion seek");
        }
    }
    static private boolean prompt(String f) {
	
        System.out.println("Seleccione operacion sobre fichero " + f);
        System.out.println("Introduzca en una unica linea 2 enteros A y B:");
        System.out.println("\tA indica la operacion (0 read|1 write|2 seek)");
        System.out.println("\tB indica tamaño para read|write y posicion para seek");
        System.out.println("\tNOTA: linea vacia indica pasar al siguiente fichero");

        return true;
    }

    static public void main(String args[]) {
        if (args.length != 2) {
                    System.err.println("Uso: Test tamBloque nBloquesCacheFichero");
                    return;
        }
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            int tamBloque = Integer.parseInt(args[0]);
            int tamCache = Integer.parseInt(args[1]);
            DFSCliente dfs = new DFSCliente(tamBloque, tamCache);

            while (true) {
                Scanner ent = new Scanner(System.in);
                System.out.println("Introduzca nombre de fichero y modo: r (defecto) o rw; (Crtl-D para terminar)");
                if (!ent.hasNextLine()) return;
                String lin = ent.nextLine(); 
                Scanner s = new Scanner(lin);
                if (!s.hasNext()) return;
                String fich = s.next(); 
                String modo ="r"; 
                if ((s.hasNext()) && s.next().equals("rw"))
                    modo ="rw"; 
                try {
    	            DFSFicheroCliente f = new DFSFicheroCliente(dfs, fich, modo);
                    while (prompt(fich) && ent.hasNextLine()) {
                        boolean formatoOK = false;
                        lin = ent.nextLine();
                        if (lin.length() == 0) break;
                        s = new Scanner(lin);
                        if (s.hasNextInt()) {
                            int op = s.nextInt();
                            if (s.hasNextInt()) {
                                int val = s.nextInt();
                                if (val % tamBloque != 0)
                                    System.err.println("Error: tamaño/posición deben ser múltiplos del tamaño del bloque");
                                else
                                    switch (op) {
                                        case 0: doRead(f, val);
                                                formatoOK = true; break;
                                        case 1: doWrite(f, val);
                                                formatoOK = true; break;
                                        case 2: doSeek(f, val);
                                                formatoOK = true; break;
                                    }
                            }
                        }
                        if (!formatoOK)
                            System.err.println("Error en formato de operacion");
                    }
    	            f.close();
                }
                catch (java.io.FileNotFoundException e) {
                    System.out.println("Fichero no existente");
                }
                catch (java.io.IOException e) {
e.printStackTrace();
                    System.out.println("Error en operacion E/S sobre el fichero");
                }
            }
        }
        catch (Exception e) {
            System.err.println("Excepcion en Test:");
            e.printStackTrace();
        }
        finally {
            System.exit(0);
        }

    }
}
