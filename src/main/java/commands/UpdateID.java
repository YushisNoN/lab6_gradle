package commands;


import commands.exceptions.WrongArgumentsAmountException;
import managers.ProductManager;
import models.*;
import models.creators.ProductCreator;

public class UpdateID extends Command {
    private ProductManager<Product> productManager;


    public UpdateID(ProductManager<Product> manager) {
        super();
        this.productManager = manager;
        this.isNeedArguments = true;
        this.commandArguments = 1;
    }

    @Override
    public void execute(String[] arguments)
            throws WrongArgumentsAmountException, IncorrectStringValueException, IncorrectIntegerValueException {
        if (arguments.length != this.commandArguments) {
            throw new WrongArgumentsAmountException();
        }
        if (arguments[arguments.length - 1].matches("^-?\\d+$") == false) {
            throw new IncorrectIntegerValueException();
        }
        long id = Long.parseLong(arguments[arguments.length - 1]);
        for (Product product : this.productManager.getCollection()) {
            if (product.getId() == id) {
                this.productManager.getCollection().remove(product);
                IdGenerator.updateCounter(this.productManager.getCollection());
                System.out.println("Для обновления элемента нужно ввести параметры.");
                Product updatedProduct = new ProductCreator().createProduct();
                this.productManager.addProdut(updatedProduct);
                System.out.println("Продукт был успешно обновлён в коллекции");
            }
        }
        System.out.println("Элемента с таким id не существует в коллекции :(");
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "update_id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public String toString() {
        return "update_id";
    }
}
