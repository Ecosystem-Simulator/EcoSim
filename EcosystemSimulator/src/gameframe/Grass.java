package gameframe;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Grass extends Entity{
    private int d = 20;
    public Grass(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(gridX, gridY, entities, entitygrid, gridLength);
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(0, 154, 23));
        g.fillPolygon(new int[] {getX() - getLength()/2, getX(), getX() + getLength()/2}, new int[] {getY() + d, getY() - getHeight()*2 + d, getY() + d}, 3);
        g.fillPolygon(new int[] {getX() - getLength(), getX() - getLength(), getX() - getLength()/2}, new int[] {getY() + d, getY() - getHeight()*2 + d, getY() + d}, 3);        
        g.fillPolygon(new int[] {getX() + getLength(), getX() + getLength(), getX() + getLength()/2}, new int[] {getY() + d, getY() - getHeight()*2 + d, getY() + d}, 3);          
    }
}
