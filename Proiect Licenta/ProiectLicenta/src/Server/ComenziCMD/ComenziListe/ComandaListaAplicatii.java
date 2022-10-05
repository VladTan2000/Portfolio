package Server.ComenziCMD.ComenziListe;

import Server.ComenziCMD.IComanda.Comanda;
import Server.ComenziCMD.StreamGobbler.StreamGobblerList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComandaListaAplicatii implements Comanda {

    List<String> listaAplicatii=new ArrayList<>();
    String eroare="";
    private String user;


    @Override
    public void exec() {
        try {

            Process p = Runtime.getRuntime().exec("ssh -T "+user);

            PrintWriter out = new PrintWriter(p.getOutputStream(),true);
            out.println("Get-ItemProperty HKLM:\\\\Software\\\\Wow6432Node\\\\Microsoft\\\\Windows\\\\CurrentVersion\\\\Uninstall\\\\* | Select-Object DisplayName,DisplayVersion,Publisher,InstallLocation | Format-List | out-string -width 300");
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
            listaAplicatii=stdout.getLista();
            listaAplicatii=listaAplicatii.stream().filter(s -> s.contains("DisplayName")|| s.contains("DisplayVersion")||s.contains("Publisher")||s.contains("InstallLocation")).collect(Collectors.toList());
            listaAplicatii.remove(0);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public List<String> getLista(){
        return listaAplicatii;
    }

    public String getEroare() {return eroare;}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
