
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
        else if (getHunger() > getThirst() && getHunger() > 25){
            //look for food
        }
        else if (getThirst() > getHunger() && getThirst() > 25){
            //look for water
        }
        else if (getReproductiveUrge() > 200){
            //mate
            resetReproductiveUrge();
        }
        
    }
}
