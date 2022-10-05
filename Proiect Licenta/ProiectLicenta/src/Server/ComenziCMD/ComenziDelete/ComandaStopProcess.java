package Server.ComenziCMD.ComenziDelete;

import Server.ComenziCMD.IComanda.ComandaString;
import Server.ComenziCMD.StreamGobbler.StreamGobblerList;

import java.io.IOException;
import java.io.PrintWriter;

public class ComandaStopProcess implements ComandaString {

    private String user;

    String eroare="";


    @Override
    public void execString(String argument) {
        try {
            Process process = Runtime.getRuntime().exec("ssh -T " + user);
            PrintWriter out = new PrintWriter(process.getOutputStream(), true);
            out.println("stop-process -id " + argument + " -Force");
            out.println("exit");
            StreamGobblerList stdout = new StreamGobblerList(process.getInputStream());
            StreamGobblerList stderr=new StreamGobblerList(process.getErrorStream());
            Thread threadOut = new Thread(stdout);
            Thread threadErr=new Thread(stderr);
            threadOut.start();
            threadErr.start();
            process.waitFor();
            threadOut.join();
            threadErr.join();
            if(!stderr.getLista().isEmpty()){
                eroare="Eroare";
                return;
            }

        } catch (IOException | InterruptedException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getErore() {
        return eroare;
    }
}




