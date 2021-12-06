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

## Design Patterns
1. chain of responsibility to handle check for luxury , miscelleneous and essential
2. Singleton to make single instance of values of upper cap and keep it constant for all item and over xomplete chain



## Steps to execute
1. open CMD in project root folder
2. Type Following commands:
      mvn compile
      - mvn clean
      - mvn install
      - mvn -X clean install exec:java -Dexec.mainClass="com.inventory.Runner" -Dexec.args="{paste input file in project directory and give its path eg("input.csv" if input file is in resources folder)}"
        
3. check output in resources folder

