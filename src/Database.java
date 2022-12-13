import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
  private Connection connection;
  private Statement statement;

  public Database(String name) throws SQLException  {
    connection = DriverManager.getConnection("jdbc:sqlite:" + name);
    statement = connection.createStatement();
  }

  public boolean execute(String sql) throws SQLException {
    return statement.execute(sql);
  }

  public int executeUpdate(String sql) throws SQLException {
    return statement.executeUpdate(sql);
  }

  public ResultSet executeQuery(String sql) throws SQLException {
    return statement.executeQuery(sql);
  }

  public boolean execute(String sql, Object... params) throws SQLException {
    for (Object object : params) {
      if (object instanceof String) {
        sql = sql.replaceFirst("[?]", "'" + (String) object + "'");
      } else {
        sql = sql.replaceFirst("[?]", String.valueOf(object));
      } 
    }
    return statement.execute(sql);
  }

  public int executeUpdate(String sql, Object... params) throws SQLException {
    for (Object object : params) {
      if (object instanceof String) {
        sql = sql.replaceFirst("[?]", "'" + (String) object + "'");
      } else {
        sql = sql.replaceFirst("[?]", String.valueOf(object));
      } 
    }
    return statement.executeUpdate(sql);
  }

  public ResultSet executeQuery(String sql, Object... params) throws SQLException {
    for (Object object : params) {
      if (object instanceof String) {
        sql = sql.replaceFirst("[?]", "'" + (String) object + "'");
      } else {
        sql = sql.replaceFirst("[?]", String.valueOf(object));
      } 
    }
    return statement.executeQuery(sql);
  }

  public class Utils {
    public static String useSql(String fileName) throws IOException {
      return new String(Files.readAllBytes(Paths.get("sql", fileName + ".sql")));
    }

    public static String useSql(String fileName, String usage) throws IOException {
      String data = new String(Files.readAllBytes(Paths.get("sql", fileName + ".sql")));
      String[] lines = data.split("\n");

      ArrayList<String> parts = new ArrayList<String>();

      boolean put = false;

      for (String line : lines) {
        if (put) {
          if (line.startsWith("-->")) break;
          if (line.length() > 0) parts.add(line);
        }

        if (line.toLowerCase().startsWith("--> " + usage.toLowerCase())) {
          put = true;
        }
      }

      return String.join("\n", parts);
    }

    public static User toUser(ResultSet resultSet) throws SQLException {
      User user = new User();

      user.setUsername(resultSet.getString("username"));
      user.setPassword(resultSet.getString("password"));
      user.setEmail(resultSet.getString("email"));
      user.setCpf(resultSet.getString("cpf"));

      return user;
    }
  }
}
