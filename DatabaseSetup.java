import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Storage", "postgres", "D763512892045002d");
             Statement statement = connection.createStatement()) {

            // таблица Food
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Food (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price INT NOT NULL," +
                    "amount INT NOT NULL," +
                    "expires BOOLEAN NOT NULL)");

            // таблица Drink
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Drink (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price INT NOT NULL," +
                    "amount INT NOT NULL," +
                    "type VARCHAR(255) NOT NULL," +
                    "volume INT NOT NULL)");

            // таблица Desert
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Desert (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price INT NOT NULL," +
                    "amount INT NOT NULL," +
                    "type VARCHAR(255) NOT NULL)");

            System.out.println("Таблицы успешно созданы!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class FoodDAO {
    private Connection connection;

    public FoodDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(Food food) {
        String sql = "INSERT INTO Food (name, price, amount, expires) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, food.getName());
            preparedStatement.setInt(2, food.getPrice());
            preparedStatement.setInt(3, food.getAmount());
            preparedStatement.setBoolean(4, food.getExpires());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Food read(int id) {
        String sql = "SELECT * FROM Food WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Food food = new Food(resultSet.getString("name"),
                            resultSet.getInt("price"),
                            resultSet.getInt("amount"));
                    food.setExpires(resultSet.getBoolean("expires"));
                    food.setId(resultSet.getInt("id"));
                    return food;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Food food) {
        String sql = "UPDATE Food SET name = ?, price = ?, amount = ?, expires = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, food.getName());
            preparedStatement.setInt(2, food.getPrice());
            preparedStatement.setInt(3, food.getAmount());
            preparedStatement.setBoolean(4, food.getExpires());
            preparedStatement.setInt(5, food.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Food WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class DrinkDAO {
    private Connection connection;

    public DrinkDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(Drink drink) {
        String sql = "INSERT INTO Drink (name, price, type, volume) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, drink.getName());
            preparedStatement.setInt(2, drink.getPrice());
            preparedStatement.setString(3, drink.getType());
            preparedStatement.setInt(4, drink.getVolume());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drink read(int id) {
        String sql = "SELECT * FROM Drink WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Drink drink = new Drink(resultSet.getString("name"),
                            resultSet.getInt("price"),
                            resultSet.getString("type"),
                            resultSet.getInt("volume"));
                    drink.setId(resultSet.getInt("id"));
                    return drink;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Drink drink) {
        String sql = "UPDATE Drink SET name = ?, price = ?, type = ?, volume = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, drink.getName());
            preparedStatement.setInt(2, drink.getPrice());
            preparedStatement.setString(3, drink.getType());
            preparedStatement.setInt(4, drink.getVolume());
            preparedStatement.setInt(5, drink.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Drink WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class DesertDAO {
    private Connection connection;

    public DesertDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(Desert desert) {
        String sql = "INSERT INTO Desert (name, price, amount) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, desert.getName());
            preparedStatement.setInt(2, desert.getPrice());
            preparedStatement.setInt(3, desert.getAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Desert read(int id) {
        String sql = "SELECT * FROM Desert WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Desert desert = new Desert(resultSet.getString("name"),
                            resultSet.getInt("price"),
                            resultSet.getInt("amount"));
                    desert.setId(resultSet.getInt("id"));
                    return desert;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Desert desert) {
        String sql = "UPDATE Desert SET name = ?, price = ?, amount = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, desert.getName());
            preparedStatement.setInt(2, desert.getPrice());
            preparedStatement.setInt(3, desert.getAmount());
            preparedStatement.setInt(4, desert.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Desert WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
