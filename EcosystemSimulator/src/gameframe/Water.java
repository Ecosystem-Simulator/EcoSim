package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Water extends Terrain {

    private boolean hasFish;

    private int eyeSize;
    private Color blue = new Color(58, 142, 216);
    private Color salmon = new Color(250, 128, 114);
    private Color black = new Color(0, 0, 0);
    private Color white = new Color(255, 255, 255);

    public Water(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean passable) {
        super(x, y, entities, entitygrid, gridLength, passable);
        setHeight(getHeight() / 3);
    }

    public boolean getHasFish() {
        return (hasFish);
    }

    public void setHasFish(boolean hasFish) {
        this.hasFish = hasFish;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(blue);
        g.fillRect(getX() - getGridLength() / 2, getY() - getGridLength() / 2, getGridLength(), getGridLength());
        if (hasFish) {
            //draw fish body
            g.setColor(salmon);
            g.fillOval(getX() - getLength() / 2, getY() - getHeight() / 2, getLength(), getHeight());
            //draw tail
            g.fillPolygon(new int[]{getX(), getX(), getX() + getLength() / 2, getX() + getLength() / 2}, new int[]{getY() - getHeight() / 2, getY() + getHeight() / 2, getY() + getHeight() / 8, getY() - getHeight() / 8}, 4);
            g.fillPolygon(new int[]{getX() + getLength() / 2, getX() + getLength() / 2, getX() + getLength() * 5 / 8, getX() + getLength() * 5 / 8}, new int[]{getY() + getHeight() / 8, getY() - getHeight() / 8, getY() - getHeight() / 3, getY() + getHeight() / 3}, 4);
//draw eye
            g.setColor(black);
            eyeSize = getLength() / 10;
            g.fillOval(getX() - getLength() / 3 - eyeSize / 2, getY() - getHeight() / 3 - eyeSize / 2, eyeSize, eyeSize);

            g.setColor(white);
            eyeSize = eyeSize / 2;
            g.fillOval(getX() - getLength() / 3 - eyeSize, getY() - getHeight() / 3 - eyeSize / 2, eyeSize, eyeSize);
            //draw mouth
            g.setColor(blue);
            g.fillArc(getX() - getLength() / 2, getY(), getLength() / 2, getHeight() / 3, 160, 60);
        }
    }

    @Override
    public void act() {
        if (this.getHasFish()) {
            int rand = (int) (Math.random() * 4) + 1;
            if (rand == 1) {
                //try to move right
                if (getGridX() + 1 < entitygrid[0].length) {
                    if (entitygrid[getGridX() + 1][getGridY()] instanceof Water) {
                        Water water = ((Water) entitygrid[getGridX() + 1][getGridY()]);
                        if (!water.getHasFish()) {
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
            if (rand == 2) {
                if (getGridX() - 1 >= 0) {
                    if (entitygrid[getGridX() - 1][getGridY()] instanceof Water) {
                        Water water = ((Water) entitygrid[getGridX() - 1][getGridY()]);
                        if (!water.getHasFish()) {
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
            if (rand == 3) {
                if (getGridY() + 1 < entitygrid.length) {
                    if (entitygrid[getGridX()][getGridY() + 1] instanceof Water) {
                        Water water = ((Water) entitygrid[getGridX()][getGridY() + 1]);
                        if (!water.getHasFish()) {
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
            if (rand == 4) {
                if (getGridY() - 1 >= 0) {
                    if (entitygrid[getGridX()][getGridY() - 1] instanceof Water) {
                        Water water = ((Water) entitygrid[getGridX()][getGridY() - 1]);
                        if (!water.getHasFish()) {
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
        } else {
            int rand = (int) (Math.random() * 250) + 1;
            if (rand == 1) {
                this.setHasFish(true);
            }
        }
    }
}
