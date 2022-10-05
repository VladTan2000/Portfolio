package Server.ComenziCMD.ComenziInfo;

import Server.ComenziCMD.IComanda.Comanda;
import Server.ComenziCMD.StreamGobbler.StreamGobbler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ComandaScreenShot implements Comanda{

    BufferedImage bufferedImage;
    private String user;

    private String nume;

    String eroare="";


    @Override
    public void exec() {
        try {
            if(new ComenziInfo().getComandaSession(user).equals("Eroare")){
                eroare="Eroare";
                return;
            }
            String[] numes=nume.split("\\\\");
            nume=numes[0];
            String session=new ComenziInfo().getComandaSession(user);
            Process p = Runtime.getRuntime().exec("ssh -T "+user);
            StreamGobbler stdout=new StreamGobbler(p.getInputStream());
            Thread threadOut=new Thread(stdout);
            threadOut.start();
            PrintWriter out = new PrintWriter(p.getOutputStream(),true);
            out.println("Get-Process explorer | Select-Object -Property SI -First 1 | Format-List");
            out.println("psexec -i " +session+  " -s  javaw -cp C:\\Temp\\ScreenShot ComandaScreenShotLocal");
            out.println("cd C:\\Temp");
            out.println("scp C:\\Temp\\" +nume  + ".jpg  C:\\Temp\\SSH\\");
            out.println("exit");
            p.waitFor();
            threadOut.join();
            String locatie="C:\\Temp\\SSH\\"+nume+".jpg";
            bufferedImage= ImageIO.read(new File(locatie));


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEroare() {
        return eroare;
    }
}

