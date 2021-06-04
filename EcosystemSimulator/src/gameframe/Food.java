/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import java.util.ArrayList;

/**
 *
 * @author thuph
 */
public class Food extends Entity{
    private int age;
    public Food(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        age = 0;
    }
}
