package g12.Client.UI;

import g12.Middleware.DTO.ResponseDTO.BookFlightDTO;
import g12.Middleware.DTO.ResponseDTO.ResponseDTO;

public class DTOPrinter {

    public static void printFlightBooking(ResponseDTO dto) {
        switch (dto.getRespCode()) {
            case 200:
                BookFlightDTO resp = (BookFlightDTO) dto;
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

    }
}
