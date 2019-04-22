package logic;

import java.sql.*;

class Util {
  private String par;
  private Connection connection = null;
  private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
  private static final String DB_USER = "";
  private static final String DB_PASSWORD = "";

  private static final String[] query = {
      "select * from PROJECT where title = ?",
      "select * from Users where name = ?",
      "select * from Issue where type = ?"
  };

  Util() throws SQLException {
    connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
  }


  void insert(String query) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);

  }

  void insert(String query, String str1, String str2, String aStr, String bStr) throws SQLException {
    PreparedStatement statement = null;
    statement = connection.prepareStatement(query);
    statement.setString(1, str1);
    statement.setString(2, str2);
    statement.setString(3, aStr);
    statement.setString(4, bStr);
    statement.executeUpdate();
  }

  void readRow(String str) throws SQLException {
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(str);
    while (resultSet.next()) {
      StringBuilder stringBuilder
          = new StringBuilder();
      stringBuilder.append(resultSet.getInt(1)).append(" -- ")
          .append(resultSet.getString(2)).append(" -- ")
          .append(resultSet.getString(3)).append(" -- ")
          .append(resultSet.getString(4)).append(" -- ")
          .append(resultSet.getString(5));
      System.out.println(stringBuilder);
    }
  }

  boolean checkData(String str) throws SQLException {
    PreparedStatement statement = null;
    int i = 0;
    do {
      statement = connection.prepareStatement(query[i]);
      statement.setString(1, str);

      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        par=resultSet.getString(3);
        statement.close();
        return true;
      } else {
        i++;
      }

    } while (i < query.length);
      statement.close();
      return false;
  }

  String getPar() {
    return par;
  }

  void startDB() throws SQLException {
    PreparedStatement statement = null;

    String strUser = "CREATE TABLE Users(id int generated ALWAYS AS IDENTITY, name varchar(255), position varchar(255), project_title varchar(255), issue_type varchar(255), primary key (id))";

    String strTable = "CREATE TABLE Project(id int generated ALWAYS AS IDENTITY,  title varchar(255), description varchar(255), user_name varchar(255), issue_type varchar(255), primary key (id))";

    String strIssue = "CREATE TABLE Issue(id int generated ALWAYS AS IDENTITY, type varchar(255), status varchar(255), project_title varchar(255), user_name varchar(255), primary key (id))";

    statement = connection.prepareStatement(strIssue);
    statement.executeUpdate();
    statement = connection.prepareStatement(strUser);
    statement.executeUpdate();
    statement = connection.prepareStatement(strTable);
    statement.executeUpdate();
    statement.close();
  }

  void startData() throws SQLException {
    String query = "INSERT INTO Project (title, description, user_name, issue_type) values('Project1','Description','Anton', 'Operation1')";
    insert(query);
    query = "INSERT INTO Project (title, description, user_name, issue_type) values('Project1','Description','Artem', 'Operation2')";
    insert(query);
    query = "INSERT INTO Project (title, description, user_name, issue_type) values('Project1','Description','Artem', 'Operation3')";
    insert(query);
    query = "INSERT INTO Users (name, position, project_title, issue_type) values('Anton', 'developer', 'Project1', 'Operation1')";
    insert(query);
    query = "INSERT INTO Users (name, position, project_title, issue_type) values('Artem', 'developer', 'Project1', 'Operation2')";
    insert(query);
    query = "INSERT INTO Users (name, position, project_title, issue_type) values('Artem', 'developer', 'Project1', 'Operation3')";
    insert(query);
    query = "INSERT INTO Issue (type, status, project_title, user_name) values('Operation1', 'inAction', 'Project1', 'Anton')";
    insert(query);
    query = "INSERT INTO Issue (type, status, project_title, user_name) values('Operation2', 'inAction', 'Project1', 'Artem')";
    insert(query);
    query = "INSERT INTO Issue (type, status, project_title, user_name) values('Operation3', 'inAction', 'Project1', 'Artem')";
    insert(query);
  }
}