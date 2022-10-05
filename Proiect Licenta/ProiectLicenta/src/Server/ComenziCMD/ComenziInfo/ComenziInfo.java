package Server.ComenziCMD.ComenziInfo;

import java.awt.image.BufferedImage;

public class ComenziInfo {

    ComandaIPExtern comandaIPExtern;
    ComandaLocatie comandaLocatie;
    ComandaNumeUser comandaNumeUser;
    ComandaScreenShot comandaScreenShot;

    ComandaSession comandaSession;


    public ComenziInfo(){

    }


    public String getComandaIPExtern(String user){

        comandaIPExtern=new ComandaIPExtern();
        comandaIPExtern.setUser(user);
        comandaIPExtern.exec();
        return comandaIPExtern.getLista();
    }

    public String getComandaIPExternEroare(){
        return comandaIPExtern.getEroare();
    }
    public String getComandaLocatie(String user){
        comandaLocatie=new ComandaLocatie();
        comandaLocatie.setUser(user);
        comandaLocatie.exec();
        return comandaLocatie.getLista();
    }

    public String getComandaLocatieEroare(){
        return comandaLocatie.getEroare();
    }

    public String getComandaNumeUser(){
        comandaNumeUser=new ComandaNumeUser();
        comandaNumeUser.exec();
        return comandaNumeUser.getNume();
    }

    public String getComandaSession(String user){
        comandaSession=new ComandaSession();
        comandaSession.setUser(user);
        comandaSession.exec();
        if(!comandaSession.getEroare().isEmpty()){
            return comandaSession.getEroare();
        }
        return comandaSession.getLista();
    }


    public BufferedImage getComandaScreenShot(String user,String calc){
        comandaScreenShot=new ComandaScreenShot();
        comandaScreenShot.setUser(user);
        comandaScreenShot.setNume(calc);
        comandaScreenShot.exec();
        return comandaScreenShot.getBufferedImage();
    }

    public String getComandaScreenShotEroare(){
        return comandaScreenShot.getEroare();
    }
}
