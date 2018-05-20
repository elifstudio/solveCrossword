

import java.awt.*;

import java.util.ArrayList;

import java.awt.event.*;
import javax.swing.*;

public class CrosswordGrid {

    //properties
    private JFrame frame;
    private JPanel panel;
    private ArrayList<GridCell> cellList;
    private ArrayList<AnswerCell> answerList;
    //private ArrayList<String> inputLetters;
    private JButton button;
    private JTextArea across;
    private JTextArea down;
    private ArrayList<String> hintsAcross;
    private ArrayList<String> hintsDown;
    private JLabel acc;
    private JLabel dwn;
    private JPanel answerPanel;
    private ArrayList<Integer> colors;
    private ArrayList<String> numbers;
    private ArrayList<String> clues;
    private ArrayList<String> answers;
    private String output = "";
    private String[][] array;
    private ArrayList<String> tempArray;

    private JTextArea log;
    private JScrollPane scroll;
    private JLabel logTitle;
    final String[] arr = {"Unit of a train or roller coaster", "Where many modern files are stored, with \"the\"","Apples that share a name with Japan's largest mountain","Hospital professionals, for short","Political commentator Pfeiffer"};

    //constructor
    public CrosswordGrid(ArrayList<Integer> colors, ArrayList<String> numbers, ArrayList<String> clues, ArrayList<String> answers) {
        //makeGrid();
        this.colors = colors;
        this.numbers = numbers;
        this.clues = clues;
        this.answers = answers;
        //setup the window
        frame = new JFrame();
        frame.setBounds(300, 120, 600, 600);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);



    }

    public void update(ArrayList<Integer> colors, ArrayList<String> numbers, ArrayList<String> clues, ArrayList<String> answers)
    {
        this.colors = colors;
        this.numbers = numbers;
        this.clues = clues;
        this.answers = answers;
        display();
    }

    public void display() {

        makeGrid();
        displayHints();
        displayAnswerGrid();
        makeLog();
        // selectCrossWord();

        //frame.repaint();

    }

    public void makeLog() {
        log = new JTextArea();
        //log.setBounds(755, 50, 600, 150);
        //log.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        log.setLineWrap(true);
        log.setWrapStyleWord(true);


        Font font = new Font("Courier New",Font.PLAIN, 15);
        log.setFont(font);
        log.setEditable(true);
        scroll = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(755, 70, 600, 150);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        //DefaultCaret caret = (DefaultCaret)log.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);


        logTitle = new JLabel("Log:");
        logTitle.setBounds(755, 0, 70, 90);
        frame.add(logTitle);
        frame.add(scroll);
    }

    public void updateLog(String text) {
        log.setCaretPosition(log.getDocument().getLength());
        log.append(text);
        log.setCaretPosition(log.getDocument().getLength());
        log.append("\n");
        //log.append("\n");
        log.setCaretPosition(log.getDocument().getLength());
    }




    public void makeGrid() {

        //make 25 grid cells
        cellList = new ArrayList<GridCell>(26);
        tempArray = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            cellList.add(new GridCell());

        }
        for (int i = 0; i < 25; i++) {
            cellList.get(i).setIsBlack(colors.get(i));
            cellList.get(i).setCellNo(numbers.get(i));

        }
        for (int i = 0; i < 5;i++) {
            for (int j = 0; j < 5;j++) {
                tempArray.add(array[i][j]);
            }
        }
        //write answers
        for (int i = 0; i < 25; i++) {
            if(cellList.get(i).getIsBlack() == 1 && cellList.get(i).getLetter() != null && !(tempArray.get(i).equals("white"))) {
                cellList.get(i).setLetter(tempArray.get(i));
            }


        }

        //setup the grid
        panel = new JPanel();
        panel.setBounds(280, 180, 400, 400);
        panel.setLayout(new GridLayout(5,5));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setBackground(Color.WHITE);

        //add cells to the grid
        for (int i = 0; i < 25; i++) {
            panel.add(cellList.get(i));
            cellList.get(i).makeCell();
        }



        frame.add(panel);
    }

    public void displayHints() {
        across = new JTextArea();
        down = new JTextArea();

        across.setBounds(755, 280, 300,300);
        down.setBounds(1060,280,300,300);
        across.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        down.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        acc = new JLabel("Across");
        dwn = new JLabel("Down");

        acc.setBounds(755, 220, 70, 90);
        dwn.setBounds(1060,220,70,90);

        across.setLineWrap(true);
        across.setWrapStyleWord(true);
        down.setLineWrap(true);
        down.setWrapStyleWord(true);

        Font font = new Font("Arial",Font.PLAIN, 20);
        across.setFont(font);
        down.setFont(font);
        for (int i = 0 ; i < 5 ; i ++ ) {
            across.append(clues.get(i));
            across.append("\n");
            across.append("\n");
        }
        for (int i = 5 ; i < 10 ; i ++ ) {
            down.append(clues.get(i));
            down.append("\n");
            down.append("\n");
        }

        across.setEditable(false);
        down.setEditable(false);

        frame.add(acc);
        frame.add(dwn);
        frame.add(across);
        frame.add(down);

    }

    public void getVal (String value)
    {
        output = value;
    }

    public String getOutput()
    {
        return output;

    }



    public void sendArray(String[][] array) {
        this.array = array;
    }

    public void displayAnswerGrid() {
        //setup the grid
        answerPanel = new JPanel();
        answerPanel.setBounds(15, 380, 200, 200);
        answerPanel.setLayout(new GridLayout(5,5));
        answerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        //panel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));
        answerPanel.setBackground(Color.WHITE);

        answerList = new ArrayList<AnswerCell>();
        //add cells to the grid
        for (int i = 0; i < 25; i++) {
            answerList.add(new AnswerCell());
            answerList.get(i).setLetter(answers.get(i));
            answerList.get(i).setCellNo(numbers.get(i));
            answerList.get(i).setIsBlack(colors.get(i));

        }
        for (int i = 0; i < 25; i++) {
            answerPanel.add(answerList.get(i));
            answerList.get(i).makeCell();
        }

        frame.add(answerPanel);
        frame.setVisible(true);
    }

}
