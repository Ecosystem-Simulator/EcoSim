
package gameframe;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Bear extends Animal{
    public Bear(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, gender, entitygrid, gridLength);
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(255, 0, 0));
        g.fillOval(getX() - getLength()/2, getY() - getHeight()/2, getLength(), getHeight());
    }
    
    
}
