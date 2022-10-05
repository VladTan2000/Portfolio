import Sudoku.SudokuGenerator;
import Sudoku.SudokuSolver;
import Sudoku.SudokuVerifier;

import java.util.Arrays;


public class Test {
    public static void main(String[] args) {
        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        SudokuVerifier sudokuVerifier = new SudokuVerifier();
        int[][] sudoku = sudokuGenerator.sudokuGenerator();
        int[][] sudokuIncomplet=sudokuGenerator.sudokuGeneratorJoc();
        int[][] sudokuCorect={
                {8,1,2,7,5,3,6,4,9},
                {9,4,3,6,8,2,1,7,5},
                {6,7,5,4,9,1,2,8,3},
                {1,5,4,2,3,7,8,9,6},
                {3,6,9,8,4,5,7,2,1},
                {2,8,7,1,6,9,5,3,4},
                {5,2,1,9,7,4,3,6,8},
                {4,3,8,5,2,6,9,1,7},
                {7,9,6,3,1,8,4,5,2}
        };
        for (int[] elemente : sudoku) {
            //System.out.println(Arrays.toString(Arrays.stream(elemente).toArray()));
        }
        for (int[] elemente : sudokuIncomplet) {
            //System.out.println(Arrays.toString(Arrays.stream(elemente).toArray()));
        }
        //System.out.println(sudokuVerifier.verificareSudoku(sudoku));
        //System.out.println(sudokuVerifier.verificareSudoku(sudokuCorect));

        int[][] board = new int[][] {
                { 3, 0, 6, 5, 0, 8, 4, 0, 0 },
                { 5, 2, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 8, 7, 0, 0, 0, 0, 3, 1 },
                { 0, 0, 3, 0, 1, 0, 0, 8, 0 },
                { 9, 0, 0, 8, 6, 3, 0, 0, 5 },
                { 0, 5, 0, 0, 9, 0, 6, 0, 0 },
                { 1, 3, 0, 0, 0, 0, 2, 5, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 7, 4 },
                { 0, 0, 5, 2, 0, 6, 3, 0, 0 }
        };
        SudokuSolver sudokuSolver=new SudokuSolver();
        System.out.println(sudokuSolver.rezolvaSudoku(board,9));
    }
}
