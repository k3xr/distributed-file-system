#!/bin/sh

test $# -ge 1 || { echo "Uso: $0 fichero.java..." 2>&1; exit 1; }

set -x
javac -cp .:dfs_cliente.jar $*
