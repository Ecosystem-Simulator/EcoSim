package gameframe;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Water extends Terrain{
    
    public Water(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean passable){
        super(x, y, entities, entitygrid, gridLength, passable);
    }    
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(58, 142, 216));
        g.fillRect(getX() - getGridLength()/2, getY() - getGridLength()/2, getGridLength(), getGridLength()); 
    }
}
