package g12.Client.UI;

import java.util.Scanner;

import g12.Server.FlightManager.IFlightManager;
import g12.Server.FlightManager.Exceptions.LoginInvalido;

public class ClientUI {
    
    public static Scanner scin;

    /**
     * Construtor para o UI da parte cliente do sistema
     * 
     * @param model Camada de negócio 
     */
    public ClientUI() {
        scin = new Scanner(System.in);

    }

    /**
     * Método run para executar a apresentação dos menus
     * 
     * Começa com o menu inicial
     */
    public void run() {
        System.out.println("Bem vindo ao Flight Manager!");
        this.menuInicial();
        System.out.println("Até breve...");
    }

    /**
     * Método para apresentar o menu inicial do sistema -> Flight Manager
     * 
     * A este ponto só é permitido ao user entrar ou registar-se 
     * no sistema
     */
    private void menuInicial() {
        Menu menu = new Menu(new String[]{
            "Entrar",
            "Registar-se"
        });

        // Registar os handlers das transições
        menu.setHandler(1, this::menuLogin);
        menu.setHandler(2, this::menuRegister);

        // Executar o menu
        menu.run();
    }

    /**
     * Método para apresentar o menu Entrar ao user
     * 
     * Permite autenticar-se no sistema, caso seja um user
     * registado no sistema
     */
    private void menuLogin() {

        System.out.println("Insira o seu nome de Utilizador: ");
        String id = scin.nextLine();

        System.out.println("Insira a sua palavra-passe: ");
        String pass = scin.nextLine();

    }

    /**
     * Método para apresentar o menu Registar-se ao user
     * 
     * 
     */
    private void menuRegister() {

    }

}
