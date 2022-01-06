package g12.Middleware.DTO;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class DTO {

    public abstract void serialize(DataOutputStream out) throws IOException;
}