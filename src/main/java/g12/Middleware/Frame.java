package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import static java.util.Map.entry;

import g12.Middleware.DTO.DTO;
import g12.Middleware.DTO.QueryDTO.AvailableFlightsQueryDTO;
import g12.Middleware.DTO.QueryDTO.BookFlightQueryDTO;
import g12.Middleware.DTO.QueryDTO.CancelBookQueryDTO;
import g12.Middleware.DTO.QueryDTO.CloseDayQueryDTO;
import g12.Middleware.DTO.QueryDTO.LoginQueryDTO;
import g12.Middleware.DTO.QueryDTO.RegisterFlightQueryDTO;
import g12.Middleware.DTO.QueryDTO.RegisterUserQueryDTO;
import g12.Middleware.DTO.ResponseDTO.AvailableFlightsDTO;
import g12.Middleware.DTO.ResponseDTO.BookFlightDTO;
import g12.Middleware.DTO.ResponseDTO.LoginDTO;
import g12.Middleware.DTO.ResponseDTO.UnitDTO;

public class Frame {
    static Map<String, Class<? extends DTO>> mapping = Map.ofEntries(
            entry(UnitDTO.class.getSimpleName(), UnitDTO.class),
            entry(BookFlightDTO.class.getSimpleName(), BookFlightDTO.class),
            entry(AvailableFlightsDTO.class.getSimpleName(), AvailableFlightsDTO.class),
            entry(LoginDTO.class.getSimpleName(), LoginDTO.class),
            entry(LoginQueryDTO.class.getSimpleName(), LoginQueryDTO.class),
            entry(RegisterUserQueryDTO.class.getSimpleName(), RegisterUserQueryDTO.class),
            entry(RegisterFlightQueryDTO.class.getSimpleName(), RegisterFlightQueryDTO.class),
            entry(CloseDayQueryDTO.class.getSimpleName(), CloseDayQueryDTO.class),
            entry(BookFlightQueryDTO.class.getSimpleName(), BookFlightQueryDTO.class),
            entry(CancelBookQueryDTO.class.getSimpleName(), CancelBookQueryDTO.class),
            entry(AvailableFlightsQueryDTO.class.getSimpleName(), AvailableFlightsQueryDTO.class));

    static Class<? extends DTO> getMapping(String m) {
        return mapping.get(m);
    }

    public final int tag;
    private final String className;
    private DTO dto;

    public Frame(int tag, DTO dto) {
        this.tag = tag;
        this.className = dto.getClass().getSimpleName();
        this.dto = dto;
    }

    public DTO getDto() {
        return dto;
    }

    public void setDto(DTO dto) {
        this.dto = dto;
    }

    public String getClassName() {
        return className;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(className);
        out.writeInt(tag);
        dto.serialize(out);
    }

    public static Frame deserialize(DataInputStream in) throws IOException {
        String cName = in.readUTF();
        int tag = in.readInt();
        try {
            Class<? extends DTO> p = getMapping(cName);
            Method m = p.getMethod("deserialize", DataInputStream.class);
            DTO dto = (DTO) m.invoke(null, in);
            return new Frame(tag, dto);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new IOException("Tipo nao corresponde");
        }
    }
}