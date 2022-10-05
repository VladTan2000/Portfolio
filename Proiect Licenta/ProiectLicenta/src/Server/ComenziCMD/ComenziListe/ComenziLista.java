package Server.ComenziCMD.ComenziListe;

import java.util.List;

public class ComenziLista {
    private Server.ComenziCMD.ComenziListe.ComandaListaAplicatii comandaListaAplicatii;
    private Server.ComenziCMD.ComenziListe.ComandaListaProcese comandaListaProcese;
    private ComandaHistory comandaHistory;

    public ComenziLista(){

    }

    public List<String> getComandaListaAplicatii(String user){
        comandaListaAplicatii=new ComandaListaAplicatii();
        comandaListaAplicatii.setUser(user);
        comandaListaAplicatii.exec();
       return comandaListaAplicatii.getLista();
    }

    public String getComandaListaAplicatiiEroare(){
        return comandaListaAplicatii.getEroare();
    }


    public List<String> getComandaListaProcese(String user){
        comandaListaProcese=new ComandaListaProcese();
        comandaListaProcese.setUser(user);
        comandaListaProcese.exec();
        return comandaListaProcese.getLista();
    }

    public String getComandaListaProceseEroare(){
        return comandaListaProcese.getListaErori();
    }

    public List<String> getComandaHistory(String user){
        comandaHistory=new ComandaHistory();
        comandaHistory.setUser(user);
        comandaHistory.exec();
        return comandaHistory.getLista();
    }

    public String getComandaHistoryEroare(){
        return comandaHistory.getListaErori();
    }
}
