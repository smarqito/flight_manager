package g12.Client.UI;

import java.io.IOException;

import g12.Client.Client;
import g12.Middleware.Params;
import g12.Middleware.Response;

public class MenuAdmin {

    private Client c;

    /**
     * Construtor para o menu do administrador
     */
    public MenuAdmin(Client c) {
        this.c = c;
    }

    /**
     * Método para apresentar o menu principal ao administrador
     */
    public void menuAdmin() {
        System.out.println("Menu do Administrador!");
        Menu menu = new Menu(new String[] {
                "Registar voo diário",
                "Encerrar dia"
        });

        // Registar os handlers
        menu.setHandler(1, this::menuRegistarVoo);
        menu.setHandler(2, this::menuEncerrarDia);

        menu.run();
    }

    /**
     * Método para apresentar ao administrador o menu para
     * registar um novo voo diário
     */
    public void menuRegistarVoo() {

        Params p = new Params(3);

        System.out.println("Insira a origem do seu voo:");
        String origem = ClientUI.scin.nextLine();
        p.add(origem);

        System.out.println("Insira o destino do seu voo:");
        String dest = ClientUI.scin.nextLine();
        p.add(dest);

        System.out.println("Insira a capcidade:");
        Integer cap = ClientUI.getInt();
        p.add(String.valueOf(cap));

        try {
            Response r = this.c.queryHandler("registerFlight", p);

            if (r.getRespCode().equals(200)) {
                System.out.println("Registo efetuado com sucesso!");
            } else {
                System.out.println("Recusado.Verifique os parâmetros inseridos!");
            }

        } catch (IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }

    }

    /**
     * Método para apresentar ao administrador o menu para
     * encerrar o dia, no flight manager
     */
    public void menuEncerrarDia() {

        Params p = new Params(0);

        try {
            Response r = this.c.queryHandler("closeDay", p);

            if(r.getRespCode() == 200) {
                System.out.println("Pedido efetuado com sucesso!");
            } else if(r.getRespCode() == 400) {
                System.out.println("Nao tem permissoes para encerrar o dia!");
            } else {
                System.out.println("O pedido foi mal construído!");
            }

        } catch (IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }

    }

}
