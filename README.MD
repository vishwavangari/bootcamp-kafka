# Prerequisites

`Step 1`: 
Check your java version. If your java is less than 1.8, then update it
To check java version, execute below command in terminal: 
java -version
Install openjdk using this link https://openjdk.java.net/install/

`Step 2:`
For Mac Users: Download Docker for Mac.
https://download.docker.com/mac/stable/Docker.dmg
and follow instructions to setup docker.

For Windows Users: Follow this to setup docker for windows:
https://docs.docker.com/docker-for-windows/install/

`Step 3:`
Download Kafka Binary and place it in any path (we will refer this while hands-on):
https://www.apache.org/dyn/closer.cgi?path=/kafka/2.3.1/kafka_2.12-2.3.1.tgz

`Step 4:`
If you don't have any Java IDE. Setup Java IDE preferably IntellijIdea
Download Community Edition, its free 
or 
else download Ultimate Enterprise Edition free for a month
https://www.jetbrains.com/idea/download/#section=mac

`Step 5:` 
Make sure have docker up and running. Use below commands in terminal to check out:
docker ps -a
If it throws out below error, start your docker
Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?

`Step 6:`
Execute setup.sh file at this repo root dir bootcamp/setup.sh to download docker images
chmod +x setup.sh
./setup.sh




