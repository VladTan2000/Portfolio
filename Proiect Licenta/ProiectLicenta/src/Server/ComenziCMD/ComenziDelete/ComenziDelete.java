package Server.ComenziCMD.ComenziDelete;

public class ComenziDelete {
    ComandaDeleteAplication comandaDeleteAplication;
    ComandaStopProcess comandaStopProcess;
    public ComenziDelete(){

    }
    public void ComandaDeleteAplication(String application,String user){
        comandaDeleteAplication=new ComandaDeleteAplication();
        comandaDeleteAplication.setUser(user);
        comandaDeleteAplication.execString(application);
    }

    public String ComandaDeleteAplicationError(){
        return comandaDeleteAplication.getEroare();
    }
    public void ComandaStopProcess(String process,String user){
        comandaStopProcess=new ComandaStopProcess();
        comandaStopProcess.setUser(user);
        comandaStopProcess.execString(process);
    }
    public String ComandaStopProcessError(){
        return comandaStopProcess.getErore();
    }



}
