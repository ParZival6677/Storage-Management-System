import java.util.ArrayList; // import ArrayList to create a dynamic space for all products
import java.time.LocalDateTime; // import LocalDateTime to get local date and time and create a check
import java.time.temporal.ChronoUnit; // import ChronoUnit to truncate milliseconds
import java.time.format.DateTimeFormatter; // import formatter to get rid of "T" in output

class Storage {
    ArrayList<Food> foods = new ArrayList<Food>();
    ArrayList<Drink> drinks = new ArrayList<Drink>();
    ArrayList<Desert> deserts = new ArrayList<Desert>();
    private Object Desert;

    boolean subtractProduct(String name, int amount) {
        for (int i=0; i<foods.size(); i++) {
            if (foods.get(i).getName()==name) {
                if (amount<=foods.get(i).getAmount()) {
                    foods.get(i).setAmount(foods.get(i).getAmount()-amount);
                    return true;
                } else {return false;}
            }
        }
        for (int i=0; i<drinks.size(); i++) {
            if (drinks.get(i).getName()==name) {
                if (amount<=drinks.get(i).getAmount()) {
                    drinks.get(i).setAmount(drinks.get(i).getAmount()-amount);
                    return true;
                } else {return false;}
            }
        }
        for (int i=0; i<deserts.size(); i++) {
            if (deserts.get(i).getName()==name) {
                if (amount<=deserts.get(i).getAmount()) {
                    deserts.get(i).setAmount(deserts.get(i).getAmount()-amount);
                    return true;
                } else {return false;}
            }
        }
        return false;
    }
    void deleteFood(String name) {
        for (int i=0; i<foods.size(); i++) {
            if (foods.get(i).getName()==name) {
                foods.remove(i);
            }
        }
    }
    void deleteDrink(String name) {
        for (int i=0; i<drinks.size(); i++) {
            if (drinks.get(i).getName()==name) {
                drinks.remove(i);
            }
        }
    }
    void deleteDesert(String name) {
        for (int i=0; i<deserts.size(); i++) {
            if (deserts.get(i).getName()==name) {
                deserts.remove(i);
            }
        }
    }
    void newFood(Food food, int amount) {
        if (amount>0) {
            Food newFood = new Food(food);
            newFood.setAmount(amount);
            foods.add(newFood);
        }
    }

    void newDrink(Drink drink, int amount) {
        if (amount>0) {
            Drink newDrink = new Drink(drink);
            newDrink.setAmount(amount);
            drinks.add(newDrink);
        }
    }

    void newDesert(Desert desert, int amount) {
        if (amount>0) {
            Desert newDesert = new Desert("pudding", Desert);
            newDesert.setAmount(amount);
            this.deserts.add(newDesert);
        }
    }
}

class Check extends Storage {
    int getTotalCost() {
        int sum=0;
        for (int i=0; i<foods.size(); i++) {
            sum+=foods.get(i).getAmount()*foods.get(i).getPrice();
        }
        for (int i=0; i<drinks.size(); i++) {
            sum+=drinks.get(i).getAmount()*drinks.get(i).getPrice();
        }
        for (int i=0; i<deserts.size(); i++) {
            sum+=deserts.get(i).getAmount()*deserts.get(i).getPrice();
        }
        return sum;
    }
    private LocalDateTime outTime;
    public void setOutTime() {
        LocalDateTime time = LocalDateTime.now();
        this.outTime =time.truncatedTo(ChronoUnit.SECONDS);
    }
    String getOutTime() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(outTime);
    }
    public Check() {
        this.setOutTime();
    }

    void addFood(Storage storage, Food food, int amount) {
        if (storage.subtractProduct(food.getName(), amount)) {
            this.newFood(food, amount);
        } else {
            System.out.println("Failed to add to cart");
        }
    }
    void addDrink(Storage storage, Drink drink, int amount) {
        if (storage.subtractProduct(drink.getName(), amount)) {
            this.newDrink(drink, amount);
        } else {
            System.out.println("Failed to add to cart");
        }
    }
    void addDesert(Storage storage, Desert desert, int amount) {
        if (storage.subtractProduct(desert.getName(), amount)) {
            this.newDesert(desert, amount);
        } else {
            System.out.println("Failed to add to cart");
        }
    }
    public boolean editCheckProduct(Storage storage, String name, int amount) {

        for (int i=0; i<foods.size(); i++) {
            if (foods.get(i).getName()==name) {
                if (amount+foods.get(i).getAmount()>0 && storage.subtractProduct(name, amount)) {
                    foods.get(i).setAmount(foods.get(i).getAmount()+amount);
                    return true;
                } else if (amount+foods.get(i).getAmount()<=0) {
                    storage.subtractProduct(name, -foods.get(i).getAmount());
                    deleteFood(name);
                    return true;
                } else {return false;}
            }
        }

        for (int i=0; i<drinks.size(); i++) {
            if (drinks.get(i).getName()==name) {
                if (amount+drinks.get(i).getAmount()>0 && storage.subtractProduct(name, amount)) {
                    drinks.get(i).setAmount(drinks.get(i).getAmount()+amount);
                    return true;
                } else if (amount+foods.get(i).getAmount()<=0) {
                    storage.subtractProduct(name, -drinks.get(i).getAmount());
                    deleteDrink(name);
                    return true;
                } else {return false;}
            }
        }

        for (int i=0; i<deserts.size(); i++) {
            if (deserts.get(i).getName()==name) {
                if (amount+deserts.get(i).getAmount()>0 && storage.subtractProduct(name, amount)) {
                    deserts.get(i).setAmount(deserts.get(i).getAmount()+amount);
                    return true;
                } else if (amount+deserts.get(i).getAmount()<=0) {
                    storage.subtractProduct(name, -deserts.get(i).getAmount());
                    deleteDesert(name);
                    return true;
                } else {return false;}
            }
        }
        return true;
    }

    public boolean deleteCheckProduct(Storage storage, String name) {
        for (int i=0; i<foods.size(); i++) {
            if (foods.get(i).getName()==name) {
                editCheckProduct(storage, foods.get(i).getName(), -this.foods.get(i).getAmount());
                return true;
            }
        }
        for (int i=0; i<drinks.size(); i++) {
            if (drinks.get(i).getName()==name) {
                editCheckProduct(storage, drinks.get(i).getName(), -this.drinks.get(i).getAmount());
                return true;
            }
        }
        for (int i=0; i<deserts.size(); i++) {
            if (deserts.get(i).getName()==name) {
                editCheckProduct(storage, deserts.get(i).getName(), -this.deserts.get(i).getAmount());
                return true;
            }
        }
        return false;
    }
}

class Product {
    private String name;
    private int price;
    private int amount;
    private boolean expires;

    Product() {
    }

    void setName(String name) {
        this.name=name;
    }

    void setPrice(int price) {
        this.price = price;
    }

    void setAmount(int amount) {
        this.amount=amount;
    }

    void setExpiration(String expires) {
        if (expires.toLowerCase()=="yes") {
            this.expires = true;
        } else {
            this.expires = false;
        }
    }
    void setExpires(boolean expires) {
        this.expires=expires;
    }

    boolean getExpires() {
        return expires;
    }
    String getName() {
        return name;
    }
    int getPrice() {
        return price;
    }
    int getAmount() {
        return amount;
    }

    public Product(String name, int price, String expires) {
        this.setName(name);
        this.price=price;
        this.amount=0;
        this.setExpiration(expires);
    }
    public Product(Product product) {
        this.setName(product.getName());
        this.setPrice(product.getPrice());
        this.amount=0;
        this.setExpires(product.getExpires());
    }

}

class Food extends Product {
    private int weight;
    void setWeight(int weight) {
        this.weight = weight;
    }
    int getWeight() {
        return weight;
    }


    public Food(String name, int price, int weight) {
        super();
        this.setWeight(weight);
    }
    public Food(Food food) {
        super(food);
        this.setWeight(food.getWeight());
    }

    public void setId(int id) {

    }

    public int getId() {
        return 0;
    }
}
class Drink extends Product {
    private String type;
    private int volume;
    void setType(String type) {
        this.type = type;
    }
    String getType() {
        return type;
    }

    void setVolume(int volume) {this.volume=volume;}

    int getVolume () {return volume;}
    public Drink(String name, int price, String type, int volume) {
        this.setType(type);
        this.setVolume(volume);
    }
    public Drink(Drink drink) {
        super(drink);
        this.setType(drink.getType());
        this.setVolume(drink.getVolume());
    }

    public void setId(int id) {

    }

    public int getId() {
        return 0;
    }
}

class Desert extends Product {
    public Desert(String pudding, Object desert) {
    }

    public Desert(String name, int price, int amount) {

    }

    public void setId(int id) {

    }

    public int getId() {
        return 0;
    }
}



public class Main {
    public static void main(String[] args)
    {
        Storage storage = new Storage();
        Check check1 = new Check();
        System.out.println(check1.getTotalCost());
        System.out.println(check1.getOutTime());
        Desert inj = new Desert("pudding", "yes");
        storage.newDesert(inj, 8);
        System.out.println(inj.getAmount());
        System.out.println("Current deserts in storage: " + storage.deserts.get(0).getAmount());
        check1.addDesert(storage, storage.deserts.get(0), 5);
        System.out.println("Current deserts in storage: " + storage.deserts.get(0).getAmount());
        if (check1.editCheckProduct(storage, "pancake", 3)) {
            System.out.println("pancake edited succesfully!");
        } else {
            System.out.println("Error");
        }
        System.out.println("Current deserts in storage: " + storage.deserts.get(0).getAmount());
        System.out.println(check1.deserts.get(0).getName());
        System.out.println(check1.deserts.get(0).getAmount());
        System.out.println(check1.getTotalCost());
        check1.deleteCheckProduct(storage, "pancake");
        System.out.println(storage.deserts.get(0).getAmount());

    }
}

