package Sudoku;

import java.util.*;

public class SudokuGenerator {
    private int[][] sudoku;
    public int[][] sudokuGenerator(){
        sudoku=new int[9][9];
        Random random=new Random();
        for(int i=0;i<9;i++){
            for(int j=0;j<8;j++){
                sudoku[i][j]=random.nextInt(9)+1;
            }
        }
        return sudoku;
    }

    public int[][] sudokuGeneratorJoc(){
        sudoku=new int[9][9];
        Random random=new Random();
        for(int i=0;i<9;i++) {
            for (int j = 0; j < 8; j += random.nextInt(9) + 1) {
                sudoku[i][j]=random.nextInt(9)+1;
            }
        }

    return sudoku;}

}
