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
    private Entity target;
    private ArrayList<String> moves = new ArrayList();
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
        // moving to water
        //if (getThirst() > getHunger() && getThirst() > 25){
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Water){
                    if (minDistance > distanceTo(entities.get(k))){
                        minDistance = distanceTo(entities.get(k));
                        target = entities.get(k);
                        //row = entities.get(k).getGridX();
                        //col = entities.get(k).getGridY();
                    }
                }
            }
        //}
        if(distanceTo(target) > 1)
            move();
        else if(target instanceof Water){
            drink();
        }
        
    }
    
    public void move(){
        /*
        if(target.getGridX() > getGridX() && entitygrid[getGridX() + 1][getGridY()] == null){
            moveRight();
        }
        else if(target.getGridY() > getGridY() && entitygrid[getGridX()][getGridY() + 1] == null){
            moveDown();
        }
        else if(target.getGridY() < getGridY() && entitygrid[getGridX()][getGridY() - 1] == null){
            moveUp();
        }
        else if(target.getGridX() < getGridX() && entitygrid[getGridX() - 1][getGridY()] == null){
            moveLeft();
        }
        else{
        }
*/
    }

    public Animal() {
        super(0, 0, null, null, 0);
    }
    public void eat(){
        //eat code
    }
    public void drink(){
        thirst = 0;
    }
    public void mate(){
        //mate code
    }
    public void die(){
        //die code
    }
}

