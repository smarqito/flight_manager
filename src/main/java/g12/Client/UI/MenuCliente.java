package g12.Client.UI;

import java.io.IOException;
import java.time.LocalDate;

import g12.Client.Client;
import g12.Middleware.BadRequest;
import g12.Middleware.DTO.QueryDTO.GetFlightListQueryDTO;
import g12.Middleware.DTO.ResponseDTO.*;
import g12.Middleware.Params;
import g12.Middleware.DTO.QueryDTO.AvailableFlightsQueryDTO;
import g12.Middleware.DTO.QueryDTO.BookFlightQueryDTO;
import g12.Middleware.DTO.QueryDTO.CancelBookQueryDTO;
import g12.Middleware.DTO.ResponseDTO.AvailableFlightsDTO;
import g12.Middleware.DTO.ResponseDTO.ResponseDTO;
import g12.Middleware.DTO.ResponseDTO.UnitDTO;

public class MenuCliente {

    private Client c;

    /**
     * Construtor para o menu a apresentar ao cliente
     */
    public MenuCliente(Client c) {
        this.c = c;
    }

    /**
     * Método para apresentar o menu principal para um cliente
     */
    public void menuCliente() {
        System.out.println("Menu do Cliente");

        Menu menu = new Menu(new String[] {
                "Reservar Voo",
                "Cancelar Voo"
        });

        // Registar handlers
        menu.setHandler(1, this::menuReservarVoo);
        menu.setHandler(2, this::menuCancelarVoo);

        menu.run();
    }

    /**
     * Método para apresentar o menu que permite reservar um
     * voo ao cliente
     */
    public void menuReservarVoo() {
        System.out.println("Menu para Reservar Voo");

        Menu menu = new Menu(new String[] {
                "Ver voos disponíveis",
                "Ver percursos possíveis (limitado a 2 escalas)",
                "Fazer reserva",
                "Ver pedidos de reserva concluídos"
        });

        // Registar handlers
        menu.setHandler(1, this::menuVerVoosDisp);
        menu.setHandler(2, this::menuPercursosPossiveis);
        menu.setHandler(3, this::menuReserva);
        menu.setHandler(4, this::menuReservasPendentes);

        menu.setPreCondition(4, () -> this.c.cm.hasPending() > 0);

        menu.run();
    }

    /**
     * Método que permite ao cliente ver o voos disponíveis
     */
    public void menuVerVoosDisp() {

        try {
            AvailableFlightsQueryDTO q = new AvailableFlightsQueryDTO();
            AvailableFlightsDTO r = (AvailableFlightsDTO) this.c.queryHandler(q);
            System.out.println(r.toString());

        } catch (IOException | BadRequest e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método que permite ao cliente reservar um voo
     */
    public void menuReserva() {

        System.out.println("Insira o seu percurso desejado(separado com , )");
        String perc = ClientUI.scin.nextLine();

        System.out.println("Insira a data mínima! [YYYY-MM-dd]");
        LocalDate min = ClientUI.getDate();

        System.out.println("Insira a data máxima! [YYYY-MM-dd]");
        LocalDate max = ClientUI.getDate();
        try {
            String[] s = perc.split(",");
            Params params = new Params(s.length);
            params.addAll(s);
            BookFlightQueryDTO q = new BookFlightQueryDTO(params, min, max);
            this.c.asyncHandler(q);
            System.out.println("O seu pedido foi efetuado.");
            System.out.println("Pode acompanhar no menu de pendentes!");
        } catch (IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método que permite ao cliente cancelar um voo
     */
    public void menuCancelarVoo() {

        System.out.println("Insira o identificador do voo a cancelar:");
        String id = ClientUI.scin.nextLine();

        try {
            CancelBookQueryDTO q = new CancelBookQueryDTO(id);
            UnitDTO r = (UnitDTO) this.c.queryHandler(q);

            switch (r.getRespCode()) {
                case 200:
                    System.out.println("Cancelamento com sucesso");
                    break;
                case 300:
                    System.out.println("Cancelamento nao sucedido");
                    break;
                default:
                    System.out.println("Utilizador nao existe ou nao e cliente");
                    break;
            }
        } catch (IOException | BadRequest e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método que obter os percursos possíveis entre uma origem e destino (limitado
     * a 2 escalas)
     */
    private void menuPercursosPossiveis() {
        try {
            System.out.println("Insira o seu percurso desejado(separado por linha)");
            String origem = ClientUI.scin.nextLine();
            String dest = ClientUI.scin.nextLine();
            GetFlightListQueryDTO q = new GetFlightListQueryDTO(origem, dest);
            ResponseDTO r = (ResponseDTO) this.c.queryHandler(q);
            switch (r.getRespCode()) {
                case 200:
                    GetFlightListDTO resp = (GetFlightListDTO) r;
                    System.out.println(resp.toString());
                    break;
                default:
                    UnitDTO u = (UnitDTO) r;
                    System.out.println("erro");
            }
        } catch (BadRequest | IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }

    }

    private void menuReservasPendentes() {
        this.c.cm.getEndedThreads().stream().forEach(x -> DTOPrinter.printFlightBooking((ResponseDTO) x));
    }
}
