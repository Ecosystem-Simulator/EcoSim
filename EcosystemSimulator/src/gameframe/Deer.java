package gameframe;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Deer extends Animal {
    public Deer(int gridX, int gridY, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(gridX, gridY, entities, gender, entitygrid, gridLength);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(186, 135, 89));
        g.fillOval(getX() - getLength()/2, getY() - getHeight()/2, getLength(), getHeight()*2);
        
    }
}
