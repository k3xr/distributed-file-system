#!/bin/sh

set -x


cd ..


javac dfs/*.java


jar cf dfs/dfs_cliente.jar dfs/DFSCliente*.class dfs/DFSFicheroCliente*.class dfs/DFSServicio.class dfs/DFSFicheroServ.class dfs/Bloque.class dfs/Cache.class dfs/Cache'$1'.class dfs/FicheroInfo.class dfs/DFSFicheroCallback.class dfs/DFSFicheroCallbackImpl.class

jar cf dfs/dfs_servidor.jar dfs/DFSServicio.class dfs/DFSFicheroServ.class dfs/DFSServicioImpl*.class dfs/DFSFicheroServImpl*.class dfs/Bloque.class dfs/FicheroInfo.class dfs/DFSFicheroCallback.class


