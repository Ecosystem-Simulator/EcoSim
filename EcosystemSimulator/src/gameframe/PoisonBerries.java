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
public class PoisonBerries extends Berries {
    public PoisonBerries(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength);
        setNutritionVal(0);
    }
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(0, 153, 0));
        g.fillOval(getX() - getLength(), getY() - getHeight(), getLength()*2, getHeight()*2);
        g.setColor (new Color(153, 0, 76));
        g.fillOval(getX() - getLength()/2, getY() - getHeight()/2, getLength()/2, getHeight()/2);
        g.fillOval(getX() + getLength()/2, getY() - getHeight()/4, getLength()/2, getHeight()/2);
    }
}
