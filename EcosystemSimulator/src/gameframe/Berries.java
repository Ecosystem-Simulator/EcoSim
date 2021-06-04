/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import java.util.ArrayList;

/**
 *
 * @author 1090720
 */
public class Berries extends Food{
    public Berries(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, entitygrid, gridLength);
        setNutritionVal(20);
    }
}
