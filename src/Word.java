import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class Word {

    //properties
    int number;                     //cell number of the first letter
    String orientation;                //0 for across., 1 for down
    int wordLength;                 //length of the answer word
    String[] wordSoFar;             //array of found letters of the word
    ArrayList<String> candidates;   //list of potential answers
    int[] startingPoint;            //coordinates of the first letter, x = row = startingPoint[0],
                                                                    // y = col = startingPoint[1]
    String name;

    String clue;

    //constructor
    public Word(int number, String orientation, int wordLength, int array[])
    {
        this.number = number;
        this.orientation = orientation;
        this.wordLength = wordLength;
        startingPoint = new int[2];
        startingPoint[0] = array[0];                      //initialize stratingPoint array to length 2
        startingPoint[1] = array[1];
        wordSoFar = new String[wordLength];         //initialize wordSoFar array to length = wordLength
        candidates = new ArrayList<>();
        name = number + orientation;
    }

    //copy constructor
    public Word( Word newWord)
    {
        this.number = newWord.getNumber();
        this.orientation = newWord.getOrientation();
        this.wordLength = newWord.getWordLength();
        this.startingPoint = new int[2];
        this.startingPoint[0] = newWord.getStartingPoint()[0];
        this.startingPoint[1] = newWord.getStartingPoint()[1];
        this.wordSoFar = new String[wordLength];
        for(int i = 0; i < wordSoFar.length; i++)
        {
            if(newWord.getWordSoFar()[i] != null)
                wordSoFar[i] = newWord.getWordSoFar()[i];
        }
        this.candidates = new ArrayList<>();
        for(int i = 0; i < newWord.getCandidates().size(); i++)
        {
                this.candidates.add(newWord.getCandidates().get(i));
        }
        this.name = newWord.getName();

    }

    //methods


    public void removeCandidates()
    {
        while(candidates.size()!= 0)
        {
            candidates.remove(0);
        }
    }

    public int getLength()
    {
        return wordLength;
    }

    public void updateWordSoFar(String letter, int index)
    {
        wordSoFar[index] = letter;
    }

    public String getName()
    {
        return name;
    }

    public int getNumber()
    {
        return number;
    }

    public String getOrientation()
    {
        return orientation;
    }

    public int getWordLength()
    {
        return wordLength;
    }

    public int[] getStartingPoint()
    {
        return startingPoint;
    }

    public ArrayList<String> getCandidates()
    {
        return candidates;
    }

    public String[] getWordSoFar()
    {
        return wordSoFar;
    }

    public void googleSearch()  {


            ArrayList<String> words = getWords(getClue());

            ArrayList<String> localC = new ArrayList<>();

            String query = "http://www.google.com/search?q=";

            for (int i = 0; i < words.size(); i++) {
                if (i != (words.size() - 1)) {
                    query = query + words.get(i).replaceAll("[-+.^:,']","") + "+";
                } else {
                    query = query + words.get(i).replaceAll("[-+.^:,']","");
                }
            }

            System.out.println(query);

            try {

                Document doc = Jsoup.connect(query).userAgent("Chrome").get();


                Elements elems = doc.getElementsByClass("st" );



                for (int i = 0; i < elems.size(); i++) {

                   // System.out.println(elems.get(i).text());

                   localC.addAll(getWords(elems.get(i).text()));

                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            candidates.addAll(localC);

            LinkedHashSet<String> localCset = new LinkedHashSet<>(candidates);

            candidates.clear();

            candidates.addAll(localCset);


            candidates.replaceAll(String::toUpperCase);

            removeDuplicates();


    }

    public void  trackerSearch()
    {
        ArrayList<String> words = getWords(getClue());

        ArrayList<String> localC = new ArrayList<>();



            String query = "http://crosswordtracker.com/clue/";

            for (int i = 0; i < words.size(); i++)
            {
                if (i != (words.size() - 1))
                {
                    query = query + words.get(i).toLowerCase() + "-";
                }
                else
                {
                    query = query + words.get(i).toLowerCase() + "/";
                }
            }

            System.out.println(query);

            try {

                Document doc = Jsoup.parse(new URL(query), 10000);


                Elements elems = doc.getElementsByClass("answer");

                for(int i = 0; i < elems.size(); i++){
                    localC.add(elems.get(i).text());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        candidates.addAll(localC);

        LinkedHashSet<String> localCset = new LinkedHashSet<>(candidates);

        candidates.clear();

        candidates.addAll(localCset);


        candidates.replaceAll(String::toUpperCase);

        removeDuplicates();

    }




    public void wikiSearch() {

        ArrayList<String> words = getWords(getClue());

        ArrayList<String> localC = new ArrayList<>();

            String query = "https://en.wikipedia.org/w/index.php?search=";

            for (int i = 0; i < words.size(); i++) {
                if (i != (words.size() - 1)) {
                    query = query + words.get(i).replaceAll("[-+.^:,']","") + "+";
                } else {
                    query = query + words.get(i).replaceAll("[-+.^:,']","");
                }
            }

            System.out.println(query);

            try {

                Document doc = Jsoup.parse(new URL(query), 10000);


                Elements elems = doc.getElementsByClass("searchresult");

                for (int i = 0; i < elems.size(); i++) {

                    localC.addAll(getWords(elems.get(i).text()));

                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        candidates.addAll(localC);

        LinkedHashSet<String> localCset = new LinkedHashSet<>(candidates);

        candidates.clear();

        candidates.addAll(localCset);


        candidates.replaceAll(String::toUpperCase);

        removeDuplicates();

    }




    public void setClue(String clue)
    {
        this.clue = clue;
    }

    public String getClue()
    {
        return clue;
    }

    public void removeDuplicates()
    {
        LinkedHashSet<String> localCset = new LinkedHashSet<>(candidates);

        candidates.clear();

        candidates.addAll(localCset);

    }




    public void filterByLetters()
    {
        boolean removed;
        boolean ended;

        int counter;

        for(int i = 0; i < candidates.size(); i++)
        {
            removed = false;
            ended = false;
            counter = 0;

            while(!(removed || ended))
            {
                if(wordSoFar[counter]!= null)
                {
                    if(candidates.get(i).charAt(counter) != wordSoFar[counter].charAt(0))
                    {
                        candidates.remove(i);
                        i--;
                        removed = true;
                    }

                }
                counter++;

                if(counter >= wordSoFar.length)
                {
                    ended = true;
                }

            }
        }

    }

    public void addCandidates(ArrayList<String> newCand)
    {
        this.candidates.addAll(newCand);
    }

    public void showWordSoFar()
    {
        System.out.println(Arrays.deepToString(wordSoFar));
    }


    public void filterBySize()
    {

        for(int i = 0; i < candidates.size(); i++)
                if(candidates.get(i).length() != wordLength) {
                    candidates.remove(i);
                    i--;
                }

    }

    public ArrayList<String> getWords(String text) {
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


}
