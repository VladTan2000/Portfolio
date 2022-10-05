package Server.ComenziCMD.ComenziInfo;

import Server.ComenziCMD.IComanda.Comanda;
import Server.ComenziCMD.StreamGobbler.StreamGobbler;

import java.io.IOException;

public class ComandaNumeUser implements Comanda {
    String nume="";
    @Override
    public void exec() {
        ProcessBuilder pb = new ProcessBuilder("whoami");
        try {
            Process proc = pb.start();
            StreamGobbler output = new StreamGobbler(proc.getInputStream());
            Thread stdout = new Thread(output);
            stdout.start();
            proc.waitFor();
            stdout.join();
            nume=output.getContents();
        }
        catch (IOException | InterruptedException x) {
            x.printStackTrace();
        }
    }
    public String getNume(){

        return nume;
    }
}



