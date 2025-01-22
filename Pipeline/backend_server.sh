#!/usr/bin/env bash

export IMAGE=$1
docker-compose -f backend.yaml up --detach
echo "success"
