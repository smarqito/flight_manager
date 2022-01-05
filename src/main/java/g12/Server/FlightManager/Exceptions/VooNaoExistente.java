package g12.Server.FlightManager.Exceptions;

public class VooNaoExistente extends Exception {

    public VooNaoExistente() {
        super();
    }

    public VooNaoExistente(String msg){
        super(msg);
    }
}
