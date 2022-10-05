package GUI;

import Sudoku.SudokuSolver;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tabla {

    private static void createAndShowGUI() {
        final JPanel grid = new JPanel(new BorderLayout());
        JTable jTable=new JTable(9 ,9);
        JButton jButton=new JButton("Rezolva");
        grid.add(jTable, BorderLayout.CENTER);
        grid.add(jButton);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] sudoku=new int[9][9];
                for (int i = 0; i < jTable.getRowCount(); i++) {
                    for (int j = 0; j < jTable.getColumnCount(); j++) {
                        String value=jTable.getValueAt(i,j).toString();
                        if(value.trim().length() != 0) {
                            sudoku[i][j] = Integer.parseInt(value);
                        }
                        else {sudoku[i][j]=0;}
                    }
                }
                SudokuSolver sudokuSolver=new SudokuSolver();
                sudokuSolver.rezolvaSudoku(sudoku,9);


            }
        });

        final JPanel centeredGrid = new JPanel(new GridBagLayout());
        centeredGrid.add(grid);

        final JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(centeredGrid);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(Tabla::createAndShowGUI);
    }
}