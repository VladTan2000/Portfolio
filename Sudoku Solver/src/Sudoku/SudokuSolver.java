package Sudoku;

public class SudokuSolver {

    private boolean numarSigurRand(int[][] sudoku,int rand,int numar){
        for(int i=0;i<sudoku.length;i++){
            if(sudoku[rand][i]==numar){
                return false;
            }
        }

   return true; }

    private boolean numarSigurColoana(int[][] sudoku,int coloana,int numar){
        for(int i=0;i<sudoku.length;i++){
            if(sudoku[i][coloana]==numar){
                return false;
            }
        }
    return true;}

    private boolean numarSigurPatrat(int[][] sudoku,int coloana,int rand,int numar){
        int sqrt=(int)Math.sqrt(sudoku.length);
        int patratRandStart=rand-rand%sqrt;
        int patratColoanaStart=coloana-coloana%sqrt;
        for(int i=patratRandStart;i<patratRandStart+sqrt;i++){
            for(int j=patratColoanaStart;j<patratColoanaStart+sqrt;j++){
                if(sudoku[i][j]==numar){
                    return false;
                }
            }
        }
    return true;}

    public boolean esteRezolvabil(int[][] sudoku,int rand,int coloana,int numar){
        return numarSigurRand(sudoku,rand,numar)&&numarSigurColoana(sudoku,coloana,numar)&&numarSigurPatrat(sudoku,coloana,rand,numar);
    }

    public boolean rezolvaSudoku(int[][] sudoku,int n){
        int rand=-1;
        int coloana=-1;
        boolean isEmpty=true;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(sudoku[i][j]==0){
                    rand=i;
                    coloana=j;
                    isEmpty=false;
                    break;
                }
            }
            if(!isEmpty){
                break;
            }
        }
        if(isEmpty){
            return true;
        }
        for(int i=1;i<=n;i++){
            if(esteRezolvabil(sudoku,rand,coloana,i))
            {
                sudoku[rand][coloana]=i;
                if(rezolvaSudoku(sudoku,n))
                {
                    return true;
                }
                else{
                    sudoku[rand][coloana]=0;
                }
            }
        }
        return false;
    }

}
