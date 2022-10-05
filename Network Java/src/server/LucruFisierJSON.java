package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class LucruFisierJSON {
    private Socket server;
    private BufferedReader reader;
    private PrintWriter writer;
    private FileWriter file;
    private static JSONArray clienti = new JSONArray();

    public LucruFisierJSON(Socket server) throws IOException {
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
        this.writer = new PrintWriter(server.getOutputStream(), true);
    }

    public void introducereDate() throws IOException {

        this.writer.println("Introduceti cati clienti vreti sa adaugati");
        int nr = Integer.parseInt(reader.readLine());
        for (int i = 0; i < nr; i++) {
            JSONObject client = new JSONObject();
            this.writer.println("ID: ");
            int nrIntrodus = Integer.parseInt(reader.readLine());
            client.put("id", nrIntrodus);
            this.writer.println("Varsta: ");
            nrIntrodus = Integer.parseInt(reader.readLine());
            client.put("varsta", nrIntrodus);
            this.writer.println("Prenume: ");
            String comanda = reader.readLine();
            client.put("prenume", comanda);
            this.writer.println("Nume: ");
            comanda = reader.readLine();
            client.put("nume", comanda);
            clienti.put(client);
        }
        this.writer.println(clienti.length());

    }

    public void scriereFisier()throws Exception{
        String text = "";
        for (int i = 0; i < clienti.length(); i++) {
            text += clienti.getJSONObject(i).toString();
            text += "\n";
        }
        try (PrintWriter out = new PrintWriter(new FileWriter("clienti.txt"))) {
            out.write(text);
        }
    }

    public void vizualizareArrayJSON()throws IOException{
        if(clienti==null){this.writer.println("Vectorul este null");}
        else
        {
            for(int i=0;i<clienti.length();i++){
                this.writer.println(clienti.getJSONObject(i).toString());
        }
        }
    }

    public void vizualizareFisierJSON() throws IOException {
        String now;
        try {
            BufferedReader objReader = new BufferedReader(new FileReader("clienti.txt"));
            while ((now = objReader.readLine()) != null) {
                JSONObject o = new JSONObject(now);
                String afisare="";
                afisare="ID: "+Integer.toString(o.getInt("id"));
                afisare+="Prenume: "+o.getString("prenume");
                afisare+="Nume: "+o.getString("nume");
                afisare+="Varsta: "+Integer.toString(o.getInt("varsta"));
                this.writer.println(afisare);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adaugareFisierJSONinJSONArray() throws IOException {
        String now;
        try {
            BufferedReader objReader = new BufferedReader(new FileReader("clienti.txt"));
            while ((now = objReader.readLine()) != null) {
                JSONObject o = new JSONObject(now);
                clienti.put(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
