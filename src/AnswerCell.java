import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class AnswerCell extends JPanel {

    //properties
    JTextField field;
    JLabel superscript;
    String letter;
    int isBlack;

    String cellNo;

    public AnswerCell() {
        isBlack = 1;
        superscript = new JLabel("");
        field =  new JTextField("");
        cellNo = "";
        letter = null;

        makeCell();
    }

    //methods
    public void setIsBlack(int val) {
        isBlack = val;
    }

    public void setCellNo(String val) {
        cellNo = val;
    }
    public void setLetter(String str) {
        letter = str;
    }
    public String getLetter() {
        return letter;
    }
    public void makeCell() {
        setPreferredSize(new Dimension(35, 35));
        setLayout(new BorderLayout());


        if (isBlack == 0) {
            setBackground(Color.BLACK);
            //letter = "black";
            superscript.setVisible(false);
            field.setVisible(false);
        }
        else {
            // put a superscript if cell has a number

            String strCellNo = cellNo + "";
            superscript = new JLabel(strCellNo);
            superscript.setSize(new Dimension(1,1));
            superscript.setBackground(Color.WHITE);
            superscript.setOpaque(true);
            add(superscript, BorderLayout.NORTH);


            Font font = new Font("SansSerif", Font.PLAIN, 17);
            field = new JTextField();

            field.setText(letter);
            field.setBorder(BorderFactory.createEmptyBorder());
            field.setFont(font);
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setEditable(false);
            field.setBackground(Color.WHITE);

            add(field, BorderLayout.CENTER);


            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

}
