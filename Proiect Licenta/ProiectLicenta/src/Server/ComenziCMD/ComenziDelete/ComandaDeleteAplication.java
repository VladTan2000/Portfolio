package Server.ComenziCMD.ComenziDelete;

import Server.ComenziCMD.ComenziInfo.ComenziInfo;
import Server.ComenziCMD.IComanda.ComandaString;
import Server.ComenziCMD.StreamGobbler.StreamGobblerList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComandaDeleteAplication implements ComandaString {

    List<String> listaApp=new ArrayList<>();
    private String user;

    String eroare="";


    @Override
    public void execString(String argument)  {
        String verificare = "";
        String uninstallString = "";
        try {
            if(new ComenziInfo().getComandaSession(user).equals("Eroare")){
                eroare="Eroare";
                return;
            }
            String session=new ComenziInfo().getComandaSession(user);
            Process process = Runtime.getRuntime().exec("ssh -T "+user);
            PrintWriter out = new PrintWriter(process.getOutputStream(),true);
            out.println("Get-ItemProperty HKLM:\\\\Software\\\\Wow6432Node\\\\Microsoft\\\\Windows\\\\CurrentVersion\\\\Uninstall\\\\* | Select-Object DisplayName,UninstallString | Format-List | out-string -width 300");
            out.println("exit");
            StreamGobblerList streamGobblerList=new StreamGobblerList(process.getInputStream());
            Thread stdout=new Thread(streamGobblerList);
            stdout.start();
            process.waitFor();
            stdout.join();
            listaApp=streamGobblerList.getLista();
            listaApp=listaApp.stream().filter(s -> s.contains("DisplayName")||s.contains("UninstallString")).collect(Collectors.toList());
            listaApp.remove(0);
            Process process2 = Runtime.getRuntime().exec("ssh "+user);
            PrintWriter out2 = new PrintWriter(process2.getOutputStream(),true);
            for (int i = 0; i < listaApp.size(); i=i+2) {
                verificare = listaApp.get(i).substring(17);
                if (verificare.equalsIgnoreCase(argument)) {
                    uninstallString = listaApp.get(i + 1).substring(17);
                    out2.println("psexec.exe -i "+session +" -s cmd /c "+uninstallString);
                    break;
                }
            }
        } catch (IOException ioException){throw new RuntimeException(ioException);} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


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

