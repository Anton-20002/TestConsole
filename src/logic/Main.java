package logic;

import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
  private static Map<String, String> data = new HashMap<>();

    public static void main(String[] args) throws SQLException, IOException {
        Util db = new Util();
        db.startDB();
        db.startData();

      init();

    }

  private static void init() throws IOException, SQLException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Выберите действие: Ввод Данных(i) / Чтение Данных(r) / SQL запрос(s)" +"\n");

    String line = reader.readLine();

    switch (line) {
      case ("i"):
        insertData();
        break;
      case ("r"):
        readData();
        break;
      case ("s"):
        queryExe();
        break;
      default:

        init();
    }

  }

  private static void readData() throws SQLException, IOException {
    System.out.println("Выберите объект для чтения: Users(u), Project(p), Issue(i), All(a). Back(b)");
    String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
    Util util = new Util();

    switch (s){
      case ("u"):
        System.out.println("\nUsers\nId - Name -   Position   - Project_title - Issue_type");
        try{util.readRow("select * from Users");}
        catch (JdbcSQLSyntaxErrorException e){
          System.out.println(e.getMessage());}
        break;
      case ("p"):
        System.out.println("\nProject\nId - Title -     Description - User_name - Issue_type");
        try{util.readRow("select * from PROJECT");}
                catch (JdbcSQLSyntaxErrorException e){
        System.out.println(e.getMessage());}
        break;
      case ("i"):
        System.out.println("\nIssue\nId - Type -        Status - Project_title - User_name");
        try{util.readRow("select * from Issue");}
        catch (JdbcSQLSyntaxErrorException e){
          System.out.println(e.getMessage());}
        break;
      case ("a"):
        System.out.println("\nProject\nId - Title -     Description - User_name - Issue_type");
        try{util.readRow("select * from PROJECT");}
                catch (JdbcSQLSyntaxErrorException e){
        System.out.println(e.getMessage());}
        System.out.println("\nUsers\nId - Name -   Position   - Project_title - Issue_type");
        try {
          util.readRow("select * from Users");
        }catch (JdbcSQLSyntaxErrorException e){
          System.out.println(e.getMessage());
        }
        System.out.println("\nIssue\nId - Type -         Status - Project_title - User_name");
        try {
          util.readRow("select * from Issue");
        }catch (JdbcSQLSyntaxErrorException e){
          System.out.println(e.getMessage());
        }
        break;
      case ("b"):
        init();
      default:
        init();
    }
    readData();
  }

  private static void queryExe() throws IOException, SQLException {
    System.out.println("SQL query for all rows and fields Objects: Back(b)");
    String s = new BufferedReader(new InputStreamReader(System.in)).readLine();

    if (s.equals("b")){
      init();
    }
    Util util = new Util();
    try{
      if (s.toLowerCase().trim().startsWith("select")){
          util.readRow(s);
      }else {
        util.insert(s);}}
    catch (SQLException e){
      System.out.println(e.getMessage());
    }finally {

      queryExe();
    }
  }

  private static void insertData() throws IOException, SQLException {

    System.out.println("Укажите название проекта: ");
    String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
    check(line, "title", "description");

    System.out.println("Укажите пользователя: ");
    line = new BufferedReader(new InputStreamReader(System.in)).readLine();
    check(line, "name", "position");

    System.out.println("Укажите issue: ");
    line = new BufferedReader(new InputStreamReader(System.in)).readLine();
    check(line, "type", "status");

    System.out.println("Повторить ввод данных(y/n): Back(b)");
    line = new BufferedReader(new InputStreamReader(System.in)).readLine();

    if (line.equals("y")){
      insertData();}
    else if (line.equals("b")){
        init();
    }else {
      putData();
    }
    System.out.println("Данные внесены в базу");
    init();
  }

  private static void putData() throws SQLException {
    Util util = new Util();
    String insertProject = "INSERT INTO Project (title, description, user_name, issue_type) values(?,?,?,?)";
    util.insert(insertProject, data.get("title"), data.get("description"),data.get("name"),data.get("type"));

    String insertUser = "INSERT INTO Users (name, position, project_title, issue_type) values(?,?,?,?)";
    util.insert(insertUser, data.get("name"), data.get("position"),data.get("title"),data.get("type"));

    String insertIssue = "INSERT INTO Issue (type, status, project_title, user_name) values(?,?,?,?)";
    util.insert(insertIssue, data.get("type"), data.get("status"), data.get("title"),data.get("name"));

  }

  private static void check(String line, String str1, String str2) throws SQLException, IOException {
    Util util = new Util();
    if (util.checkData(line)){
      System.out.println("Найден объект в базе. Взяты его параметры");
      data.put(str1, line);
      data.put(str2, util.getPar());
      System.out.println("Par: "+data.get(str2));
    }else {
      System.out.println("Создан новый объект. Введите его данные ("+str2+"): ");
      data.put(str1, line);
      line = new BufferedReader(new InputStreamReader(System.in)).readLine();
      data.put(str2, line);
    }
  }

}
