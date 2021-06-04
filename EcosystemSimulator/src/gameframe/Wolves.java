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
public class Wolves extends Animal{
    public Wolves(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, gender, entitygrid, gridLength);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(220, 220, 220));
        g.fillOval(getX() - getLength()/2, getY() - getHeight()/2, getLength(), getHeight());
    }
}
