package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Entity {
    private ArrayList<Entity> entities;
    private int x, y;
    //private double direction;
    //private boolean active = true;
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
    
    /*public boolean isActive() {
        return (active);
    }*/
    
    public void draw(Graphics g){
        g.setColor(new Color(255, 0, 0));
        g.fillOval(getX(), getY(), 10, 10);
    }
    
    public ArrayList<Entity> getEntities() {
        return (entities);
    }

}
