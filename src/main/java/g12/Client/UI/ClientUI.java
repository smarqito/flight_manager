package g12.Client.UI;

import java.io.IOException;
import java.util.Scanner;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import g12.Client.Client;
import g12.Middleware.Params;
import g12.Middleware.Response;

public class ClientUI {

    private Client c;
    public static Scanner scin;
    private MenuAdmin ma;
    private MenuCliente mc;

    /**
     * Construtor para o UI da parte cliente do sistema
     * 
     * @param model Camada de negócio
     */
    public ClientUI(Client c) {
        scin = new Scanner(System.in);
        this.c = c;
        this.ma = new MenuAdmin(c);
        this.mc = new MenuCliente(c);
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
        System.out.println("Menu Inicial");
        Menu menu = new Menu(new String[] {
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

        Params p = new Params(2);

        boolean login = false;
        while (!login) {
            System.out.println("Insira o seu nome de Utilizador: ");
            String id = scin.nextLine();
            p.add(id);
            System.out.println("Insira a sua palavra-passe: ");
            String pass = scin.nextLine();
            p.add(pass);
            try {
                Response r = this.c.loginHandler(p);
                if(r.getRespCode().equals(200)){
                    login = true;

                    DecodedJWT tok_dec = JWT.decode(r.getRespBody());
                    boolean isAdmin = tok_dec.getClaim("isAdmin").asBoolean();
                    System.out.println(r.getRespBody());
                    if(isAdmin) 
                        ma.menuAdmin();
                    else 
                        mc.menuCliente();
                }
            } catch (IOException e) {
                System.out.println("Houve problemas de comunicação. Tente novamente.");
            }
        }
    }

    /**
     * Método para apresentar o menu Registar-se ao user
     * 
     * Se registar-se no sistema com sucesso, volta ao menu inicial.
     */
    private void menuRegister() {

        Params p = new Params(2);

        System.out.println("Insira o seu nome de Utilizador: ");
        String id = scin.nextLine();
        p.add(id);
        System.out.println("Insira a sua palavra-passe: ");
        String pass = scin.nextLine();
        p.add(pass);

        try {
            Response r = this.c.queryHandler("registerUser", p);
            System.out.println(r.getRespBody());
        } catch (IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método que lê do scanner um int
     * Só termina quando um int válido é inserido
     */
    public static int getInt(){
        while (!scin.hasNextInt()) {
            scin.next();
            System.out.println("Insira uma quantidade válida");
        }
        return scin.nextInt();
    }

}
