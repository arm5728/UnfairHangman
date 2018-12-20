/*  Student information for assignment:
 *
 *  On my honor, Adrian Melendez Relli, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  Name: Adrian Melendez Relli
 *  email address: adrianmelendezrelli@gmail.com
 *  UTEID: arm5728
 *  Section 5 digit ID: 51395
 *  Grader name: Ethan
 *  Number of slip days used on this assignment: 0
 */

// add imports as necessary

import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Manages the details of EvilHangman. This class keeps
 * tracks of the possible words from a dictionary during
 * rounds of hangman, based on guesses so far.
 *
 */
public class HangmanManager {

    // instance variables
	private ArrayList <String> dictionary;
	private ArrayList <String> activeWords;
	private HangmanDifficulty difficulty;
	private int guessesAllowed;
	private int numWrongGuesses;
	private int wordLength;
	private String pattern;
	private ArrayList <String> guessesMade;
	private int thisGuess;
	

    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     * @param debugOn true if we should print out debugging to System.out.
     */
    public HangmanManager(Set<String> words, boolean debugOn) {
    	//Check Preconditions
    	if (words == null || words.size() <= 0) {
    		throw new IllegalArgumentException("pre: words != null, words.size() > 0");
    	}
    	
    	//Build Dictionary ArrayList using iterator
    	dictionary = new ArrayList <String>();
    	Iterator<String> iterator = words.iterator();
    	while (iterator.hasNext()) {
    		dictionary.add(iterator.next());
    	}  
    }

    /**
     * Create a new HangmanManager from the provided set of words and phrases. 
     * Debugging is off.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     */
    public HangmanManager(Set<String> words) {
    	//Check Preconditions
    	if (words == null || words.size() <= 0) {
    		throw new IllegalArgumentException("pre: words != null, words.size() > 0");
    	}
    	
    	//Build Dictionary ArrayList using iterator
    	dictionary = new ArrayList <String>();
    	Iterator<String> iterator = words.iterator();
    	while (iterator.hasNext()) {
    		dictionary.add(iterator.next());
    	}
    }


    /**
     * Get the number of words in this HangmanManager of the given length.
     * pre: none
     * @param length The given length to check.
     * @return the number of words in the original Dictionary with the given length
     */
    public int numWords(int length) {
        int numOfLength = 0;
        
        //Check amt. of words in this dictionary that are == to length input
    	for (int word = 0; word < dictionary.size(); word++) {
        	if (dictionary.get(word).length() == length)
        		numOfLength++;
        }
    	return numOfLength;
    }


    /**
     * Get for a new round of Hangman. Think of a round as a complete game of Hangman.
     * @param wordLen the length of the word to pick this time. numWords(wordLen) > 0
     * @param numGuesses the number of wrong guesses before the player loses the round. numGuesses >= 1
     * @param diff The difficulty for this round.
     */
    public void prepForRound(int wordLen, int numGuesses, HangmanDifficulty diff) {
    	//Check Preconditions 
    	if (wordLen <= 0 || numGuesses < 1) {
    		throw new IllegalArgumentException("Invalid Parameters: Check wordLen and numGuesses input.");
    	}
    	
    	//Set and reset instance vars for new round. 
    	wordLength = wordLen;
    	guessesAllowed = numGuesses;
    	numWrongGuesses = 0;
    	guessesMade = new ArrayList<String>();
    	difficulty = diff;
    	thisGuess = 1;
    	
    	//Set/ Reset Active Words to words in Dictionary that are of length.
    	activeWords = new ArrayList<String>();
    	for (int word = 0; word < dictionary.size(); word++) {
    		//Check if of proper length
    		if (dictionary.get(word).length() == wordLength) {
    			activeWords.add(dictionary.get(word));
    		}
    	}
    	
    	//Make base pattern. Basically '-' times length
    	StringBuilder base = new StringBuilder();
		for (int build = 0; build < wordLen; build++) {
			base.append("-");
		}
		pattern = base.toString();
    }


    /**
     * The number of words still possible (live) based on the guesses so far. Guesses will eliminate possible words.
     * @return the number of words that are still possibilities based on the original dictionary and the guesses so far.
     */
    public int numWordsCurrent() {
        return activeWords.size();
    }


    /**
     * Get the number of wrong guesses the user has left in this round (game) of Hangman.
     * @return the number of wrong guesses the user has left in this round (game) of Hangman.
     */
    public int getGuessesLeft() {
        return (guessesAllowed - numWrongGuesses);
    }


    /**
     * Return a String that contains the letters the user has guessed so far during this round.
     * The String is in alphabetical order. The String is in the form [let1, let2, let3, ... letN].
     * For example [a, c, e, s, t, z]
     * @return a String that contains the letters the user has guessed so far during this round.
     */
    public String getGuessesMade() {
    	//Sort current arrayList of guesses alphabetically.
    	Collections.sort(guessesMade);
    	
    	//Build String to Return
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("[");
        for (int letter = 0; letter < guessesMade.size(); letter++) {        	
        	toReturn.append(guessesMade.get(letter));
        	//Only add space and comma if its going to display multiple letters.
        	if (guessesMade.size() > 1 && letter != guessesMade.size() -1) {
        		toReturn.append(", ");
        	}
        }
        toReturn.append("]");
        
        //Return
        return toReturn.toString();
    }


    /**
     * Check the status of a character.
     * @param guess The characater to check.
     * @return true if guess has been used or guessed this round of Hangman, false otherwise.
     */
    public boolean alreadyGuessed(char guess) {
    	//Convert guess to string, use contains method to check guessesMade ArrayList.
        if (guessesMade.contains(String.valueOf(guess))) {
        	return true;
        }
    	return false;
    }

    
    /**
     * Get the current pattern. The pattern contains '-''s for unrevealed (or guessed)
     * characters and the actual character for "correctly guessed" characters.
     * @return the current pattern.
     */
    public String getPattern() {
    	return pattern;
    }

    /**
     * Update the game status (pattern, wrong guesses, word list), based on the give
     * guess.
     * @param guess pre: !alreadyGuessed(ch), the current guessed character
     * @return return a tree map with the resulting patterns and the number of
     * words in each of the new patterns.
     * The return value is for testing and debugging purposes.
     */
    public TreeMap<String, Integer> makeGuess(char guess) {
    	//N.B. : METHOD IS SUB 25 LINES W/O SPACING AND COMMENTING
    	//Base TreeMap for this method.
    	TreeMap <String, ArrayList<String>> lists = new TreeMap<String, ArrayList<String>>();
    	
    	//Used at the very end to check if this method changed the pattern:
    	//if so, then guess is correct, otherwise: wrong
    	String oldPattern = pattern; 
    	
    	//Fill lists using helper method sub.
    	lists = fillLists(guess);
    	
    	//Create Set of Keys based on lists, helpful for operations below.
    	Set <String> keys = lists.keySet();
	
    	//Create ArrayList of Family Objects (class below) to be sorted by difficulty
    	ArrayList<families> toSort = new ArrayList<families>();
    	Iterator<String> it2 = keys.iterator();
    	while (it2.hasNext()) {
    		String temp = it2.next();
    		//Add new families object to array to be sorted
    		toSort.add(new families(temp, lists.get(temp)));
    	}
    	
    	//Sort using compareTo implemented in Families class
    	Collections.sort(toSort);
    	
    	//Determine Hardest from position in sorted ArrayList of families
    	//Using helper below
    	String keyOfHardest = keyOfHardest(toSort);

    	//Update Pattern to keyOfHardest
    	pattern = keyOfHardest;
    	
    	//Update WordsList
    	activeWords = lists.get(keyOfHardest);
    	
    	//Update numWrongGuesses if pattern is unchanged  	
    	if (oldPattern.equals(pattern)) numWrongGuesses++;
    	
    	//Update letters Guessed (convert guess to String)
    	guessesMade.add(String.valueOf(guess));
    	
    	//Update current guess
    	thisGuess++;
    	
    	//Return DebugMap asked by method, using helper below
    	return debugMap(keys, lists);
    }
    
    
    /**Method returns a TreeMap of lists to be used, with patterns as keys and an 
     * ArrayList of Strings of words fitting pattern.
     * pre: none, method is private and guess is already verified by makeGuess
     * post: Return a TreeMap of lists to be used
     */
    private TreeMap<String, ArrayList<String>> fillLists (char guess) {
    	//Tree Map to Return
    	TreeMap <String, ArrayList<String>> lists = new TreeMap<String, ArrayList<String>>();
    	
    	//Loop through the Active Words
    	for (int word = 0; word < activeWords.size(); word ++) {
    		StringBuilder currentPattern = new StringBuilder();
    		//Use existing pattern as base
    		currentPattern.append(pattern);
    		
    		//New pattern is created based on each Active Word's relation to guess
    		for (int letter = 0; letter < activeWords.get(word).length(); letter++) {
    			//If match, update pattern
    			if (activeWords.get(word).charAt(letter) == guess) {
    				currentPattern.setCharAt(letter, guess);
    			}
    		}	
    		//Convert Builder to String here for simplicity
    		String newPattern = currentPattern.toString();
    		
    		//Each temp is the ArrayList of words for each pattern
    		ArrayList <String> temp = new ArrayList <String>();
    		//Below takes into account if this key's list already has other words in it.
    		if (lists.containsKey(newPattern)) {
    			temp = lists.get(newPattern);
    		}	
    		
    		//Add current word
			temp.add(activeWords.get(word));
			//Add updated list to key (pattern created)
			lists.put(newPattern, temp);
    	}    	
    	return lists;
    }
    
    
    /**
     * Method returns the String corresponding to Key of the Hardest Family
     * pre: none, method is private
     * post: return key Of Hardest Family
     */
    private String keyOfHardest (ArrayList<families> toSort) {
    	//Initialize keyOfHardest
    	String keyOfHardest = "";
    	
    	//HARD: Always default to first in difficulty-sorted array
    	if(difficulty == HangmanDifficulty.HARD) { keyOfHardest = toSort.get(0).getKey(); }
    	
    	//MEDIUM: Every 4th time (using thisGuess) return 2nd hardest
    	if(difficulty == HangmanDifficulty.MEDIUM) { 
    		//Check if this guess is divisible by 4 and more than 1 word is left
    		if (thisGuess % 4 == 0 && toSort.size() > 1) {
    			//Return 2nd hardest
    			keyOfHardest = toSort.get(1).getKey();
    		//Otherwise return hardest
    		} else {
    			keyOfHardest = toSort.get(0).getKey(); 
    		}
    	}
    	//EASY: Every 2nd time (using thisGuess) return 2nd hardest
    	if(difficulty == HangmanDifficulty.EASY) {
    		//Check if this guess is divisible by 2 and more than 1 word is left
    		if (thisGuess % 2 == 0 && toSort.size() > 1) {
    			//Return 2nd hardest
    			keyOfHardest = toSort.get(1).getKey();
    		//Otherwise return hardest
    		} else {
    			keyOfHardest = toSort.get(0).getKey(); 
    		}
    	}
    	//Return String corresponding to key of hardest
    	return keyOfHardest;
    }
    
    
    /**
     * Method return map for Debug (pattern, number of words corresponding) as specified
     * pre: none, method is private
     * post: return map for debugging
     */
    private TreeMap<String, Integer> debugMap (Set <String> keys, TreeMap <String, ArrayList<String>> lists) {
    	//Create debug TreeMap
    	TreeMap<String, Integer> toReturn = new TreeMap<String, Integer>();
    	//Iterate through Set of keys created in makeGuess, passed as parameter
    	Iterator<String> it = keys.iterator();
    	while (it.hasNext()) {
    		String temp = it.next();
    		//Add pattern as key, size as value
    		toReturn.put(temp, lists.get(temp).size());
    	}
    	return toReturn;
    }


    /**
     * Return the secret word this HangmanManager finally ended up picking for this round.
     * If there are multiple possible words left one is selected at random.
     * <br> pre: numWordsCurrent() > 0
     * @return return the secret word the manager picked.
     */
    public String getSecretWord() {
    	//Return remaining word if only 1
    	if (activeWords.size() == 1) {
    		return activeWords.get(0);
    	//return random of remaining
    	} else {
        	return activeWords.get((int)(Math.random()*activeWords.size()+1));
    	}
    }
    
    /**
     * Nested Class: 
     * Creates a families object that
     * Stores size, revealedChars and name (key) of Arraylist to be sorted
     */
    public class families implements Comparable<families> {
    	//Instance Variables
    	private int size;
    	private int revealedChars;
    	private String key;
    	
    	public families (String key, ArrayList<String> family) {
    		//Set size to ArrayList length
    		size = family.size();
    		
    		//Set revealed Chars
    		revealedChars = 0;
    		for (int word = 0; word < key.length(); word++) {
    			//If not a - then its a revealed char
    			if (key.charAt(word) != '-') {
    				revealedChars++;
    			}
    		}
    		
    		this.key = key;	
    	}
    	
    	//Trivial Getters
    	//For each:
    	//Pre: none 
    	//Post: return value of instance variables for this family
    	public int getSize() {return size;}
    	public int revealedChars() {return revealedChars;}
    	public String getKey() {return key;}

    	
    	/**
    	 * Method: Overrides and implements compareTo
    	 * Will sort the arraylist of families by 
    	 * 1: size
    	 * if tie then 2: revealed Chars
    	 * if still tie then 3: lexiographical value
    	 * pre: familiy o1 must not be null
    	 * post: return 1, -1 or in god knows what case 0. (probably never)
    	 */
		@Override
		public int compareTo(families o1) {
			if(o1 == null) {
				throw new IllegalArgumentException("Families to compare should not be null");
			}
			//Compare size
			int result = o1.getSize() - this.getSize();
			//Compare revealedChars if tie
			if (result == 0) {
				result = this.revealedChars() - o1.revealedChars();
			}
			//Compare lexiographically if still tie
			if (result == 0) {
				result = this.getKey().compareTo(o1.getKey());
			}
			return result;
		}
    }
}