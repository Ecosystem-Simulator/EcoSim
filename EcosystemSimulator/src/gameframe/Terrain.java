package gameframe;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
        
public class Terrain extends Entity {
    
    private boolean passable;
   
    public Terrain(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean passable){
        super(x, y, entities, entitygrid, gridLength);
        this.passable = passable;
       
    }
    
}
