package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Entity {
    ArrayList<Entity> entities;
    Entity[][] entitygrid;
    private int x, y;
    private int gridX, gridY;
    private int gridLength;
    private int length = 10;
    private int height = 10;
    //private double direction;
    private boolean active;
    public Entity(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        this.gridX = gridX;
        this.gridY = gridY;
        x = gridX*gridLength + gridLength/2;
        y = gridY*gridLength + gridLength/2;
        this.entities = entities;
        this.entitygrid = entitygrid;
        this.gridLength = gridLength;
        active = true;
        entitygrid[gridX][gridY] = this;
    }
    
    public int getGridX(){
        return(gridX);
    }
    
    public int getGridY(){
        return(gridY);
    }
    
    public int getX(){
        return(x);
    }
    
    public int getY(){
        return(y);
    }
    public int getLength(){
        return(length);
    }
    public int getHeight(){
        return(height);
    }
    public int getGridLength(){
        return gridLength;
    }
    public int distanceTo(Entity e){
        return(Math.abs(getGridX() - e.getGridX()) + Math.abs(getGridY() - e.getGridY()));
    }
    public boolean isActive() {
        return (active);
    }
    public void moveLeft(){
        if(gridX - 1 >= 0){
            entitygrid[gridX][gridY] = null;
            gridX--;
            entitygrid[gridX][gridY] = this;
            x = gridX*gridLength + gridLength/2;
        }
    }
    
    public void moveRight(){
        if(gridX + 1 < entitygrid[0].length){
            entitygrid[gridX][gridY] = null;
            gridX++;
            entitygrid[gridX][gridY] = this;
            x = gridX*gridLength + gridLength/2;
        }
    }
    public void moveUp(){
        if(gridY - 1 >= 0){
            entitygrid[gridX][gridY] = null;
            gridY--;
            entitygrid[gridX][gridY] = this;
            y = gridY*gridLength + gridLength/2;
        }
    }
    
    public void moveDown(){
        if(gridY + 1 < entitygrid[0].length){
            entitygrid[gridX][gridY] = null;
            gridY++;
            entitygrid[gridX][gridY] = this;
            y = gridY*gridLength + gridLength/2;
        }
    }
    
    public void act(){
         
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(255, 0, 0));
        g.fillOval(getX() - length/2, getY() - height/2, length, height);
    }
    
    public ArrayList<Entity> getEntities() {
        return (entities);
    }

}
