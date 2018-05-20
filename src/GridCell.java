

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class GridCell extends JPanel {

    //properties
    JTextField field;
    JLabel superscript;
    String letter;
    int isBlack;

    String cellNo;

    public GridCell() {
        isBlack = 1;

        cellNo = "";
        letter = "";

        makeCell();
    }

    //methods
    public void setIsBlack(int val) {
        isBlack = val;

    }
    public int getIsBlack() {
        return isBlack;

    }
    public void setLetter(String str) {
        letter = str;
    }

    public void setCellNo(String val) {
        cellNo = val;
    }
    public String getLetter() {
        return letter;
    }
    public void makeCell() {
        setPreferredSize(new Dimension(50, 50));
        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);

        if (isBlack == 0) {
            setBackground(Color.BLACK);
            superscript.setVisible(false);
            field.setVisible(false);
        }
        else {
            // put a superscript if cell has a number

            String strCellNo = cellNo;
            superscript = new JLabel(strCellNo);
            // superscript.setSize(new Dimension(2,2));
            superscript.setBackground(Color.WHITE);
            superscript.setOpaque(true);
            add(superscript, BorderLayout.NORTH);


            Font font = new Font("SansSerif", Font.BOLD, 20);
            field = new JTextField();

            //field.setText("A");r
            field.setBorder(BorderFactory.createEmptyBorder());
            field.setFont(font);
            field.setText(letter);
            field.setHorizontalAlignment(JTextField.CENTER);

            field.addKeyListener(new KeyAdapter() {

                public void keyTyped(KeyEvent e) {
                    if (field.getText().length() >= 1 ) {//limit text to 1 characters
                        e.consume();
                        letter = field.getText();
                        printLetter();
                    }
                }
            });

            add(field, BorderLayout.CENTER);

            //setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }
    public void printLetter() {
        System.out.println(letter);
    }
}