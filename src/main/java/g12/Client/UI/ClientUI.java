package g12.Client.UI;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import g12.Client.Client;
import g12.Middleware.BadRequest;
import g12.Middleware.DTO.QueryDTO.LoginQueryDTO;
import g12.Middleware.DTO.QueryDTO.RegisterUserQueryDTO;
import g12.Middleware.DTO.ResponseDTO.LoginDTO;
import g12.Middleware.DTO.ResponseDTO.UnitDTO;

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
        System.out.println("Menu Inicial!");
        Menu menu = new Menu(new String[] {
                "Entrar",
                "Registar-se"
        });

        // Registar os handlers das transições
        menu.setHandler(1, this::menuLogin);
        menu.setHandler(2, this::menuRegister);

        // Executar o menu
        menu.runOnce();
    }

    /**
     * Método para apresentar o menu Entrar ao user
     * 
     * Permite autenticar-se no sistema, caso seja um user
     * registado no sistema
     * Se falhar na autenticação, volta ao menu inicial
     */
    private void menuLogin() {
        LoginQueryDTO q = new LoginQueryDTO();

        System.out.println("Insira o seu nome de Utilizador: ");
        String id = scin.nextLine();
        q.setUser(id);
        System.out.println("Insira a sua palavra-passe: ");
        String pass = scin.nextLine();
        q.setPass(pass);
        try {
            LoginDTO r = this.c.loginHandler(q);
            if (r.getRespCode().equals(200)) {

                DecodedJWT tok_dec = JWT.decode(r.getToken());
                boolean isAdmin = tok_dec.getClaim("isAdmin").asBoolean();
                System.out.println(r.getToken());
                if (isAdmin)
                    ma.menuAdmin();
                else
                    mc.menuCliente();
            } else {
                System.out.println("O login do utilizador está inválido!");
                this.menuInicial();
            }
        } catch (IOException | BadRequest e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método para apresentar o menu Registar-se ao user
     * 
     * Se registar-se no sistema com sucesso, volta ao menu inicial.
     */
    private void menuRegister() {

        System.out.println("Insira o seu nome de Utilizador: ");
        String id = scin.nextLine();
        System.out.println("Insira a sua palavra-passe: ");
        String pass = scin.nextLine();
        try {
            RegisterUserQueryDTO r = new RegisterUserQueryDTO(id, pass);
            UnitDTO resp = (UnitDTO) this.c.queryHandler(r);
            switch (resp.getRespCode()) {
                case 200:
                    System.out.println("Registo efetuado com sucesso!");
                    this.menuInicial();
                    break;
                default:
                    System.out.println("Ja existe um utilizador com o mesmo nome");
                    this.menuInicial();
                    break;
            }
        } catch (IOException | BadRequest e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método que lê do scanner um int
     * Só termina quando um int válido é inserido
     */
    public static int getInt() {
        while (!scin.hasNextInt()) {
            scin.next();
            System.out.println("Insira uma quantidade válida");
        }
        int res = scin.nextInt();
        scin.nextLine();
        return res;
    }

    public static LocalDate getDate() {
        while (true) {
            try {
                String s = scin.nextLine();
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Data no formato errado: [dd-MM-yyyy]");
            }
        }
    }
}
