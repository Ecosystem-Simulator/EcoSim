package gameframe;

import java.util.ArrayList;
public class Animals extends Entity{
    private String gender;
    private int age;
    private int attractiveness;
    private boolean restrictedVision;
    public Animals(int x, int y, ArrayList<Entity> entities, String gender){
        super(x, y, entities);
        this.gender = gender;
        age = 0;
        attractiveness = (int)(Math.random()*100)+1;
        restrictedVision = false;
    }
    public void move(){
        //move code
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
