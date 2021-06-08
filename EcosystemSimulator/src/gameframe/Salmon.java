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
public class Salmon extends Food {
    public Salmon (int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength);
        setNutritionVal(20);
    }
    public void draw(Graphics g){
        g.setColor(new Color(253, 162, 180));
        g.fillOval(getX() - getLength()/2, getY() - getHeight()/2, getLength(), getHeight()*2);
    }
}
