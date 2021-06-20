package gameframe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Mud extends Terrain{
    public Mud(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(146, 105, 50));
        g.fillRect(getX() - getGridLength()/2, getY() - getGridLength()/2, getGridLength(), getGridLength());
    }
}

