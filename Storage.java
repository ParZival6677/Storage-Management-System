import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Storage {
    public static void main(String[] args) {
        boolean running = true;
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


        // Получаем текущую дату и время
            LocalDateTime now = LocalDateTime.now();

        // Удаляем миллисекунды из текущей даты и времени
            LocalDateTime truncatedDateTime = now.truncatedTo(ChronoUnit.SECONDS);

        // Создаем форматтер для вывода даты и времени без буквы "T"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println(formatter.format(truncatedDateTime));

        // Create a Scanner object to get user input
            Scanner scanner = new Scanner(System.in);


            while (running) {
                System.out.println("Выберите категорию: 1 - Food, 2 - Drink, 3 - Desert, 0 - Выйти");
                int category = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                switch (category) {
                    case 1:
                        handleFood(scanner, connection);
                        break;
                    case 2:
                        handleDrink(scanner, connection);
                        break;
                    case 3:
                        handleDesert(scanner, connection);
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор категории.");
                        break;
                }

                // Ask the user if they want to see the stock and expiration status
                if (running) {
                    System.out.println("Хотите посмотреть информацию со склада? (yes/no)");
                    String response = scanner.nextLine().toLowerCase();
                    while (!response.equals("yes") && !response.equals("no")) {
                        System.out.println("Пожалуйста, введите 'yes' или 'no':");
                        response = scanner.nextLine().toLowerCase();
                    }

                    if (response.equals("yes")) {
                        Check stockChecker = new Check(connection);
                        stockChecker.checkStockAndExpiration();
                    } else {
                        System.out.println("Спасибо за использование нашей программы!");
                    }


                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to handle Food operations
    private static void handleFood(Scanner scanner, Connection connection) {

        System.out.println("Выберите действие: 1 - Создать, 2 - Удалить, 3 - Обновить, 4 - Просмотреть");
        int action = scanner.nextInt();
        FoodDAO foodDAO = new FoodDAO(connection);

        switch (action) {
            case 1:
                System.out.print("Введите имя продукта: ");
                String name = scanner.next();

                System.out.print("Введите цену продукта: ");
                int price = scanner.nextInt();

                System.out.print("Введите количество продукта: ");
                int amount = scanner.nextInt();

                System.out.print("Продукт имеет срок годности? (true/false): ");
                boolean expires = scanner.nextBoolean();

                Food newFood = new Food(name, price, amount, expires);
                foodDAO.create(newFood);
                System.out.println(newFood.getName() + " успешно добавлен в категорию Food!");
                break;

            case 2:
                // Delete product
                System.out.println("Введите ID продукта для удаления:");
                int deleteId = scanner.nextInt();
                foodDAO.delete(deleteId);
                System.out.println("Продукт с ID " + deleteId + " успешно удален!");
                break;
            case 3:
                // Update product
                System.out.println("Введите ID продукта для обновления:");
                int updateId = scanner.nextInt();

                Food foodToUpdate = foodDAO.read(updateId);
                if (foodToUpdate != null) {
                    // Ask for updated product details
                    System.out.println("Введите новое имя продукта:");
                    String newName = scanner.next();
                    foodToUpdate.setName(newName);

                    System.out.println("Введите новую цену продукта:");
                    int newPrice = scanner.nextInt();
                    foodToUpdate.setPrice(newPrice);

                    System.out.println("Введите новое количество продукта:");
                    int newAmount = scanner.nextInt();
                    foodToUpdate.setAmount(newAmount);

                    System.out.println("Продукт имеет срок годности? (true/false):");
                    boolean newExpires = scanner.nextBoolean();
                    foodToUpdate.setExpires(newExpires);

                    foodDAO.update(foodToUpdate);
                    System.out.println("Продукт с ID " + updateId + " успешно обновлен!");
                } else {
                    System.out.println("Продукт с ID " + updateId + " не найден.");
                }
                break;
            case 4:
                // View product
                System.out.println("Введите ID продукта для просмотра:");
                int viewId = scanner.nextInt();
                Food foodToView = foodDAO.read(viewId);
                if (foodToView != null) {
                    System.out.println(foodToView.toString());
                } else {
                    System.out.println("Продукт с ID " + viewId + " не найден.");
                }
                break;
            default:
                System.out.println("Неверный выбор действия.");
                break;
        }


    }

    // Function to handle Drink operations
    private static void handleDrink(Scanner scanner, Connection connection) {
        System.out.println("Выберите действие: 1 - Создать, 2 - Удалить, 3 - Обновить, 4 - Просмотреть");
        int action = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()

        DrinkDAO drinkDAO = new DrinkDAO(connection);

        switch (action) {
            case 1:
                System.out.print("Введите имя продукта: ");
                String name = scanner.next();

                System.out.print("Введите цену продукта: ");
                int price = scanner.nextInt();

                System.out.print("Введите количество продукта: ");
                int amount = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Введите тип напитка: ");
                String type = scanner.next();

                System.out.print("Введите объем напитка: ");
                int volume = scanner.nextInt();

                Drink newDrink = new Drink(name, price, amount, type, volume);
                drinkDAO.create(newDrink);
                System.out.println(newDrink.getName() + " успешно добавлен в категорию Drink!");
                break;

            case 2:
                // Delete product
                System.out.println("Введите ID продукта для удаления:");
                int deleteId = scanner.nextInt();
                drinkDAO.delete(deleteId);
                System.out.println("Продукт с ID " + deleteId + " успешно удален!");
                break;

            case 3:
                // Update product
                System.out.println("Введите ID продукта для обновления:");
                int updateId = scanner.nextInt();

                Drink drinkToUpdate = drinkDAO.read(updateId);
                if (drinkToUpdate != null) {
                    // Ask for updated product details
                    System.out.println("Введите новое имя продукта:");
                    String newName = scanner.next();
                    drinkToUpdate.setName(newName);

                    System.out.println("Введите новую цену продукта:");
                    int newPrice = scanner.nextInt();
                    drinkToUpdate.setPrice(newPrice);

                    System.out.println("Введите новое количество продукта:");
                    int newAmount = scanner.nextInt();
                    drinkToUpdate.setAmount(newAmount);

                    System.out.println("Введите новый тип напитка:");
                    String newType = scanner.next();
                    drinkToUpdate.setType(newType);

                    System.out.println("Введите новый объем напитка:");
                    int newVolume = scanner.nextInt();
                    drinkToUpdate.setVolume(newVolume);

                    drinkDAO.update(drinkToUpdate);
                    System.out.println("Продукт с ID " + updateId + " успешно обновлен!");
                } else {
                    System.out.println("Продукт с ID " + updateId + " не найден.");
                }
                break;

            case 4:
                // View product
                System.out.println("Введите ID продукта для просмотра:");
                int viewId = scanner.nextInt();
                Drink drinkToView = drinkDAO.read(viewId);
                if (drinkToView != null) {
                    System.out.println(drinkToView.toString());
                } else {
                    System.out.println("Продукт с ID " + viewId + " не найден.");
                }
                break;

            default:
                System.out.println("Неверный выбор действия.");
                break;
        }
    }
    // Function to handle Desert operations
    private static void handleDesert(Scanner scanner, Connection connection) {
        System.out.println("Выберите действие: 1 - Создать, 2 - Удалить, 3 - Обновить, 4 - Просмотреть");
        int action = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()

        DesertDAO desertDAO = new DesertDAO(connection);

        switch (action) {
            case 1:
                System.out.print("Введите имя продукта: ");
                String name = scanner.next();

                System.out.print("Введите цену продукта: ");
                int price = scanner.nextInt();

                System.out.print("Введите количество продукта: ");
                int amount = scanner.nextInt();

                System.out.print("Введите тип десерта: ");
                String type = scanner.next();

                Desert newDesert = new Desert(name, price, amount, type);
                desertDAO.create(newDesert);
                System.out.println(newDesert.getName() + " успешно добавлен в категорию Desert!");
                break;

            case 2:
                // Delete product
                System.out.println("Введите ID продукта для удаления:");
                int deleteId = scanner.nextInt();
                desertDAO.delete(deleteId);
                System.out.println("Продукт с ID " + deleteId + " успешно удален!");
                break;

            case 3:
                // Update product
                System.out.println("Введите ID продукта для обновления:");
                int updateId = scanner.nextInt();

                Desert desertToUpdate = desertDAO.read(updateId);
                if (desertToUpdate != null) {
                    // Ask for updated product details
                    System.out.println("Введите новое имя продукта:");
                    String newName = scanner.next();
                    desertToUpdate.setName(newName);

                    System.out.println("Введите новую цену продукта:");
                    int newPrice = scanner.nextInt();
                    desertToUpdate.setPrice(newPrice);

                    System.out.println("Введите новое количество продукта:");
                    int newAmount = scanner.nextInt();
                    desertToUpdate.setAmount(newAmount);

                    System.out.println("Введите новый тип десерта:");
                    String newType = scanner.next();
                    desertToUpdate.setType(newType);

                    desertDAO.update(desertToUpdate);
                    System.out.println("Продукт с ID " + updateId + " успешно обновлен!");
                } else {
                    System.out.println("Продукт с ID " + updateId + " не найден.");
                }
                break;

            case 4:
                // View product
                System.out.println("Введите ID продукта для просмотра:");
                int viewId = scanner.nextInt();
                Desert desertToView = desertDAO.read(viewId);
                if (desertToView != null) {
                    System.out.println(desertToView.toString());
                } else {
                    System.out.println("Продукт с ID " + viewId + " не найден.");
                }
                break;

            default:
                System.out.println("Неверный выбор действия.");
                break;
        }
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

    @Override
    public void increaseAmount(Scanner quantity) {

    }

    @Override
    public void decreaseAmount(Scanner quantity) {

    }
}



class Check {
    private Connection connection;

    public Check(Connection connection) {
        this.connection = connection;
    }


    // Method to check and display stock and expiration status for Food
    public void checkFoodStockAndExpiration() {
        String sql = "SELECT * FROM Food";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Food Stock and Expiration Status:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int amount = resultSet.getInt("amount");
                boolean expires = resultSet.getBoolean("expires");

                System.out.println("ID: " + id + ", Name: " + name +", Price " + price +"$" + ", Amount: " + amount + ", Expires: " + expires);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check and display stock and expiration status for Drink
    public void checkDrinkStockAndExpiration() {
        String sql = "SELECT * FROM Drink";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Drink Stock and Expiration Status:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int amount = resultSet.getInt("amount");
                String type = resultSet.getString("type");
                int volume = resultSet.getInt("volume");

                System.out.println("ID: " + id + ", Name: " + name + ", Price " + price +"$" + ", Amount: " + amount + ", Type: " + type + ", Volume: " + volume);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check and display stock and expiration status for Desert
    public void checkDesertStockAndExpiration() {
        String sql = "SELECT * FROM Desert";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Desert Stock and Expiration Status:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int amount = resultSet.getInt("amount");
                String type = resultSet.getString("type");

                System.out.println("ID: " + id + ", Name: " + name + ", Price " + price +"$" +  ", Amount: " + amount + ", Type: " + type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check and display stock and expiration status for all categories
    public void checkStockAndExpiration() {
        checkFoodStockAndExpiration();
        checkDrinkStockAndExpiration();
        checkDesertStockAndExpiration();
    }
}



class Drink extends Product implements Storable, Purchasable{
    private String type;
    private int volume;

    public Drink(String name, int price, int amount, boolean expires) {
        super(name, price, amount);
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
        return super.getPrice();
    }

    @Override
    public String toString() {
        return "Drink [id=" + getId() + ", name=" + getName() + ", price=" + getPrice() + ", amount=" + getAmount() + ", type=" + type + ", volume=" + volume + "]";
    }

    @Override
    public void increaseAmount(Scanner quantity) {

    }

    @Override
    public void decreaseAmount(Scanner quantity) {

    }
}

class Desert extends Product implements Storable{
    private String type;

    public Desert(String productName, int productPrice, int productAmount, boolean productExpires) {
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

    @Override
    public void increaseAmount(Scanner quantity) {

    }

    @Override
    public void decreaseAmount(Scanner quantity) {

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
            System.out.println(food.getName() + " успешно обновлено в базе данных.");
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
            System.out.println("Продукт с ID " + id + " успешно удален из базы данных.");
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
        String sql = "INSERT INTO Drink (name, price, amount, type, volume) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, drink.getName());
            preparedStatement.setInt(2, drink.getPrice());
            preparedStatement.setInt(3, drink.getAmount()); // Set the "amount" field
            preparedStatement.setString(4, drink.getType());
            preparedStatement.setInt(5, drink.getVolume());
            preparedStatement.executeUpdate();
            System.out.println(drink.getName() + " успешно добавлен в базу данных.");
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
            System.out.println("Продукт с ID " + id + " успешно удален из базы данных.");
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
            System.out.println(desert.getName() + " успешно обновлено в базе данных.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Desert WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Продукт с ID " + id + " успешно удален из базы данных.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

interface Storable {
    void increaseAmount(Scanner quantity);
    void decreaseAmount(Scanner quantity);
}

interface Purchasable {
    int getPrice();
}