package utils.kernel;


import client.Client;
import commands.*;
import commands.exceptions.WrongCommandFoundException;
import managers.CommandManager;
import managers.ProductManager;
import models.Product;;
import utils.console.ConsoleHandler;
import utils.files.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


public class Kernel {
    private boolean exitProgram = false;
    public ConsoleHandler consoleManager = new ConsoleHandler();
    private CommandManager commandManager = new CommandManager();
    private ProductManager<Product> productManager = new ProductManager<Product>();
    private FileReader fileReader = new FileReader();

    private Client client;

    public void setCommands() {
        this.commandManager.addCommand(new Add(this.client));
        this.commandManager.addCommand(new AddIfMax(this.productManager));
        this.commandManager.addCommand(new AddIfMin(this.productManager));
        this.commandManager.addCommand(new RemoveGreater(this.productManager));
        this.commandManager.addCommand(new Exit(this));
        this.commandManager.addCommand(new ExecuteScript(this));
    }
    public void exitProgram() {
        this.exitProgram = true;
    }

    public Kernel() {
        try {
            this.fileReader.read(this.productManager);
        } catch (FileNotFoundException exception) {
            System.out.println("Файла не существует :(");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        // TODO 192.168.10.80 - helios lcoal ip
        this.client = new Client("localhost", 2205, false);
    }
    public void runProgram()  {

        try {
            client.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.consoleManager.printString("-> ");
        while (false == this.exitProgram && this.consoleManager.getInputStream().hasNextLine()) {
            String currentInput = this.consoleManager.getInputString().trim();
            if (this.exitProgram)
                break;
            try {
                this.consoleManager.printString("-> ");
                if(this.commandManager.getCommandsList().containsKey(currentInput.split(" ")[0])) {
                    this.executeCommand(currentInput);
                } else {
                    client.sendRequest(currentInput.getBytes(StandardCharsets.UTF_8));
                    Object response = client.receiveResponse(5000);
                    if (response != null) {
                        if (response instanceof String) {
                            System.out.println((String) response);
                            if(this.commandManager.getCommandsList().containsKey(((String) response).split(" ")[0])) {
                                this.executeCommand((String) response);
                            }
                        }
                    }
                }
                this.consoleManager.printString("-> ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void executeCommand(String currentInput) {
        String[] currentArguments = Arrays.stream(currentInput.replaceAll("\\s+", " ").trim().split(" "))
                .skip(1).toArray(String[]::new);
        Executable currentCommand = this.commandManager.getCommandsList().get(currentInput.split(" ")[0]);
        try {
            if (null == currentCommand) {
                throw new WrongCommandFoundException();
            } else {
                if (currentCommand.getNeededArguments() || currentArguments.length > 0) {
                    currentCommand.execute(currentArguments);
                    return;
                }
                currentCommand.execute();

            }
        } catch (Exception exception) {
            this.consoleManager.printStringln(exception.getMessage());
        }
    }
    public void executeCommandsFromScript(List<String> commands) {
        for (String command : commands) {
            executeCommand(command);
        }
    }
}
