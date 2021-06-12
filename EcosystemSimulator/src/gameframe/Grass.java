package gameframe;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Grass extends Food {

    private int d = 20;

    public Grass(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(gridX, gridY, entities, entitygrid, gridLength);
        this.nutritionVal = 10;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 154, 23));
        g.fillPolygon(new int[]{getX() - getLength() / 4, getX(), getX() + getLength() / 4}, new int[]{getY() + d, getY() - getHeight() + d, getY() + d}, 3);
        g.fillPolygon(new int[]{getX() - getLength() / 2, getX() - getLength() / 2, getX() - getLength() / 4}, new int[]{getY() + d, getY() - getHeight() + d, getY() + d}, 3);
        g.fillPolygon(new int[]{getX() + getLength() / 2, getX() + getLength() / 2, getX() + getLength() / 4}, new int[]{getY() + d, getY() - getHeight() + d, getY() + d}, 3);
    }
}
