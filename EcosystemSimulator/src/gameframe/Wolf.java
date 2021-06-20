/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Wolf extends Animal {

    private int length;
    private int eyeSize;
    private int snoutLength;
    private int noseSize;
    private int d;

    private Color grey = new Color(115, 125, 132);
    private Color black = new Color(0, 0, 0);
    private Color white = new Color(255, 255, 255);
    private Color lightgrey = new Color(144, 157, 166);
    private Color darkgrey = new Color(92, 100, 106);

    public Wolf(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        setMaxHunger(200);
        setMaxThirst(100);
        if (getGender() == 2) {
            setHeight(getHeight() * 3 / 4);
            setLength(getLength() * 3 / 4);
        }
    }
    
    public Wolf(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, int gender, int hunger, int thirst, int reproductiveUrge) {
        super(x, y, entities, entitygrid, gridLength);
        setMaxHunger(200);
        setMaxThirst(100);
        setGender(gender);
        setHunger(hunger);
        setThirst(thirst);
        setReproductiveUrge(reproductiveUrge);
        if (getGender() == 2) {
            setHeight(getHeight() * 3 / 4);
            setLength(getLength() * 3 / 4);
        }
    }

    @Override
    public void draw(Graphics g) {
        d = getGridLength() / 15;
        length = getLength();
        //draw ears
        g.setColor(grey);
        g.fillPolygon(new int[]{getX() + length / 8, getX() + length * 9 / 16, getX() + length / 2}, new int[]{getY() - length / 2 + d, getY() - length * 3 / 4 + d, getY() - length / 4 + d}, 3);
        g.fillPolygon(new int[]{getX() - length / 8, getX() - length * 9 / 16, getX() - length / 2}, new int[]{getY() - length / 2 + d, getY() - length * 3 / 4 + d, getY() - length / 4 + d}, 3);

        g.setColor(darkgrey);
        g.fillPolygon(new int[]{getX() + length / 16, getX() + length / 2, getX() + length * 7 / 16}, new int[]{getY() - length * 3 / 8 + d, getY() - length * 5 / 8 + d, getY() - length * 1 / 8 + d}, 3);
        g.fillPolygon(new int[]{getX() - length / 16, getX() - length / 2, getX() - length * 7 / 16}, new int[]{getY() - length * 3 / 8 + d, getY() - length * 5 / 8 + d, getY() - length * 1 / 8 + d}, 3);

        //draw head
        g.setColor(grey);
        g.fillOval(getX() - length / 2, getY() - length / 2, length, length);
        //draw eyes
        g.setColor(black);
        eyeSize = length / 5;
        g.fillOval(getX() - length / 4 - eyeSize / 2, getY() - length / 4, eyeSize, eyeSize);
        g.fillOval(getX() + length / 4 - eyeSize / 2, getY() - length / 4, eyeSize, eyeSize);

        g.setColor(white);
        g.fillOval(getX() - length / 4 - eyeSize / 4, getY() - length / 4, eyeSize / 2, eyeSize / 2);
        g.fillOval(getX() + length / 4 - eyeSize / 4, getY() - length / 4, eyeSize / 2, eyeSize / 2);

        //draw snout
        g.setColor(lightgrey);
        snoutLength = length / 3;
        //g.fillOval(getX() - snoutLength / 4, getY() + length / 4 - snoutLength / 2, snoutLength / 2, snoutLength);

        g.fillRoundRect(getX() - snoutLength / 2, getY(), snoutLength, snoutLength, snoutLength * 3 / 4, snoutLength * 3 / 4);

        //draw nose
        g.setColor(black);
        noseSize = snoutLength / 2;
        g.fillOval(getX() - noseSize / 2, getY() + noseSize, noseSize, noseSize * 3 / 4);

        //draw bars
        //hunger
        barLength = getGridLength() / 2;
        barHeight = getGridLength() / 16;
        g.setColor(new Color(255, 0, 0));
        g.fillRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight, barLength * getHunger() / getMaxHunger(), barHeight);

        //thirst
        g.setColor(new Color(0, 0, 255));
        g.fillRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight * 2, barLength * getThirst() / getMaxThirst(), barHeight);

        g.setColor(black);
        g.drawRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight, barLength, barHeight);
        g.drawRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight * 2, barLength, barHeight);
    }

    @Override
    public void act() {
        target = null;
        //looking for food
        if (getHunger() > getThirst() && getHunger() > getMaxHunger() / 4) {
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if (entities.get(k) instanceof Deer) {
                    if (minDistance > distanceTo(entities.get(k))) {
                        if (!getRestrictedVision() || (getRestrictedVision() && distanceTo(entities.get(k)) < 5)) {
                            minDistance = distanceTo(entities.get(k));
                            target = entities.get(k);
                        }
                    }
                } 
                else if (entities.get(k) instanceof Water) {
                    if (((Water) entities.get(k)).getHasFish()) {
                        if (minDistance > distanceTo(entities.get(k))) {
                            if (!getRestrictedVision() || (getRestrictedVision() && distanceTo(entities.get(k)) < 5)) {
                                minDistance = distanceTo(entities.get(k));
                                target = entities.get(k);
                            }

                        }
                    }
                }
            }
        }
        //mating
        else if (getReproductiveUrge() > 150) {
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if (entities.get(k) instanceof Wolf) {
                    Wolf tempWolf = ((Wolf) entities.get(k));
                    if (tempWolf.getGender() != (getGender()) && tempWolf.getReproductiveUrge() > 150) {
                        if (minDistance > distanceTo(entities.get(k))) {
                            if (!getRestrictedVision() || (getRestrictedVision() && distanceTo(entities.get(k)) < 5)) {
                                minDistance = distanceTo(entities.get(k));
                                target = entities.get(k);
                            }

                        }
                    }
                }
            }

        }
        super.act();
    }
}
