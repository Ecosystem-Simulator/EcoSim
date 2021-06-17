package gameframe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class FloodWater extends Water{
    
    private Color blue = new Color(58, 142, 216);
    static boolean justMade;
     
    public FloodWater(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean justMade) {
        super(x, y, entities, entitygrid, gridLength);
        this.justMade = justMade;
    }
    @Override
     public void draw(Graphics g) {
        g.setColor(blue);
        g.fillRect(getX() - getGridLength() / 2, getY() - getGridLength() / 2, getGridLength(), getGridLength());
     }

}
