package commands;

import commands.exceptions.WrongArgumentsAmountException;
import managers.ProductManager;
import models.IdGenerator;
import models.IncorrectStringValueException;
import models.Product;
import models.creators.ProductCreator;

public class RemoveGreater extends Command {
    private ProductManager<Product> productCollection;

    public RemoveGreater(ProductManager<Product> manager) {
        super();
        this.productCollection = manager;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "remove_greater {element} : удалить из коллекции все элементы, превыщающие заданный.";
    }

    @Override
    public String toString() {
        return "remove_greater";
    }

    @Override
    public void execute() {
        Product product = new ProductCreator().createProduct();
        int sizeOld = this.productCollection.getCollection().size();
        this.productCollection.getCollection()
                .removeAll(this.productCollection.getCollection().tailSet(product, false));
        int sizeNew = this.productCollection.getCollection().size();
        IdGenerator.updateCounter(this.productCollection.getCollection());
        System.out.println("Из коллекции успешно удалено " + (sizeOld - sizeNew) + " Элементов");
    }
}
