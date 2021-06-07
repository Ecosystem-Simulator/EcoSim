package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Rock extends Terrain{
    public Rock(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean passable){
        super(gridX, gridY, entities, entitygrid, gridLength, passable);
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(128, 132, 135));
        g.fillOval(getX() - getLength()*3/2, getY() - getHeight()*3/2, getLength()*3, getHeight()*3);
    }
}
