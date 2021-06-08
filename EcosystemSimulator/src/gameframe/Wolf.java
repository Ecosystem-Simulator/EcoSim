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
    public Wolf(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, gender, entitygrid, gridLength);
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(105, 105, 105));
        g.fillOval(getX() - getLength(), getY() - getHeight(), getLength()*2, getHeight()*2);
        //g.drawString("Wolf", getX() - getLength(), getY() + getHeight()*2);
    }
}
