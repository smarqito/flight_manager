package g12.Client.UI;

public class MenuAdmin {
    
    /**
     * Construtor para o menu do administrador
     */
    public MenuAdmin() {

    }

    /**
     * Método para apresentar o menu principal ao administrador
     */
    public void menuAdmin() {
        
        Menu menu = new Menu(new String[]{
            "Registar voo",
            "Encerrar dia"
        });

        // Registar os handlers

        menu.run();
    }


    public void menuRegistarVoo(){
        
    }
}
