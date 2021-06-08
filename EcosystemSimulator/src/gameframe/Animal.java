package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Animal extends Entity{
    private String gender;
    private int age;
    private int hunger;
    private int thirst;
    private int attractiveness;
    private boolean restrictedVision;
    public Animal(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength);
        this.gender = gender;
        age = 0;
        hunger = 0;
        thirst = 0;
        attractiveness = (int)(Math.random()*100)+1;
        restrictedVision = false;
    }
    public void act(){
        //move code
        age++;
        hunger++;
        thirst++;
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

