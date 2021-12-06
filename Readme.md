# CMPE -202 -Individual Project

## Inventory Management

Name : Sakshi Mishra
SJSU ID : 015721810

## Installation Guide
Clone git repository
Download Intellij 
import project in intellij
Move to pom.xml and import all dependencies
Download Java and set up path of JDK in Environment
Download Maven and Set up path of maven

## Steps to execute
1. open CMD in project root folder
2. Type Following commands:
      mvn compile
      - mvn clean
      - mvn install
      - mvn -X clean install exec:java -Dexec.mainClass="com.inventory.Runner" -Dexec.args="{paste input file in project directory and give its path eg(resources.input.csv if input file is in resources folder)}"
        
3. check output in resources folder
