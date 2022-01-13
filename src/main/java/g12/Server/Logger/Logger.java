package g12.Server.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static FileWriter erros;
    private static FileWriter pedidos;

    public static void InitLogger() throws IOException {
        File f = new File("erros.log");
        f.createNewFile(); // não cria caso já exista
        erros = new FileWriter(f);
        f = new File("pedidos.log");
        f.createNewFile(); // não cria caso já exista
        pedidos = new FileWriter(f);
    }

    private static void Write(FileWriter fw, String log) {
        try {
            fw.append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "> " + log + "\n");
            fw.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void WriteError(String log) {
        Write(erros, log);
    }

    public static void WriteError(String[] logs) {
        for (String log : logs) {
            Write(erros, log);
        }
    }
    public static void WriteLog(String log) {
        Write(pedidos, log);
    }

    public static void WriteLog(String[] logs) {
        for (String log : logs) {
            Write(pedidos, log);
        }
    }
}
