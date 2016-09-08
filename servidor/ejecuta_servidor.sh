#!/bin/sh

set -x

java -Djava.security.policy=servidor.permisos -cp .:dfs_servidor.jar ServidorDFS $*
