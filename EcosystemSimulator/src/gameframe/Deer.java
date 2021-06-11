package gameframe;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Deer extends Animal {
    public Deer(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(gridX, gridY, entities, entitygrid, gridLength);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(186, 135, 89));
        g.fillOval(getX() - getLength()/2, getY() - getHeight()/2, getLength(), getHeight()*2);
        
    }
    public void act(){
        if (getHunger() > getThirst() && getHunger() > 25){
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Berries || entities.get(k) instanceof Grass){
                    if (minDistance > distanceTo(entities.get(k))){
                        minDistance = distanceTo(entities.get(k));
                        target = entities.get(k);
                    }
                }
            }
        }
        else if (getReproductiveUrge() > 10){
            //set back to 200
            int minDistance = Integer.MAX_VALUE;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Deer){
                    Deer tempDeer = ((Deer) entities.get(k));
                    if (tempDeer.getGender() != (getGender()) && tempDeer.getReproductiveUrge() > 10){
                        if (minDistance > distanceTo(entities.get(k))){
                            minDistance = distanceTo(entities.get(k));
                            target = entities.get(k);
                        }
                    }
                }
            }
        }
        super.act();
    }
}
