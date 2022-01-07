package g12.Client.UI;

import java.io.IOException;
import java.time.LocalDate;

import g12.Client.Client;
import g12.Middleware.BadRequest;
import g12.Middleware.Params;
import g12.Middleware.DTO.QueryDTO.AvailableFlightsQueryDTO;
import g12.Middleware.DTO.QueryDTO.BookFlightQueryDTO;
import g12.Middleware.DTO.QueryDTO.CancelBookQueryDTO;
import g12.Middleware.DTO.ResponseDTO.AvailableFlightsDTO;
import g12.Middleware.DTO.ResponseDTO.BookFlightDTO;
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
                "Fazer reserva"
        });

        // Registar handlers
        menu.setHandler(1, this::menuVerVoosDisp);
        menu.setHandler(2, this::menuReserva);

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
            ResponseDTO r = (ResponseDTO) this.c.queryHandler(q);
            switch (r.getRespCode()) {
                case 200:
                    BookFlightDTO resp = (BookFlightDTO) r;
                    System.out.println("Reserva efetuada com o ID: " + resp.getBookId());
                    break;
                case 401:
                    System.out.println("Nao tem permissoes.");
                    break;
                case 402:
                    System.out.print("Voo não existe!");
                    break;
                case 403:
                    System.out.println("Percurso não disponível!");
                    break;
                default:
                    System.out.println("Recusado.Verifique os parametros inseridos!");
                    break;
            }
        } catch (IOException |

                BadRequest e) {
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
}
