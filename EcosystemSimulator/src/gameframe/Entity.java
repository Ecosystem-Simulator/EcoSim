package gameframe;

import java.util.ArrayList;

public class Entity {
    protected static ArrayList<Entity> entities;
    private int x, y;
    private double direction;
    private boolean active = true;
    public Entity(int x, int y, ArrayList<Entity> entities){
        this.x = x;
        this.y = y;
        this.entities = entities;
    }
    
    public int getX(){
        return(x);
    }
    
    public int getY(){
        return(y);
    }
    
    public boolean isActive() {
        return (active);
    }
    public ArrayList<Entity> getEntities() {
        return entities;
    }

}
