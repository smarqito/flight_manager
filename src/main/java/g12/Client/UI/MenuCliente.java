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
     * Método para apresentar o menu que permite reservar um 
     * voo ao cliente
     */
    public void menuReservarVoo() {
        System.out.println("Menu para Reservar Voo");

        Menu menu = new Menu(new String[]{
            "Ver voos disponíveis"
            "Fazer reserva"
        });

        //Registar handlers
        menu.setHandler(1, this::menuVerVoosDisp);
        menu.setHandler(2, this::menuReservarVoo);

        menu.run();
    }

    /**
     * Método que permite ao cliente ver o voos disponíveis
     */
    public void menuVerVoosDisp() {
        
        Params p = new Params(0);

        try {
            Response r = this.c.queryHandler("availableFlights", p);
        } catch(IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }

    /**
     * Método que permite ao cliente reservar um voo
     */
    public void menuReservarVoo() {
        
        Params p = new Params(3);

        System.out.println("Insira o seu percurso desejado(separado com , )");
        String perc = ClientUI.scin.nextLine();
        p.add(perc);

        System.out.println("Insira a data mínima!");
        String min = ClientUI.scin.nextLine();
        p.add(min);

        System.out.println("Insira a data máxima!");
        String max = ClientUI.scin.nextLine();
        p.add(max);

        try {
            Response r = this.c.queryHandler("bookFlight", p);
            
            if(r.getRespCode() == 200) {
                System.out.println("Registo efetuado com sucesso!");
            } else if(r.getRespCode() == 400) {
                System.out.println("Utilizador nao existe ou nao é cliente!");
            } else
                System.out.println("Recusado.Verifique os parametros inseridos!");
        } catch (IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }


    /**
     * Método que permite ao cliente cancelar um voo
     */
    public void menuCancelarVoo() {

        Params p = new Params(1);

        System.out.println("Insira o identificador do voo a cancelar:");
        String id = ClientUI.scin.nextLine();
        p.add(id);

        try{
            Response r = this.c.queryHandler("cancelBook", p);

            if(r.getRespCode() == 200) {
                System.out.println("Cancelamento com sucesso");
            
            } else if(r.getRespCode() == 300) {
                System.out.println("Cancelamento nao sucedido");
            } else if(r.getRespCode() == 404) {
                System.out.println("Utilizador nao existe ou nao e cliente");
            } else 
                System.out.println("Pedido mal construído, parâmetros não válidos!");
        } catch( IOException e) {
            System.out.println("Houve problemas de comunicação. Tente novamente.");
        }
    }
}
