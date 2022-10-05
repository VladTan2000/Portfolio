import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args){
        System.out.println("Introduceti numarul");
        Scanner scanner= new Scanner(System.in);
        int i=Integer.parseInt(scanner.nextLine());
        cautare(i,2);

    }

    public static void cautare(int n,int nr){
        List<Integer> numere=new ArrayList<>();
        if(nr>n/2){
            System.out.println("S-au afisat toate rezultatele");
            return;
        }
        int adauga=n/nr;
        if((adauga*2+1)==n){
            System.out.println("Cu 2 numere: ");
            System.out.println("[ "+adauga + " ,"+ (adauga + 1) + " ]");
            cautare(n,++nr);
            return;
        }
        numere.add(adauga);
        int j=1;
        for(int i=0;i<nr/2;i++){
            numere.add(adauga+j);
            j++;
        }
        j=1;
        for(int i=0;i<nr/2;i++){
            numere.add(adauga-j);
            j++;
        }
        Collections.sort(numere);
        if((numere.stream().mapToInt(Integer::intValue).sum())==n) {
            System.out.println("Cu "+nr +  " numere: ");
             System.out.println(numere);
        }
        numere.clear();
        nr=nr+2;
        cautare(n,nr);
    }
}
