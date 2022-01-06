package g12.Middleware.DTO.ResponseDTO;

/**
 * Representa o retorno de um boolean
 * closeDay
 * cancelBook
 */
public class UnitDTO extends ResponseDTO {

    public UnitDTO(int tag, Integer respCode) {
        super(tag, UnitDTO.class.getSimpleName(), respCode);
    }
}