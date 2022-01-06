package g12.Middleware.DTO.ResponseDTO;

import g12.Server.FlightManager.BookingManager.Voos;

public class AvailableFlightsDTO extends ResponseDTO {
    private Voos voos;

    // TODO
    public AvailableFlightsDTO(Integer respCode) {
        super(respCode);
    }
}