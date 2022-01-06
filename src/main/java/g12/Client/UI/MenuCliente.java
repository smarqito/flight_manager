package g12.Client.UI;

import g12.Client.Client;

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
    public void menuCliente(){
        System.out.println("Menu do Cliente");

        Menu menu = new Menu(new String[]{
            "Reservar Voo",
            "Cancelar Voo"
        });

        // Registar handlers 
        menu.setHandler(1, this::menuReservarVoo);
        menu.setHandler(2, this::menuCancelarVoo);

        menu.run();
    }

    /**
    1.1. : Reservar Voo
    1.1.1 Ver voos disponiveis
    1.1.2 Fazer reserva

    1.1.2 : Fazer reserva
    1.1.2.1 Pedir percurso, separados por virgula
    1.1.2.2 Pedir data minima
    1.1.2.3 Pedir data maxima
     */

    public void menuReservarVoo() {
        System.out.println("Menu para Reservar Voo");

        Menu menu = new Menu(new String[]{
            "Ver voos disponíveis"
            "Fazer reserva"
        });

        menu.run();
    }

    public void menuCancelarVoo() {
        
        Param p = new Params(3);

        System.out.println("Insira o seu percurso desejado(separado com , )");
        String params = ClientUI.scin.nextLine();

    }
}
