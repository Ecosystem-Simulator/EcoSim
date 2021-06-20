package gameframe;
import java.util.ArrayList;
        
public class Terrain extends Entity {
   
    public Terrain(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength); 
    }
        
}
