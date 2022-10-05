package Sudoku;

public class SudokuVerifier {

    //de verificat din nou
    //copiat de pe stack nu mai conteaza acum poate opt mai incolo
    private boolean verificareLinie(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = j + 1; k < 9; k++) {
                    if (sudoku[i][j] == sudoku[i][k]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean verificareColoana(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = j + 1; k < j; k++) {
                    if (sudoku[i][j] == sudoku[i][k]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean verificare3x3(int[][] sudoku) {
       for(int i=0;i<9;i+=3){
           for(int j=0;j<9;j+=3){
               for(int k=0;k<8;k++){
                   for(int kk=k+1;kk<9;kk++){
                       if(sudoku[i+k%3][j+k/3]==sudoku[i+kk%3][j+kk/3]){
                           return false;
                       }
                   }
               }
           }
       }
    return true;}

    public boolean verificareSudoku(int[][] sudoku){
        return verificare3x3(sudoku)&&verificareColoana(sudoku)&&verificareLinie(sudoku);
    }
}
