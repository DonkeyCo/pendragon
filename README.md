# pendragon - Client

## Introduction

**pendragon** is a Dungeons & Dragons management tool, allowing users to manage different campaign types,
campaigns and characters.

**pendragon** also provides an option to gather with friends in a chat room to play a campaign. During this
session, players can change their characters accordingly and chat together.

This README will mainly focus on setting **pendragon** up.

## Prerequisites

To build and run **pendragon** you have to fulfill the following prerequisites:

* [pendragon - WebSocket Server](https://github.com/DonkeyCo/pendragon-websocket) (see README to start WebSocket Server)
* [Java](https://jdk.java.net/archive/) (required version: 14.0.2)
* [Maven](https://maven.apache.org/download.cgi) (minimal required version: 3.6.3)

Install all the requirements and set them up accordingly.

## Setup
Follow the following steps to setup and run the application:

0 - Clone the client and server project from GitHub.

1 - Make sure the WebSocket Server is up and running.

2 - Move to Project Root Folder and execute Maven 'package' step
```
mvn package
```  

3 - Set up environment variable *websocket* to WebSocket Host, e.g.
```
export websocket="ws://localhost:3000"
```

4 - Go to /target folder and execute the JAR file which contains the dependencies (-jar-with-dependencies.jar)
```
java -jar p2p-client-1.0-SNAPSHOT-jar-with-dependencies.jar
``` 

5 - Application should start up now and is ready to use.

## How To Use

If you want to create a campaign, make sure to create a campaign variant first as they are the base for
creating campaigns. After creating a variant, you can proceed to create a campaign.

If you want to start up a lobby for said campaign, click on the 'Play'-Button in the campaign tile.

If you wantt to join a campaign, click on 'Join' and enter the lobby room code.

Please make sure that the server is up and running, otherwise errors may occur.

If you want to test lobby functionalities locally, please make sure, that you have 2 different 
instances of the application. At best, you build 2 instances separately. Export the *websocket* environment variable
for both instances.

When connecting to a different client, the application may become unresponsive for a short amount of time.

## Contact

If you have any problems or any questions, please do not hesitate to contact me:

[duc.vongoc01@gmail.com](mailto:duc.vongoc01@gmail.com)