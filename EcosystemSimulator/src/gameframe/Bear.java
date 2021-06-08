
package gameframe;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Bear extends Animal{
    public Bear(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, gender, entitygrid, gridLength);
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(105, 57, 9));
        g.fillOval(getX() - getLength(), getY() - getHeight(), getLength()*2, getHeight()*2);
    }
    
    @Override
    public void act(){
        if (getAge() > 1000 || getHunger() > 100 || getThirst() > 100){
            die();
        }
        //THIS WILL NEED TO BE MOVED TO ANIMAL
        else if (getHunger() > getThirst() && getHunger() > 25){
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Berries || entities.get(k) instanceof Deer || entities.get(k) instanceof Salmon){
                    if (minDistance > distanceTo(entities.get(k))){
                        minDistance = distanceTo(entities.get(k));
                        row = entities.get(k).getGridX();
                        col = entities.get(k).getGridY();
                    }
                }
            }
        }
        /*else if (getThirst() > getHunger() && getThirst() > 25){
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Water){
                    if (minDistance > distanceTo(entities.get(k))){
                        minDistance = distanceTo(entities.get(k));
                        row = entities.get(k).getGridX();
                        col = entities.get(k).getGridY();
                    }
                }
            }
        }*/
        else if (getReproductiveUrge() > 200){
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Bear){
                    Bear tempBear = ((Bear) entities.get(k));
                    if (!tempBear.getGender().equals(getGender()) && tempBear.getReproductiveUrge() > 200){
                        if (minDistance > distanceTo(entities.get(k))){
                            minDistance = distanceTo(entities.get(k));
                            row = entities.get(k).getGridX();
                            col = entities.get(k).getGridY();
                        }
                    }
                }
            }
            resetReproductiveUrge();
        }
        super.act();
    }
}
