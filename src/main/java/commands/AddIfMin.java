package commands;


import commands.exceptions.WrongArgumentsAmountException;
import managers.ProductManager;
import models.IdGenerator;
import models.IncorrectStringValueException;
import models.Product;
import models.creators.ProductCreator;

public class AddIfMin extends Command {
    private ProductManager<Product> productCollection;

    public AddIfMin(ProductManager<Product> manager) {
        super();
        this.productCollection = manager;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше чем у наименьшего элемента этой коллекции";
    }

    @Override
    public String toString() {
        return "add_if_min";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        Product product = new ProductCreator().createProduct();
        if (this.productCollection.getCollection().first().compareTo(product) > 0) {
            this.productCollection.addProdut(product);
            System.out.println("Продукт успешно добавлен в коллекцию");
        }
        IdGenerator.updateCounter(this.productCollection.getCollection());
        System.out.println("Элемент невозможно добавить в коллекцию, так как он больше наименьшего элемента ^^");
    }
}
