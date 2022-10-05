package Server.ComenziCMD.ComenziInfo;

import Server.ComenziCMD.IComanda.Comanda;
import Server.ComenziCMD.StreamGobbler.StreamGobblerList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComandaLocatie implements Comanda  {

    List<String> listaAplicatii=new ArrayList<>();
    private String user;
    String eroare="";


    @Override
    public void exec() {
        try {

            Process p = Runtime.getRuntime().exec("ssh -T "+user);
            PrintWriter out = new PrintWriter(p.getOutputStream(),true);
            StreamGobblerList stdout=new StreamGobblerList(p.getInputStream());
            StreamGobblerList stderr=new StreamGobblerList(p.getErrorStream());
            Thread threadOut=new Thread(stdout);
            Thread threadErr=new Thread(stderr);
            threadOut.start();
            threadErr.start();
            out.println("$WebResponse = Invoke-WebRequest \"http://ip-api.com/csv/?fields=61439\"");
            out.println("$WebResponse | Select-Object Content");
            out.println("exit");
            p.waitFor();
            threadOut.join();
            threadErr.join();
            if(!stderr.getLista().isEmpty()){
                eroare="Eroare";
                return;
            }
            listaAplicatii=stdout.getLista();
            listaAplicatii=listaAplicatii.stream().filter(s -> s.contains("success")).collect(Collectors.toList());


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public String getLista(){
        return listaAplicatii.get(0).substring(7);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getEroare() {
        return eroare;
    }
}


