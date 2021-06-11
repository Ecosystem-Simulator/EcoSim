/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author thuph
 */
public class Wolf extends Animal{
    Deer d;
    
    public Wolf(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, gender, entitygrid, gridLength);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(105, 105, 105));
        g.fillOval(getX() - getLength(), getY() - getHeight(), getLength()*2, getHeight()*2);
        //g.drawString("Wolf", getX() - getLength(), getY() + getHeight()*2);
    }
    
    @Override
    public void act(){
        if (getHunger() > getThirst() && getHunger() > 25){
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Berries || entities.get(k) instanceof Deer || entities.get(k) instanceof Salmon){
                    if (minDistance > distanceTo(entities.get(k))){
                        minDistance = distanceTo(entities.get(k));
                        target = entities.get(k);
                    }
                }
            }
        }
        
        else if (getReproductiveUrge() > 200){
            int minDistance = Integer.MAX_VALUE;
            for(int k = 0; k < entities.size(); k++){
                if (entities.get(k) instanceof Wolf){
                    Wolf tempWolf = ((Wolf) entities.get(k));
                    if (!tempWolf.getGender().equals(getGender()) && tempWolf.getReproductiveUrge() > 200){
                        if (minDistance > distanceTo(entities.get(k))){
                            minDistance = distanceTo(entities.get(k));
                        }
                    }
                }
            }
            resetReproductiveUrge();
        }
        super.act();
    }
    
    public Deer getNearestDeer(){
        int minDist = Integer.MAX_VALUE;
        for(Entity e : entities){
            if(e instanceof Deer && distanceTo(e) < minDist){
                d = (Deer)e;
            }
        }
        return d;
    }
}
