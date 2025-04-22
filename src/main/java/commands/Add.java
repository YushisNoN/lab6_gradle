package commands;


import client.Client;
import commands.exceptions.WrongArgumentsAmountException;
import managers.ProductManager;
import models.IncorrectStringValueException;
import models.Product;
import models.creators.ProductCreator;

import java.io.IOException;

public class Add extends Command {

    private Client client;
    public Add(Client client) {
        super();
        this.client = client;
    }

    @Override
    public void execute(String[] arguments) throws WrongArgumentsAmountException, IncorrectStringValueException {
        throw new WrongArgumentsAmountException();
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }

    @Override
    public String toString() {
        return "add";
    }

    @Override
    public void execute() throws WrongArgumentsAmountException {
        Product product = new ProductCreator().createProduct();
        try {
            this.client.sendProduct(product);
            Object response = client.receiveResponse(5000);
            if (response != null) {
                if (response instanceof String) {
                    System.out.println((String) response);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка отправки объекта. Объект не был доставлен на сервер");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
