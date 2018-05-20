import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.BreakIterator;
import java.util.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
//import java.net.HttpURLConnection;
import java.net.URL;

//import java.net.URISyntaxException;



public class Parser {

    private static int CWIDTH = 5;
    private static int CHEIGHT = 5;

    static String html;
    static String url = "https://www.nytimes.com/crosswords/game/mini";
    static  String file;

    static String matrix[][] = new String[5][5];

    static String answerTable[][] = new String[5][5];

    private static ArrayList<Word> words;

    private static ArrayList<Answer> answers;

    public static void start(String input){
        html = "";
        String file = "crosswords/" + input + ".html";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                html += sCurrentLine;
            }
        }

        catch (IOException e) {
            e.printStackTrace();

        }

    }




    public static ArrayList<String> getClues() {

        ArrayList<String> clues = new ArrayList<>();

        Document doc;


        try{

                doc = Jsoup.parse(html);


            Elements elems = doc.getElementsByClass("Clue-text--3lZl7");
            Elements elems2 = doc.getElementsByClass("Clue-label--2IdMY");
            for(int i = 0; i < elems.size(); i++){
                clues.add(elems2.get(i).text() + ". " + elems.get(i).text());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        return clues;

    }


    public static ArrayList<Integer> getColors(){

        ArrayList<Integer> colors = new ArrayList<>();

        Document doc;

        try{

                doc = Jsoup.parse(html);

            Elements elems = doc.select("g[data-group=\"cells\"]");

            elems = elems.get(0).getElementsByTag("rect");
            for (int i = 0; i < elems.size(); i++) {

                if (elems.get(i).className().contains("Cell-cell--1p4gH" ))
                {
                    colors.add(1);
                }
                else
                {
                    colors.add(0);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        return colors;

    }



    public static ArrayList<String> getNumbers(){

        ArrayList<String> numbers = new ArrayList<>();

        Document doc;


        try
        {

                doc = Jsoup.parse(html);

            Elements elems = doc.select("g[data-group=\"cells\"]");

            elems = elems.get(0).getElementsByTag("g");
            for (int i = 1; i < elems.size(); i++) {
                Element el = elems.get(i).attr("text-anchor","start");
                if(el.text().length() > 1)
                {
                    for(int j = 0; j < el.text().length(); j++)
                    {
                        if (Character.isDigit(el.text().charAt(j)))
                        {
                            numbers.add("" + el.text().charAt(j));
                        }

                    }
                }
                else if (el.text().length() == 1)
                {
                    if (Character.isDigit(el.text().charAt(0)))
                    {
                        numbers.add("" + el.text().charAt(0));
                    }
                    else
                    {
                        numbers.add("");
                    }
                }
                else
                {
                    numbers.add(""+ el.text());
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return numbers;

    }


    public static ArrayList<String> getAnswers() {

        ArrayList<String> numbers = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements elems = doc.select("g[data-group=\"cells\"]");

        elems = elems.get(0).getElementsByTag("g");
        // System.out.println("Before iteration");
        for (int i = 1; i < elems.size(); i++) {
            Element el = elems.get(i).attr("text-anchor","start");
            if(el.text().length() > 1)
            {
                for(int j = 0; j < el.text().length(); j++)
                {
                    if (Character.isLetter(el.text().charAt(j)))
                    {
                        numbers.add(String.valueOf(el.text().charAt(j)));
                    }

                }
            }
            else if (el.text().length() == 1)
            {
                if (Character.isLetter(el.text().charAt(0)))
                {
                    numbers.add(String.valueOf(el.text().charAt(0)));
                }
                else
                {
                    numbers.add("");
                }
            }
            else
            {
                numbers.add("");
            }
        }

            /*    } catch (IOException e) {
                    e.printStackTrace();

                } */
        return numbers;
    }

    public static ArrayList<String> getWords(String text) {
        ArrayList<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex));
            }
        }

        return words;
    }


    public static void putColors()
    {

        int count = 0;

        ArrayList<Integer> colors;
        colors = getColors();

        for (int i = 0; i < CWIDTH; i++)
        {
            for(int j = 0; j< CHEIGHT; j++)
            {
                if(colors.get(count) == 0)
                    matrix[i][j] = "black";
                else
                    matrix[i][j] = "white";

                count++;
            }
        }

    }

    public static void putNumbers()
    {

        int count = 0;

        ArrayList<String> numbers;
        numbers = getNumbers();

        for (int i = 0; i < CWIDTH; i++)
        {
            for(int j = 0; j< CHEIGHT; j++)
            {
                if(numbers.get(count).length()>0)
                    matrix[i][j] = numbers.get(count);

                count++;
            }
        }
    }

    public static void createWords(){
        words = new ArrayList<>();
        boolean enter = true;
        int number = 0;
        int[] StartingPoint = new int[2];
        String orientation = "a";
        int wordLength;
        //for row
        for(int i = 0; i < 5; i++) {
            wordLength = 5;
            for(int j = 0; j < 5; j++){


                if(!matrix[i][j].equals("black") && enter) {
                    number = Integer.valueOf(matrix[i][j]);
                    StartingPoint[0] = i;
                    StartingPoint[1] = j;
                    enter = false;
                }
                if(matrix[i][j].equals("black"))
                    wordLength--;

            }
            enter = true;
            Word word = new Word(number, orientation, wordLength, StartingPoint);
            words.add(word);

        }

        orientation = "d";
        //for column
        for(int i = 0; i < 5; i++) {
            wordLength = 5;
            for(int j = 0; j < 5; j++){

                if(!matrix[j][i].equals("black") && enter) {
                    number = Integer.valueOf(matrix[j][i]);
                    StartingPoint[0] = j;
                    StartingPoint[1] = i;
                    enter = false;

                }
                if(matrix[j][i].equals("black"))
                    wordLength--;


            }
            enter = true;
            Word word = new Word(number, orientation, wordLength, StartingPoint);
            words.add(word);
        }
    }

    public static void updateWords(){

        int[] StartingPoint;
        int wordLength;
        int k = 0;

            //for row
            for(int i = 0; i < 5; i++) {
                StartingPoint = words.get(k).getStartingPoint();
                wordLength = words.get(k).getWordLength();
                for(int j = StartingPoint[1]; j < wordLength + + StartingPoint[1]; j++){
                    if(!matrix[i][j].equals("black") && !matrix[i][j].equals("white") && !(matrix[i][j] == null) && !Character.isDigit(matrix[i][j].charAt(0)) ) {
                        words.get(k).updateWordSoFar(matrix[i][j], j - StartingPoint[1]);
                    }
                }

                k++;
            }

            //for column
            for(int i = 0; i < 5; i++) {
                StartingPoint = words.get(k).getStartingPoint();
                wordLength = words.get(k).getWordLength();
                for(int j = StartingPoint[0]; j < wordLength + StartingPoint[0]; j++){
                    if(!matrix[j][i].equals("black") && !matrix[j][i].equals("white") && !(matrix[j][i] == null)  && !Character.isDigit(matrix[j][i].charAt(0)) ) {
                        words.get(k).updateWordSoFar(matrix[j][i], j - StartingPoint[0]);
                        System.out.println("j is:" + j + ", value is" +  matrix[j][i]);
                    }

                }
                k++;

            }

    }

    public static void showWords()
    {
        for(int i = 0; i < words.size(); i++)
        {
            int number = words.get(i).getNumber();
            String orientation = words.get(i).getOrientation();
            int wordLength = words.get(i).getWordLength();
            int startingPointx = words.get(i).getStartingPoint()[0];                      //initialize stratingPoint array to length 2
            int startingPointy = words.get(i).getStartingPoint()[1];
            String clue = words.get(i).getClue();
            ArrayList<String> cans= words.get(i).getCandidates();
            //wordSoFar = new String[this.wordLength];         //initialize wordSoFar array to length = wordLength
            //candidates = new ArrayList<String>();

            System.out.println("number: " + number + ", orientation : " + orientation + ", word length: " + wordLength + ", stx: " + startingPointx + ", sty:" + startingPointy + ", clue: " + clue + ", length: " + words.get(i).getLength());
            for(int j = 0; j < cans.size(); j++)
            {
                System.out.println(cans.get(j));
            }
        }
    }

    public static void putClues(){
        ArrayList<String> clues = getClues();
        String orientation;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(j < 5){
                    orientation = "a";
                }
                else {
                    orientation = "d";
                }
                if(words.get(i).getName().equals(clues.get(j).charAt(0) + orientation)){
                    words.get(i).setClue(clues.get(j).substring(2));
                }
            }
        }

    }



    public static void initAnswerTable()
    {
        int count = 0;
        ArrayList<String> answers = getAnswers();

        for (int i = 0; i < CWIDTH; i++)
        {
            for(int j = 0; j< CHEIGHT; j++)
            {
                if(answers.get(count).length()>0)
                    answerTable[i][j] = answers.get(count);
                count++;
            }
        }

    }

    public static void createAnswers(){
        answers = new ArrayList<>();
        for(int i = 0; i < words.size(); i++){
            //initialize the variables of certain word
            StringBuilder key = new StringBuilder();
            String name = words.get(i).getName();
            int length = words.get(i).getWordLength();
            int[] strPnt = words.get(i).getStartingPoint();
            String orientation = words.get(i).getOrientation();
            //for across crossword
            if(orientation.equals("a")){
                int row = strPnt[0];
                int col = strPnt[1];
                boolean enter = false;
                for(int k = 0; k < 5; k++){
                    //if starting point matches, then enter is set to true to insert the letters till the length of word is zero
                    if((row == i && k == col) || enter){
                        if(length > 0){
                            key.append(answerTable[i][k]);
                        }
                        length--;
                        enter = true;
                    }
                }
                answers.add(new Answer(name, String.valueOf(key)));
            }
            //for down crossword
            else {
                int row = strPnt[0];
                int col = strPnt[1];
                boolean enter = false;
                for(int k = 0; k < 5; k++){
                    //if starting point matches, then enter is set to true to insert the letters till the length of word is zero
                    if((col == (i - 5)&& k == row) || enter){
                        if(length > 0){
                            key.append(answerTable[k][i-5]);
                        }
                        length--;
                        enter = true;
                    }
                }
                answers.add(new Answer(name, String.valueOf(key)));
            }
        }

    }


    public static void showAnswers()
    {
        for(int i = 0; i < answers.size(); i++)
        {
            System.out.println("name " + answers.get(i).getName() + ", key : " + answers.get(i).getKey());
        }
    }

    public static void setBase(){
        int counter = 0;
            for(int i = 0; i < words.size()  && counter < 2; i++){
                for(int j = 0; j < answers.size() && counter < 2; j++){
                    if(words.get(i).getName().equals(answers.get(j).getName())) {
                        if (words.get(i).getCandidates().contains(answers.get(j).getKey())){
                            String[] temp = new String[answers.get(j).getKey().length()];
                            for(int k = 0; k < answers.get(j).getKey().length(); k++){
                                temp[k] = answers.get(j).getKey().charAt(k) + "";
                            }

                            words.get(i).removeCandidates();

                            String orientation = words.get(i).getOrientation();
                            int[] strPoint = words.get(i).getStartingPoint();
                            int row = strPoint[0], col = strPoint[1];
                            for(int r = 0; r < temp.length; r++){
                                if(orientation.equals("a")) {
                                    matrix[row][col] = temp[r];
                                    col++;
                                }
                                if(orientation.equals("d")){
                                    matrix[row][col] = temp[r];
                                    row++;
                                }
                            }
                            counter++;

                        }
                    }
                }
            }

    }

    public static boolean updateMatrix( Word word, int index)
    {
        boolean changed = false;
        String orientation = word.getOrientation();
        int[] strPoint = word.getStartingPoint();
        int row = strPoint[0], col = strPoint[1];
        for(int r = 0; r < word.getWordLength(); r++){
            if(orientation.equals("a")) {
                if(matrix[row][col].equals("black") || matrix[row][col].equals("white") || (matrix[row][col] == null) || Character.isDigit(matrix[row][col].charAt(0))) {

                    matrix[row][col] = Character.toString(word.getCandidates().get(index).substring(r).charAt(0));

                    changed = true;
                }
                col++;

            }

            if(orientation.equals("d")){
                if(matrix[row][col].equals("black") || matrix[row][col].equals("white") || (matrix[row][col] == null) || Character.isDigit(matrix[row][col].charAt(0)))
                {
                matrix[row][col] = Character.toString(word.getCandidates().get(index).substring(r).charAt(0));

                changed = true;
                }
                row++;
            }
        }

        return changed;

    }

    public ArrayList<String[]> getFromDictionary(String[] wordSoFar) throws FileNotFoundException {
        ArrayList<String[]> resultList = new ArrayList<>();
        int count = 0;

        for (String aWordSoFar : wordSoFar)
            if (aWordSoFar != null)
                count++;

        Scanner scan = new Scanner(new File("newWord.txt"));
        while(scan.hasNext()){
            String line = scan.nextLine().toUpperCase();
            if(line.length() == wordSoFar.length) {
                int count2 = 0;
                String[] lineArray = new String[line.length()];
                for(int i = 0; i < line.length(); i++)
                    lineArray[i] = String.valueOf((line.charAt(i)));
                for(int i = 0; i < line.length(); i++) {
                    if(wordSoFar[i] != null && wordSoFar[i].equals(lineArray[i]))
                        count2++;
                }
                if(count == count2)
                    resultList.add(lineArray);

            }

        }

        return resultList;
    }


    public static void main (String[] args) throws Exception{



       /* ArrayList<String> colors;
        colors = getNumbers(true);
        for(int i = 0; i < colors.size(); i++)
        {
            System.out.println(colors.get(i));
        } */

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("1. for \"Today's, 8th March-2018\"\n" +
                "2. for \"7th March-2018\"\n3. for \"6th March-2018\"\n" +
                "4. for \"7th February-2018\"\n5. for \"2nd February-2018\"\n6. for \"3rd January-2018\"\n" +
                "7. for \"5th December-2017\"\n ");
        int n = reader.nextInt(); // Scans the next token of the input as an int.

        reader.close();

        String input ="M8";

        boolean today = true;


        if(n == 1)
        {
            input = "M8";
            today = true;
        }
        else if (n == 2)
        {
            input = "M7";
            today = false;
        }
        else if (n == 3)
        {
            input = "M6";
            today = false;
        }
        else if (n == 4)
        {
            input = "F7";
            today = false;
        }
        else if (n == 5)
        {
            input = "F2";
            today = false;
        }
        else if (n == 6)
        {
            input = "J3";
            today = false;
        }
        else if (n == 7)
        {
            input = "O30_17";
            today = false;
        }


        start(input);
        putColors();
        putNumbers();
        System.out.println(Arrays.deepToString(matrix));
        createWords();


        putClues();
        initAnswerTable();
        createAnswers();

        System.out.println(Arrays.deepToString(answerTable));
        showAnswers();

        for(int i = 0; i < words.size(); i++)
        {
            words.get(i).googleSearch();
            words.get(i).wikiSearch();
            words.get(i).trackerSearch();
            words.get(i).filterBySize();
            words.get(i).filterByLetters();

        }



        System.out.println(Arrays.deepToString(matrix));
        showWords();

        setBase();
        System.out.println(Arrays.deepToString(matrix));

        updateWords();


        for(int i = 0; i < words.size(); i++)
        {
            words.get(i).showWordSoFar();
        }

        for(int i = 0; i < words.size(); i++)
        {
            words.get(i).filterByLetters();
        }

        System.out.println("Updated by letters");
        showWords();

        int looper = 0;

        while( looper < 10)
        {
            int smallest = 100;
            int index = 0;

            for(int i = 0; i < words.size(); i++ )
            {
                if(words.get(i).getCandidates().size() > 0)
                {
                    if(words.get(i).getCandidates().size() <= smallest)
                    {
                        smallest = words.get(i).getCandidates().size();
                        index = i;
                    }
                }

            }


            updateMatrix(words.get(index),0);
            updateWords();

            System.out.println(Arrays.deepToString(matrix));

            words.get(index).removeCandidates();

            showWords();

            for(int i = 0; i < words.size(); i++)
            {
                words.get(i).showWordSoFar();
            }

            for(int i = 0; i < words.size(); i++)
            {
                words.get(i).filterByLetters();
            }

            showWords();

            looper++;
        }


        ArrayList<Integer> colors;
        colors = getColors();

        ArrayList<String> numbers;
        numbers = getNumbers();

        ArrayList<String> clues;
        clues = getClues();

        ArrayList<String> answers;
        answers = getAnswers();


        CrosswordGrid cd = new CrosswordGrid(colors, numbers, clues, answers);
        cd.sendArray(matrix);
        cd.display();





    }


}

