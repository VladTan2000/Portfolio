package Server.ComenziCMD.ComenziListe;

import Server.ComenziCMD.IComanda.Comanda;
import Server.ComenziCMD.StreamGobbler.StreamGobblerList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComandaHistory implements Comanda  {

    List<String> listaAplicatii=new ArrayList<>();
    String eroare="";
    private String user;


    @Override
    public void exec() {
        try {
            String locatie=System.getenv("LOCALAPPDATA");
            locatie+="\\Google\\Chrome\\User Data\\Default\\History";
            Process p = Runtime.getRuntime().exec("ssh -T "+user);
            PrintWriter out = new PrintWriter(p.getOutputStream(),true);
            String sLocatie=String.format("copy-item \"%s\" C:\\Temp\\History",locatie);
            out.println(sLocatie);
            out.println("cd C:\\Temp");
            out.println("./sqlite3.exe History 'select url,title from urls'");
            out.println("Remove-Item History");
            out.println("exit");
            StreamGobblerList stdout=new StreamGobblerList(p.getInputStream());
            StreamGobblerList stderr=new StreamGobblerList(p.getErrorStream());
            Thread threadOut=new Thread(stdout);
            Thread threadErr=new Thread(stderr);
            threadOut.start();
            threadErr.start();
            p.waitFor();
            threadOut.join();
            threadErr.join();
            if(!stderr.getLista().isEmpty()){
                eroare="Eroare";
                return;
            }
            this.listaAplicatii=stdout.getLista();
            this.listaAplicatii=this.listaAplicatii.stream().filter(s -> s.contains("|")).collect(Collectors.toList());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public List<String> getLista(){
        return listaAplicatii;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getListaErori() {
        return eroare;
    }
}

