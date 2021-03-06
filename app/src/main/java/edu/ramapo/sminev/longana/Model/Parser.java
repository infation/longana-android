package edu.ramapo.sminev.longana.Model;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sminev on 11/5/17.
 */

public class Parser {


   /* public Parser(){
        this.tournament = tournament;
    }*/
    /* *********************************************************************
Function Name: loadBoard
Purpose: To load the contents of the board in the file to the object
Parameters: board -> the pointer to the board where everything will be loaded
            infile -> ifstream passed by reference of the opened file
Return Value: none
Assistaince Received: none;
********************************************************************* */
    public void loadBoard(Board board, BufferedReader br) throws IOException {
        //Initialize a variable which will be read in
        String hand = br.readLine();
        //Variable to save the index of a pip in the tile object
        int pipIndex = 0;
        int pips[] = new int[2];

        //Exhaust the unrelated stuff in the buffer
        for(int i = 0; i < hand.length(); i ++){
            //Check if the chars are numbers
            if(hand.charAt(i)!=' '&& hand.charAt(i)!='-'&& hand.charAt(i)!='L'&& hand.charAt(i)!='R'){
                pips[pipIndex] = hand.charAt(i)-'0';
                if(pipIndex == 1){
                    board.addTileToRight(new Tile(pips[0], pips[1]));
                    pipIndex = 0;
                    continue;
                }
                pipIndex++;
            }
        }
       board.printboard();
    }

    /* *********************************************************************
    Function Name: loadDeck
    Purpose: To load the contents of the deck in the file to the object
    Parameters: deck -> the pointer to the deck where everything will be loaded
                infile -> ifstream passed by reference of the opened file
    Return Value: none
    Assistaince Received: none;
    ********************************************************************* */
    void loadDeck(Deck deck, BufferedReader br) throws IOException {
        //Initialize a variable which will be read in
        //Initialize a variable which will be read in
        String hand = br.readLine();
        //Variable to save the index of a pip in the tile object
        int pipIndex = 0;
        int pips[] = new int[2];

        //Exhaust the unrelated stuff in the buffer
        for(int i = 0; i < hand.length(); i ++){
            //Check if the chars are numbers
            if(hand.charAt(i)!=' '&& hand.charAt(i)!='-'){
                pips[pipIndex] = hand.charAt(i)-'0';
                if(pipIndex == 1){
                    deck.addTile(new Tile(pips[0], pips[1]));
                    pipIndex = 0;
                    continue;
                }
                pipIndex++;
            }
        }
        deck.printDeck();
    }

    /* *********************************************************************
    Function Name: loadPlayer
    Purpose: To load the contents of the player in the file to the object
    Parameters: player -> the pointer to the player where everything will be loaded
                infile -> ifstream passed by reference of the opened file
    Return Value: none
    Assistaince Received: none;
    ********************************************************************* */
   public void loadPlayer(Player player, BufferedReader br) throws IOException {
        //Initialize a variable which will be read in
        String hand = br.readLine();
        hand = hand.substring(hand.indexOf(':')+2);
        //Variable to save the index of a pip in the tile object
        int pipIndex = 0;
        int pips[] = new int[2];

        //Exhaust the unrelated stuff in the buffer
        for(int i = 0; i < hand.length(); i ++){
            //Check if the chars are numbers
            if(hand.charAt(i)!=' '&& hand.charAt(i)!='-'){
                pips[pipIndex] = hand.charAt(i)-'0';
                if(pipIndex == 1){
                    player.addToHand(new Tile(pips[0], pips[1]));
                    pipIndex = 0;
                    continue;
                }
                pipIndex++;
            }
        }
        player.getHand().printHand();
        String score = br.readLine();
        player.setTournamentScore(Integer.parseInt(score.substring(score.indexOf(':')+2)));
        System.out.println(player.getTournamentScore());
        //Get the score of the player
        //String score = br.readLine();
   }

    public void loadFile(Tournament tournament, String whichFile) {
        tournament.getRound().setDeck(new Deck(false));
        //Finding the sdcard path on the tablet
        File sdcard = Environment.getExternalStorageDirectory().getAbsoluteFile();
        System.out.println(sdcard);
        //Get the text file, depending on whichFile the user chose.
        File textFile = new File(sdcard, whichFile);
        //Initialize string builder
        StringBuilder boardState = new StringBuilder();
        boolean isPassed = false;
        //Try if the text file will be opened
        try {
            //If yes initialize buffer reader and a new line which will get the buffers next line
            BufferedReader br = new BufferedReader(new FileReader(textFile));
            String line;

            try {
                //While the end of the file read next line
                while ((line = br.readLine()) != null) {
                    if (!line.equals("")){
                        switch (line.charAt(0)) {
                            case 'T':
                                String score = line.substring(line.indexOf(':') + 2);
                                tournament.setMaxTourScore(Integer.parseInt(score));
                                break;
                            case 'R':
                                String roundNum = line.substring(line.indexOf(':') + 2);
                                tournament.setRoundNum(Integer.parseInt(roundNum));
                                tournament.getRound().setEngine(7 - (Integer.parseInt(roundNum)%7));
                                System.out.println(tournament.getRound().getEngine());
                                break;
                            case 'C':
                                loadPlayer(tournament.getRound().getPlayers()[1], br);
                                break;
                            case 'H':
                                loadPlayer(tournament.getRound().getPlayers()[0], br);
                                break;
                            //Layout
                            case 'L':
                                loadBoard(tournament.getRound().getBoard(), br);
                                break;
                            //Boneyard/Deck
                            case 'B':
                                loadDeck(tournament.getRound().getDeck(), br);
                                break;
                            //Previous player Passed
                            case 'P':
                                String passed = line.substring(line.indexOf(':')+2);
                                if(passed.equals("No"))
                                    isPassed = false;
                                else if(passed.equals("Yes"))
                                    isPassed = true;
                                System.out.println(isPassed);
                                break;
                            case 'N':
                                String turn = line.substring(line.indexOf(':')+2);
                                if(turn.equals("Computer")) {
                                    tournament.getRound().setTurn(1);
                                    tournament.getRound().getPlayers()[0].setPassed(isPassed);
                                }
                                else{
                                    tournament.getRound().setTurn(0);
                                    tournament.getRound().getPlayers()[1].setPassed(isPassed);
                                }
                                System.out.println(tournament.getRound().getTurn());
                                break;
                        }
                    }
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(Player player, BufferedWriter bw) throws IOException {
       bw.write("   Hand:");
       for(int i = 0; i < player.getHand().size(); i++){
           bw.write(player.getHand().getTileAt(i).toString());
       }
       bw.write("\n   Score: "+player.getTournamentScore()+"\n\n");
    }

    public void saveBoard(Board board, BufferedWriter bw) throws IOException {
        bw.write("  L");
        for(int i = 0; i < board.size(); i++){
            bw.write(board.getTileAt(i).toString());
        }
        bw.write(" R\n\n");
   }

   public void saveDeck(Deck deck, BufferedWriter bw) throws IOException {
        for(int i = 0; i < deck.size(); i++){
            bw.write(deck.getTileAt(i).toString());
        }
        bw.write("\n\n");
    }

    public void saveFile(Tournament tournament) throws IOException {
        String fileName="SGame.txt";
        String filePath=Environment.getExternalStorageDirectory().toString();
        File file = new File(filePath, fileName);
        if(file.exists()) file.delete();
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("Tournament Score: ");
            bw.write(tournament.getMaxTourScore()+"\n\n");
            bw.write("Round Num.: ");
            bw.write(tournament.getRoundNum()+"\n\n");
            bw.write("Computer:\n");
            savePlayer(tournament.getRound().getPlayers()[1], bw);
            bw.write("Human:\n");
            savePlayer(tournament.getRound().getPlayers()[0], bw);
            bw.write("Layout: \n");
            saveBoard(tournament.getRound().getBoard(), bw);
            bw.write("Boneyard: \n");
            saveDeck(tournament.getRound().getDeck(), bw);
            boolean isPassed;
            String turn;
            if(tournament.getRound().getBoard().size()== 0){
                bw.write("Previous Player Passed: \n");
                bw.write("Next Player: \n");
            }
            else {
                if (tournament.getRound().getTurn() == 0) {
                    isPassed = tournament.getRound().getPlayers()[1].isPassed();
                    turn = "Human";
                } else {
                    isPassed = tournament.getRound().getPlayers()[0].isPassed();
                    turn = "Computer";
                }
                if (isPassed) {
                    bw.write("Previous Player Passed: " + "Yes\n\n");
                } else {
                    bw.write("Previous Player Passed: " + "No\n\n");
                }
                bw.write("Next Player: " + turn);
            }
            bw.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
