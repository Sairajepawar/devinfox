Java Swing and SQL Application<a name="TOP"></a>
===================
This repository contains a Java Swing application that demonstrates a simple user interface for asking queries, providing answers, and viewing responses. The program utilizes Java Swing for the user interface and connects to an Oracle SQL database for data storage. Before running the program, you need to initialize three database tables: user_info, answer_table, and query_table. Below are the instructions for setting up the database and running the program.
- - - - 
# Prerequisites #
<ul>
  <li>Java Development Kit (JDK)</li>
  <li>Oracle SQL Database</li>
</ul>

- - - -  
# Setup Instructions #

## Database Setup: ##

Before running the application, you need to set up the required database tables. Here are the steps:

Open your Oracle SQL database.

Execute the following SQL commands to create the necessary tables:
<ul>
<li> Create the user_info table

  `CREATE TABLE user_info (
  USER_ID VARCHAR2(50) PRIMARY KEY,
  NAME VARCHAR2(50),
  PASSWORD VARCHAR2(50),
  EMAIL_ID VARCHAR2(50),
  CONTACT_NUMBER VARCHAR2(50)
);`</li>

<li>
  Create the query_table

  `CREATE TABLE query_table (
  QUERY_ID VARCHAR2(50) PRIMARY KEY,
  QUERY VARCHAR2(2000),
  USER_ID VARCHAR2(50) REFERENCES user_info(USER_ID)
);`
 </li>

<li> Create the answer_table

  `CREATE TABLE answer_table (
  QUERY_ID VARCHAR2(50) REFERENCES query_table(QUERY_ID),
  USER_ID VARCHAR2(50) REFERENCES user_info(USER_ID),
  ANSWER VARCHAR2(2000)
);
`
  </li>


</ul>

- - - - 
**#Java Swing Application:#**

Clone this GitHub repository to your local machine or download the source code.
Open the Java Swing project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse).
Ensure that you have the Oracle JDBC driver added to your project's classpath.
Navigate to the start.java in the src folder
Running the Application:

Compile and run the start.java
- - - - 
# Usage: #

The application will provide you with options to ask queries, view answers, and interact with the community.
Guests can ask queries but cannot provide answers.
Members can ask queries, provide answers, and delete their own queries.
Contributing
If you'd like to contribute to this project, please fork the repository and create a pull request with your changes.
- - - - 
# License #
This project is licensed under the MIT License - see the LICENSE file for details.

Enjoy using the Java Swing and SQL application! If you encounter any issues or have suggestions for improvements, feel free to open an issue on this GitHub repository.
