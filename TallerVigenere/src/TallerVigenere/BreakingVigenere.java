/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TallerVigenere;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author nicom
 */
public class BreakingVigenere {

    public static String[][] vigenereCode = new String[27][27];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String prueba = "LNUDVMUYRMUDVLLPXAFZUEFAIOVWVMUOVMUEVMUEZCUDVSYWCIVCFGUCUNYCGALLGRCYTIJTRNNPJQOPJEMZITYLIAYYKRYEFDUDCAMAVRMZEAMBLEXPJCCQIEHPJTYXVNMLAEZTIMUOFRUFC";
        fillVigenereTable();
        int n = keyLength(prueba);
        String[] m = crack(prueba, n);
        while (true) {
            int option = Integer.parseInt(JOptionPane.showInputDialog("Eliga una opción: \n"
                    + "1. Encriptar mensaje\n"
                    + "2. Conocer la longitud de un texto cifrado"
                    + ""));
            switch (option) {
                case 1:
                    String plainText = JOptionPane.showInputDialog("Texto a cifrar:");
                    String key = JOptionPane.showInputDialog("Clave para el cifrado:");
                    JOptionPane.showMessageDialog(null, "El texto cifrado es: \n" + encrypt(plainText, key));
                case 2:
                    String text = JOptionPane.showInputDialog("Texto a conocer el tamaño de la clave:");
                    JOptionPane.showMessageDialog(null, "El texto cifrado es: \n" + keyLength(text));
            }
        }
    }

    /**
     * Returns the most possible key for the encrypted text following Kasiski
     * method
     *
     * @param text //Text encrypted
     * @return String // Most possible key length
     */
    public static int keyLength(String text) {
        int keyLength = 0;
        String chain = "";
        ArrayList<repetitions> wordsChain = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<Integer> multipliers = new ArrayList<>();
        int divition = text.length() / 3;

        for (int i = 0; i < divition; i++) {
            chain = text.substring(i, i + 3);
            wordsChain.add(search(chain, text));
        }

        for (int i = 0; i < wordsChain.size(); i++) {
            for (int j = 1; j < wordsChain.size() - 1; j++) {
                if (wordsChain.get(i).getCharacter().compareToIgnoreCase(wordsChain.get(j).getCharacter()) == 0
                        || wordsChain.get(j).getRepetion() == 1) {
                    wordsChain.remove(j);
                }
            }
        }

        int c = 0;
        for (int i = 0; i < wordsChain.size(); i++) {
            if (wordsChain.get(i).getRepetion() > 1 && c < 4) {
                String comparator1 = wordsChain.get(i).getCharacter();
                c++;
                for (int j = 0; j < text.length() - 3; j++) {
                    String comparator2 = text.substring(j, j + 3);
                    if (comparator1.equals(comparator2)) {
                        positions.add(j);
                    }
                }
            }
        }

        for (int i = 0; i < positions.size() - 1; i++) {
            if (positions.get(i) < positions.get(i + 1)) {
                multipliers.add(Math.abs(positions.get(i) - positions.get(i + 1)));
            }
        }

        int[] numbers = multipliers.stream().mapToInt(i -> i).toArray();
        keyLength = findGCD(numbers, numbers.length);
        return keyLength;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static int gcd(int a, int b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

    // Function to find gcd of array of 
    // numbers 
    /**
     *
     * @param arr
     * @param n
     * @return
     */
    public static int findGCD(int arr[], int n) {
        int result = arr[0];
        for (int i = 1; i < n; i++) {
            result = gcd(arr[i], result);

            if (result == 1) {
                return 1;
            }
        }

        return result;
    }

    /**
     * Method which looks for text matches on the plain text following by
     *
     * @param pattern
     * @param chipherText
     * @return
     */
    public static repetitions search(String pattern, String cipherText) {
        repetitions repetition = new repetitions(pattern, 0);
        Pattern secuence = Pattern.compile(pattern);
        Matcher text = secuence.matcher(cipherText);
        ArrayList<String> matches = new ArrayList<>();
        String[] matchesArray;

        while (text.find()) {
            String match = text.group();
            matches.add(match);
        }

        matchesArray = new String[matches.size()];
        matches.toArray(matchesArray);

        int counter = matches.size();
        repetition.setRepetion(counter);

        return repetition;
    }

    /**
     *
     * @param text //Plain text ready for encryption
     * @param key //A defined key for Vigenere encryption
     * @return String //String corresponding to the encrypted text
     */
    public static String encrypt(String text, String key) {
        //Start encrypting 
        String textEncrypted = "";
        int count = 0;
        String cleanText = text.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "");
        String cleanKey = key.toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "");

        char[] divideText = cleanText.toCharArray();
        char[] divideKey = cleanKey.toCharArray();
        char[] iteratedKey = new char[divideText.length];

        for (int i = 0; i < divideText.length; i++) {  //Filling iteratedKey with Key 
            if (count < divideKey.length) {
                iteratedKey[i] = divideKey[count];
            } else {
                count = 0;
                iteratedKey[i] = divideKey[count];
            }
            count++;
        }

        count = 0;
        while (count < divideText.length) {
            char letterVigenere;
            char keyLetter;
            char plainLetter;
            int xPosition = -1;
            int yPosition = -1;
            for (int i = 0; i < 27; i++) {
                plainLetter = divideText[count];
                keyLetter = iteratedKey[count];
                letterVigenere = vigenereCode[0][i].toCharArray()[0];

                if (plainLetter == letterVigenere) {
                    yPosition = i;
                }
                if (keyLetter == letterVigenere) {
                    xPosition = i;
                }
                if (yPosition != -1 && xPosition != -1) {
                    textEncrypted += vigenereCode[xPosition][yPosition];
                    break;
                }
            }
            count++;
        }
        return textEncrypted;
    }

    /**
     *
     * @param text //Encrypted text for analysis
     * @param keylen //Length of the key used for Vigenere encryption
     * @return String[] //An array within possible keys for the text
     */
    public static String[] crack(String text, int keylen) {
        //Define posibles keys}
        String[] possibleKeys = new String[20];

        char[] divideText = text.toCharArray();
        ArrayList<ArrayList> subcrypts = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < keylen; i++) {
            ArrayList<String> sub = new ArrayList<>();
            for (int j = count; j < text.length(); j += keylen) {
                if (divideText[j] < divideText.length || i == 0) {
                    sub.add(String.valueOf(divideText[j]));
                }
            }
            subcrypts.add(sub);
            count++;
        }

        String[] alphabet = new String[27];

        alphabet[0] = "a";     //Defining each letter of the alphabet
        alphabet[1] = "b";
        alphabet[2] = "c";
        alphabet[3] = "d";
        alphabet[4] = "e";
        alphabet[5] = "f";
        alphabet[6] = "g";
        alphabet[7] = "h";
        alphabet[8] = "i";
        alphabet[9] = "j";
        alphabet[10] = "k";
        alphabet[11] = "l";
        alphabet[12] = "m";
        alphabet[13] = "n";
        alphabet[14] = "ñ";
        alphabet[15] = "o";
        alphabet[16] = "p";
        alphabet[17] = "q";
        alphabet[18] = "r";
        alphabet[19] = "s";
        alphabet[20] = "t";
        alphabet[21] = "u";
        alphabet[22] = "v";
        alphabet[23] = "w";
        alphabet[24] = "x";
        alphabet[25] = "y";
        alphabet[26] = "z";

        int[][] frequencyTable = new int[keylen][27];
        int[][] acumulativeTable = new int[keylen][27];
        int frequency = 0;

        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < keylen; j++) {
                for (int k = 0; k < subcrypts.get(j).size(); k++) {
                    if (alphabet[i].equalsIgnoreCase(String.valueOf(subcrypts.get(j).get(k)))) {
                        frequency++;
                        frequencyTable[j][i] = frequency;
                    }
                }
                frequency = 0;
            }
            frequency = 0;
        }

        for (int i = 0; i < keylen; i++) {
            for (int j = 0; j < 27; j++) {
                System.out.print(frequencyTable[i][j] + " ");
            }
            System.out.println("\n");
        }

        for (int i = 0; i < keylen; i++) {
            for (int j = 0; j < 27; j++) {
                if (j + 4 < 27 && j + 15 < 27) {
                    frequency = frequencyTable[i][j] + frequencyTable[i][j + 4] + frequencyTable[i][j + 15];
                } else if (j + 15 >= 27) {
                    if (j + 4 >= 27) {
                        frequency = frequencyTable[i][j] + frequencyTable[i][j - 23] + frequencyTable[i][j - 12];
                    } else {
                        frequency = frequencyTable[i][j] + frequencyTable[i][j + 4] + frequencyTable[i][j - 12];
                    }
                }
                acumulativeTable[i][j] = frequency;
                frequency = 0;
            }
            frequency = 0;
        }

        for (int i = 0; i < keylen; i++) {
            for (int j = 0; j < 27; j++) {
                System.out.print(acumulativeTable[i][j] + " ");
            }
            System.out.println("\n");
        }

        int maxNumber;
        String possibleKey;
        
        ArrayList<ArrayList> letters = new ArrayList<>();
        for (int i = 0; i < keylen; i++) {
            ArrayList<String> let = new ArrayList<>();
            maxNumber = acumulativeTable[i][0];
            int letter = 0;
            for (int j = 0; j < 27; j++) {
                if (maxNumber <= acumulativeTable[i][j]) {
                    maxNumber = acumulativeTable[i][j];
                    letter = j;
                }
            }
            let.add(alphabet[letter]);
            letters.add(let);
            int c = 0;
            while (c < 27) {
                if (maxNumber == acumulativeTable[i][c] && alphabet[letter].equalsIgnoreCase(alphabet[c]) == false) {
                    let.add(alphabet[c]);
                }
                c++;
            }
            c = 0;
            letter = 0;
            maxNumber = 0;
        }

        String key = "";
        int counter = 0;
        boolean confirm = true;
        for (int i = 0; i < letters.size(); i++) {
            int c = 0;
            for (int j = 0; j < letters.get(i).size(); j++) {
                while (c < letters.size()) {
                    if (c != i) {
                        key += letters.get(c).get(0);
                    } else {
                        key += letters.get(i).get(j).toString();
                    }
                    c++;
                }
                c = 0;
                while(c < 20){
                    if(key.equalsIgnoreCase(possibleKeys[c])){
                       confirm = false;
                       break;
                    } else{
                        confirm = true;
                    }  
                    c++;
                } 
                if(confirm == true){
                    possibleKeys[counter] = key;
                    counter++;
                }
                key = "";
                c = 0;
            }
        }
        return possibleKeys;
    }

    /**
     *
     * @param plain //Plain text for comparation
     * @param encrypted //Encrypted text throught Vigenere method
     * @param key //Main key for the Vigenere
     * @return boolean that confirms matches between plain text and encrypted
     * one
     */
    public static boolean verify(String plain, String encrypted, String key) {
        //Compare plain text between encrypted one 
        boolean c = false;
        String e = encrypt(plain, key);
        if (e.equalsIgnoreCase(encrypted)) {
            c = true;
        } else {
            c = false;
        }
        return c;
    }

    /**
     *
     */
    public static void fillVigenereTable() {
        String[] alphabet = new String[27];

        alphabet[0] = "a";     //Defining each letter of the alphabet
        alphabet[1] = "b";
        alphabet[2] = "c";
        alphabet[3] = "d";
        alphabet[4] = "e";
        alphabet[5] = "f";
        alphabet[6] = "g";
        alphabet[7] = "h";
        alphabet[8] = "i";
        alphabet[9] = "j";
        alphabet[10] = "k";
        alphabet[11] = "l";
        alphabet[12] = "m";
        alphabet[13] = "n";
        alphabet[14] = "ñ";
        alphabet[15] = "o";
        alphabet[16] = "p";
        alphabet[17] = "q";
        alphabet[18] = "r";
        alphabet[19] = "s";
        alphabet[20] = "t";
        alphabet[21] = "u";
        alphabet[22] = "v";
        alphabet[23] = "w";
        alphabet[24] = "x";
        alphabet[25] = "y";
        alphabet[26] = "z";

        for (int i = 0; i < alphabet.length; i++) {
            int count = i;
            for (int j = 0; j < alphabet.length; j++) {
                vigenereCode[i][j] = alphabet[count];
                if (count < 26) {
                    count++;
                } else {
                    count = 0;
                }
                System.out.print(vigenereCode[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
