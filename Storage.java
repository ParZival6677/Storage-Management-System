import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class Storage {
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

        // Получаем текущую дату и время
        LocalDateTime now = LocalDateTime.now();

        // Удаляем миллисекунды из текущей даты и времени
        LocalDateTime truncatedDateTime = now.truncatedTo(ChronoUnit.SECONDS);

        // Создаем форматтер для вывода даты и времени без буквы "T"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Форматируем дату и время и выводим результат
        String formattedDateTime = truncatedDateTime.format(formatter);
        System.out.println(formattedDateTime);

        // Создаем продукт
        Food apple = new Food("Яблоко", 50, 100, true);

        // Увеличиваем количество яблок на складе
        apple.increaseAmount(50);

        // Уменьшаем количество яблок на складе
        apple.decreaseAmount(30);

        // Попытка списать больше яблок, чем есть на складе
        apple.decreaseAmount(80);

        // Создаем напиток
        Drink cola = new Drink("Кола", 100, 50, "газировка", 500);

        // Увеличиваем количество колы на складе
        cola.increaseAmount(30);

        // Уменьшаем количество колы на складе
        cola.decreaseAmount(20);

        // Попытка списать больше колы, чем есть на складе
        cola.decreaseAmount(40);

        // Создаем десерт
        Desert cake = new Desert("Торт", 500, 20, "шоколадный");

        // Увеличиваем количество тортов на складе
        cake.increaseAmount(10);

        // Уменьшаем количество тортов на складе
        cake.decreaseAmount(5);

        // Попытка списать больше тортов, чем есть на складе
        cake.decreaseAmount(30);


    }
}

class Product {
    private int id;
    private String name;
    private int price;
    private int amount;

    public Product() {

    }

    public Product(String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    // Геттеры и сеттеры для полей
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // Методы для управления количеством продуктов
    public void increaseAmount(int quantity) {
        this.amount += quantity;
        System.out.println(quantity + " " + name + " добавлено на склад. Общее количество: " + amount);
    }

    public void decreaseAmount(int quantity) {
        if (this.amount >= quantity) {
            this.amount -= quantity;
            System.out.println(quantity + " " + name + " списано со склада. Общее количество: " + amount);
        } else {
            System.out.println("Недостаточное количество " + name + " на складе.");
        }
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", amount=" + amount + "]";
    }
}


class Food extends Product implements Storable {
    private boolean expires;

    public Food() {
        super();
    }

    public Food(String name, int price, int amount, boolean expires) {
        super(name, price, amount);
        this.expires = expires;
    }

    // Геттер и сеттер для поля expires
    public boolean getExpires() {
        return expires;
    }

    public void setExpires(boolean expires) {
        this.expires = expires;
    }


    // Реализация методов интерфейса Storable
    @Override
    public void increaseAmount(int quantity) {
        setAmount(getAmount() + quantity);
        System.out.println(quantity + " " + getName() + " добавлено на склад. Общее количество: " + getAmount());
    }

    @Override
    public void decreaseAmount(int quantity) {
        if (getAmount() >= quantity) {
            setAmount(getAmount() - quantity);
            System.out.println(quantity + " " + getName() + " списано со склада. Общее количество: " + getAmount());
        } else {
            System.out.println("Недостаточное количество " + getName() + " на складе.");
        }
    }

    @Override
    public String toString() {
        return "Food [id=" + getId() + ", name=" + getName() + ", price=" + getPrice() + ", amount=" + getAmount() + ", expires=" + expires + "]";
    }
}

class Drink extends Product implements Storable, Purchasable{
    private String type;
    private int volume;

    public Drink() {
        super();
    }

    public Drink(String name, int price, int amount, String type, int volume) {
        super(name, price, amount);
        this.type = type;
        this.volume = volume;
    }

    // Геттеры и сеттеры для полей type и volume
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


    @Override
    public void increaseAmount(int quantity) {
        setAmount(getAmount() + quantity);
        System.out.println(quantity + " " + getName() + " добавлено на склад. Общее количество: " + getAmount());
    }

    @Override
    public void decreaseAmount(int quantity) {
        if (getAmount() >= quantity) {
            setAmount(getAmount() - quantity);
            System.out.println(quantity + " " + getName() + " списано со склада. Общее количество: " + getAmount());
        } else {
            System.out.println("Недостаточное количество " + getName() + " на складе.");
        }
    }

    // Реализация метода интерфейса Purchasable
    @Override
    public int getPrice() {
        return getPrice();
    }

    @Override
    public String toString() {
        return "Drink [id=" + getId() + ", name=" + getName() + ", price=" + getPrice() + ", amount=" + getAmount() + ", type=" + type + ", volume=" + volume + "]";
    }
}

class Desert extends Product implements Storable{
    private String type;

    public Desert() {
        super();
    }

    public Desert(String name, int price, int amount, String type) {
        super(name, price, amount);
        this.type = type;
    }

    // Геттер и сеттер для поля type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    // Реализация методов интерфейса Storable
    @Override
    public void increaseAmount(int quantity) {
        setAmount(getAmount() + quantity);
        System.out.println(quantity + " " + getName() + " добавлено на склад. Общее количество: " + getAmount());
    }

    @Override
    public void decreaseAmount(int quantity) {
        if (getAmount() >= quantity) {
            setAmount(getAmount() - quantity);
            System.out.println(quantity + " " + getName() + " списано со склада. Общее количество: " + getAmount());
        } else {
            System.out.println("Недостаточное количество " + getName() + " на складе.");
        }
    }

    @Override
    public String toString() {
        return "Desert [id=" + getId() + ", name=" + getName() + ", price=" + getPrice() + ", amount=" + getAmount() + ", type=" + type + "]";
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
                            resultSet.getInt("amount"),
                            resultSet.getBoolean("expires"));
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
                            resultSet.getInt("amount"),
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
        String sql = "INSERT INTO Desert (name, price, amount, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, desert.getName());
            preparedStatement.setInt(2, desert.getPrice());
            preparedStatement.setInt(3, desert.getAmount());
            preparedStatement.setString(4, desert.getType());
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
                            resultSet.getInt("amount"),
                            resultSet.getString("type"));
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
        String sql = "UPDATE Desert SET name = ?, price = ?, amount = ?, type = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, desert.getName());
            preparedStatement.setInt(2, desert.getPrice());
            preparedStatement.setInt(3, desert.getAmount());
            preparedStatement.setString(4, desert.getType());
            preparedStatement.setInt(5, desert.getId());
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

interface Storable {
    void increaseAmount(int quantity);
    void decreaseAmount(int quantity);
}

interface Purchasable {
    int getPrice();
}