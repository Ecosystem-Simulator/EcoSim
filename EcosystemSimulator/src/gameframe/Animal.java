package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Animal extends Entity{
    private String gender;
    private int age;
    private int hunger;
    private int thirst;
    private int reproductiveUrge;
    private boolean restrictedVision;
    public Animal(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength);
        this.gender = gender;
        age = 0;
        hunger = 25;
        thirst = 25;
        reproductiveUrge = 0;
        restrictedVision = false;
    }
    public int getAge(){
        return(age);
    }
    public int getHunger(){
        return(hunger);
    }
    public int getThirst(){
        return(thirst);
    }
    public int getReproductiveUrge(){
        return(reproductiveUrge);
    }
    public void resetReproductiveUrge(){
        reproductiveUrge = 0;
    }
    public String getGender(){
        return(gender);
    }
    public void act(){
        //move code
        age++;
        hunger++;
        thirst++;
        reproductiveUrge++;
        //target code;
        
    }
    public void eat(){
        //eat code
    }
    public void drink(){
        //drink code
    }
    public void mate(){
        //mate code
    }
    public void die(){
        //die code
    }
}

