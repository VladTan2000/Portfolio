package Server.ComenziCMD.ComenziInfo;

import Server.ComenziCMD.IComanda.Comanda;
import Server.ComenziCMD.StreamGobbler.StreamGobblerList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ComandaSession implements Comanda {
    String user;
    List<String> lista;
    String session;
    String eroare="";
    @Override
    public void exec() {
        try {
            Process p = Runtime.getRuntime().exec("ssh -T " + user);
            PrintWriter out=new PrintWriter(p.getOutputStream(),true);
            StreamGobblerList in=new StreamGobblerList(p.getInputStream());
            StreamGobblerList stderr=new StreamGobblerList(p.getErrorStream());

            Thread threadOut=new Thread(in);
            Thread threadErr=new Thread(stderr);
            threadOut.start();
            threadErr.start();
            out.println("Get-Process explorer | Select-Object -ExpandProperty SI -First 1 | Format-List");
            out.println("exit");
            p.waitFor();
            threadOut.join();
            threadErr.join();
            if(!stderr.getLista().isEmpty()){
                eroare="Eroare";
                return;
            }
            lista=in.getLista();
            session=lista.stream().filter(s -> s.matches("[0-9]+")).findFirst().get();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLista() {
        return session;
    }

    public void setLista(List<String> lista) {
        this.lista = lista;
    }

    public String getEroare() {
        return eroare;
    }
}
