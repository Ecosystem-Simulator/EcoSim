package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;

public class Animal extends Entity {

    private String gender;
    private int age;
    private int hunger;
    private int thirst;
    private int reproductiveUrge;
    private boolean restrictedVision;
    private Entity target;
    public HashMap<String, Integer> favourableness = new HashMap<>();
    private ArrayList<String> moves = new ArrayList();

    public Animal(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        this.gender = gender;
        age = 0;
        hunger = 25;
        thirst = 25;
        reproductiveUrge = 0;
        restrictedVision = false;
        favourableness.put("up", 0);
        favourableness.put("down", 0);
        favourableness.put("left", 0);
        favourableness.put("right", 0);
    }

    public int getAge() {
        return (age);
    }

    public int getHunger() {
        return (hunger);
    }

    public int getThirst() {
        return (thirst);
    }

    public int getReproductiveUrge() {
        return (reproductiveUrge);
    }

    public void resetReproductiveUrge() {
        reproductiveUrge = 0;
    }

    public String getGender() {
        return (gender);
    }

    public void act() {
        //move code
        age++;
        hunger++;
        thirst++;
        reproductiveUrge++;
        // moving to water
        if (thirst >= 100) {
            die();
        }
        //if (getThirst() > getHunger() && getThirst() > 25){
        int minDistance = Integer.MAX_VALUE;
        int row = 0;
        int col = 0;
        for (int k = 0; k < entities.size(); k++) {
            if (entities.get(k) instanceof Water) {
                if (minDistance > distanceTo(entities.get(k))) {
                    minDistance = distanceTo(entities.get(k));
                    target = entities.get(k);
                }
            }
        }
        //}
        if (distanceTo(target) > 1) {
            move();
        } else if (target instanceof Water) {
            drink();
        }

    }

    public void move() {
        favourableness.replace("up", 0);
        favourableness.replace("down", 0);
        favourableness.replace("left", 0);
        favourableness.replace("right", 0);
        //check how favourable up is
        if (getGridY() - 1 >= 0) {
            if (entitygrid[getGridX()][getGridY() - 1] != null) {
                favourableness.replace("up", Integer.MIN_VALUE);
            } else if (target.getGridY() < getGridY()) {
                favourableness.replace("up", 1);
            }
        } else {
            favourableness.replace("up", Integer.MIN_VALUE);
        }
        //check how favourable down is
        if (getGridY() + 1 < entitygrid.length) {
            if (entitygrid[getGridX()][getGridY() + 1] != null) {
                favourableness.replace("down", Integer.MIN_VALUE);
            } else if (target.getGridY() > getGridY()) {
                favourableness.replace("down", 1);
            }
        } else {
            favourableness.replace("down", Integer.MIN_VALUE);
        }
        //check how favourable left is
        if (getGridX() - 1 >= 0) {
            if (entitygrid[getGridX() - 1][getGridY()] != null) {
                favourableness.replace("left", Integer.MIN_VALUE);
            } else if (target.getGridX() < getGridX()) {
                favourableness.replace("left", 1);
            }
        } else {
            favourableness.replace("left", Integer.MIN_VALUE);
        }
        //check how favourable right is
        if (getGridX() + 1 < entitygrid[0].length) {
            if (entitygrid[getGridX() + 1][getGridY()] != null) {
                favourableness.replace("right", Integer.MIN_VALUE);
            } else if (target.getGridX() > getGridX()) {
                favourableness.replace("right", 1);
            }
        } else {
            favourableness.replace("right", Integer.MIN_VALUE);
        }

        //if one direction stands out, move in that direction
        if (favourableness.get("up") > favourableness.get("down") && favourableness.get("up") > favourableness.get("left") && favourableness.get("up") > favourableness.get("right")) {
            moveUp();
        } else if (favourableness.get("down") > favourableness.get("up") && favourableness.get("down") > favourableness.get("left") && favourableness.get("down") > favourableness.get("right")) {
            moveDown();
        } else if (favourableness.get("left") > favourableness.get("up") && favourableness.get("left") > favourableness.get("down") && favourableness.get("left") > favourableness.get("right")) {
            moveLeft();
        } else if (favourableness.get("right") > favourableness.get("up") && favourableness.get("right") > favourableness.get("down") && favourableness.get("right") > favourableness.get("left")) {
            moveRight();
        } else {
            int random = (int) (Math.random() * 2) + 1;
            //if up is equally favourable as sth else
            if (Objects.equals(favourableness.get("up"), favourableness.get("down")) && favourableness.get("up") > favourableness.get("left")) {
                if (random == 1) {
                    moveUp();
                } else {
                    moveDown();
                }
            } else if ((Objects.equals(favourableness.get("up"), favourableness.get("left"))) && favourableness.get("up") > favourableness.get("down")) {
                if (random == 1) {
                    moveUp();
                } else {
                    moveLeft();
                }
            } else if (Objects.equals(favourableness.get("up"), favourableness.get("right")) && favourableness.get("up") > favourableness.get("down")) {
                if (random == 1) {
                    moveUp();
                } else {
                    moveRight();
                }
            } //down
            else if (Objects.equals(favourableness.get("down"), favourableness.get("up")) && favourableness.get("down") > favourableness.get("left")) {
                if (random == 1) {
                    moveDown();
                } else {
                    moveUp();
                }
            } else if (Objects.equals(favourableness.get("down"), favourableness.get("left")) && favourableness.get("down") > favourableness.get("up")) {
                if (random == 1) {
                    moveDown();
                } else {
                    moveLeft();
                }
            } else if (Objects.equals(favourableness.get("down"), favourableness.get("right")) && favourableness.get("down") > favourableness.get("up")) {
                if (random == 1) {
                    moveDown();
                } else {
                    moveRight();
                }
            } //left
            else if (Objects.equals(favourableness.get("left"), favourableness.get("up")) && favourableness.get("left") > favourableness.get("right")) {
                if (random == 1) {
                    moveLeft();
                } else {
                    moveUp();
                }
            } else if (Objects.equals(favourableness.get("left"), favourableness.get("down")) && favourableness.get("left") > favourableness.get("right")) {
                if (random == 1) {
                    moveLeft();
                } else {
                    moveDown();
                }
            } else if (Objects.equals(favourableness.get("left"), favourableness.get("right")) && favourableness.get("left") > favourableness.get("down")) {
                if (random == 1) {
                    moveLeft();
                } else {
                    moveRight();
                }
            } //right
            else if (Objects.equals(favourableness.get("right"), favourableness.get("up")) && favourableness.get("right") > favourableness.get("left")) {
                if (random == 1) {
                    moveRight();
                } else {
                    moveUp();
                }
            } else if (Objects.equals(favourableness.get("right"), favourableness.get("down")) && favourableness.get("right") > favourableness.get("left")) {
                if (random == 1) {
                    moveRight();
                } else {
                    moveDown();
                }
            } else if (Objects.equals(favourableness.get("right"), favourableness.get("left")) && favourableness.get("right") > favourableness.get("down")) {
                if (random == 1) {
                    moveRight();
                } else {
                    moveLeft();
                }
            }
        }
    }

    public void eat() {
        //eat code
        if (hunger - 10 > 0)
            hunger -= 10;
        else
            hunger = 0;
    }

    public void drink() {
        if(thirst - 10 > 0)
            thirst -= 10;
        else
            thirst = 0;
    }

    public void mate() {
        //mate code
    }
}
