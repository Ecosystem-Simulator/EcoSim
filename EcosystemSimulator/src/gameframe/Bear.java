
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
    
    
}
