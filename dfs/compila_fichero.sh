#!/bin/sh

test $# -eq 1 || { echo "Uso: $0 fichero.java" 2>&1; exit 1; }

set -x
cd ..
javac dfs/$1
