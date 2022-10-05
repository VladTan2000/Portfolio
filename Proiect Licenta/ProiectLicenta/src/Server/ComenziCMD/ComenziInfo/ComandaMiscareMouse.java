package Server.ComenziCMD.ComenziInfo;

import Server.ComenziCMD.IComanda.Comanda;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComandaMiscareMouse implements Comanda {

    private final Object object=new Object();
    AtomicBoolean isMiscat=new AtomicBoolean();
    long s=1000L;
    @Override
    public void exec() {
        Runnable runnable = () -> {
            while (true) {
                synchronized (object) {
                    long sleep=s;
                    Point location = MouseInfo.getPointerInfo().getLocation();
                    double x = location.getX();
                    double y = location.getY();
                    try {
                        object.wait(s);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    location = MouseInfo.getPointerInfo().getLocation();
                    double x2 = location.getX();
                    double y2 = location.getY();
                    isMiscat.set(x != x2 && y != y2);

                    }
                }
            };


            Thread thread = new Thread(runnable);
            thread.start();
        }

    public String getIsMiscat(){

        return String.valueOf(isMiscat.get());
    }

    public void setTimp(long timp){
        synchronized (object) {
           this.s=timp;
            object.notifyAll();
        }
    }

}
