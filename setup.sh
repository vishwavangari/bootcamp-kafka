#!/usr/bin/env bash
########## Building this Project ###################
echo ""
echo "############# Building bootcamp Project ############"

./gradlew clean build

echo ""
echo "######## Downloading Docker Images ####################"

echo ""
echo "Downloading Zookeeper docker Image"
docker pull confluentinc/cp-zookeeper:5.3.1

echo ""
echo "Downloading Kafka docker Image"
docker pull confluentinc/cp-kafka:5.3.1

echo ""
echo "Downloading Postgres docker Image"
docker pull postgres

echo ""
echo "############# Setup Finished ####################"




