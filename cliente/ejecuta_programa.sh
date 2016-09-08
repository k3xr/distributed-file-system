#!/bin/sh

test $# -eq 0 && { echo "Uso: $0 programa args..." 2>&1; exit 1; }

PROG=$1
shift
set -x
java -Djava.security.policy=cliente.permisos -cp .:dfs_cliente.jar $PROG $*

