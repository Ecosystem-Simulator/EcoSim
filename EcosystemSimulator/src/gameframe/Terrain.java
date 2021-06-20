package gameframe;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
        
public class Terrain extends Entity {
   
    public Terrain(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength); 
    }
        
}
