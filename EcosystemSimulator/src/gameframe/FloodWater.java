package gameframe;

import java.awt.Color;
import java.util.ArrayList;

public class FloodWater extends Water{
    
    private Color blue = new Color(58, 142, 216);
    
    public FloodWater(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
    
    }
}
