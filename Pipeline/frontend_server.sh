#!/usr/bin/env bash

export IMAGE=$1
docker-compose -f frontend.yaml up --detach
echo "success"
