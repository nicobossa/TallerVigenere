/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TallerVigenere;

/**
 *
 * @author nicom
 */
public class repetitions {
    
    public repetitions(String character, int repetition){
        this.character = character;
        this.repetion = repetition;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getRepetion() {
        return repetion;
    }

    public void setRepetion(int repetion) {
        this.repetion = repetion;
    }
    String character;
    int repetion;
    
    
    
}
