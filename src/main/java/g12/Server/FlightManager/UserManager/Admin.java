package g12.Server.FlightManager.UserManager;

/**
 * Utilizador com permissoes especiais
 */
public class Admin extends User {

    public Admin(Admin ad){
        super(ad);
    }

    @Override
    public User clone() {
        return new Admin(this);
    }

    @Override
    public String toString() {
        return null;
    }
}