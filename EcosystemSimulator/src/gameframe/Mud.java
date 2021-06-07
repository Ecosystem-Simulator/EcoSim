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
 * @author 1090720
 */
public class Mud extends Terrain{
    public Mud(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean passable){
        super(x, y, entities, entitygrid, gridLength, passable);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(146, 105, 50));
        g.fillRect(getX() - getGridLength()/2, getY() - getGridLength()/2, getGridLength(), getGridLength());
    }
}

