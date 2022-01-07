package g12.Client.UI;

import java.io.IOException;

import g12.Client.Client;
import g12.Middleware.BadRequest;
import g12.Middleware.DTO.QueryDTO.CloseDayQueryDTO;
import g12.Middleware.DTO.QueryDTO.RegisterFlightQueryDTO;
import g12.Middleware.DTO.ResponseDTO.UnitDTO;

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

        System.out.println("Insira a origem do seu voo:");
        String origem = ClientUI.scin.nextLine();

        System.out.println("Insira o destino do seu voo:");
        String dest = ClientUI.scin.nextLine();

        System.out.println("Insira a capacidade:");
        Integer cap = ClientUI.getInt();

        try {
            RegisterFlightQueryDTO q = new RegisterFlightQueryDTO(origem, dest, cap);
            UnitDTO r = (UnitDTO) this.c.queryHandler(q);

            if (r.getRespCode().equals(200)) {
                System.out.println("Registo efetuado com sucesso!");
            }
        } catch (IOException | BadRequest e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }

    }

    /**
     * Método para apresentar ao administrador o menu para
     * encerrar o dia, no flight manager
     */
    public void menuEncerrarDia() {

        try {
            CloseDayQueryDTO q = new CloseDayQueryDTO();
            UnitDTO r = (UnitDTO) this.c.queryHandler(q);
            switch (r.getRespCode()) {
                case 200:
                    System.out.println("Pedido efetuado com sucesso!");
                    break;
                case 402:
                    System.out.println("O dia não existe!");
                    break;
                default:
                    System.out.println("Nao tem permissoes para encerrar o dia!");
                    break;
            }
        } catch (IOException | BadRequest e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }

    }

}
