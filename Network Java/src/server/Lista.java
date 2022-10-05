package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Lista {
    private Socket server;
    private BufferedReader reader;
    private PrintWriter writer;
    private static List<String> lista=new ArrayList<String>();
    public Lista(Socket server)throws IOException {
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
        this.writer = new PrintWriter(server.getOutputStream(),true);
    }
    public void adaugareList() throws IOException {
        this.writer.println("Introduceti  ");
        lista.add(reader.readLine());
        this.writer.println("A fost introdus cu succes");
    }
    public void vizualizareLista()throws IOException{
        if(lista.size()==0){this.writer.println("Nu sunt element in vector, va rog adaugati");}
        else {
            for (String string : lista) {
                this.writer.println(string);
            }
        }
    }
}
