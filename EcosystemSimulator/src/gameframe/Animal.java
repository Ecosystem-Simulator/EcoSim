package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;

public class Animal extends Entity {
    //1 is male, 2 is female
    private int gender;
    private int age;
    private int hunger;
    private int thirst;
    private int reproductiveUrge;
    private boolean restrictedVision;
    protected Entity target;
    public HashMap<String, Integer> favourableness = new HashMap();
    private ArrayList<String> moves = new ArrayList();
    private Deer d;

    public Animal(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        gender = (int)(Math.random()*2)+1;
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
        if (getAge() > 1000 || getHunger() > 100 || getThirst() > 100){
            die();
        }
        if (getThirst() >= getHunger() && getThirst() >= 25){
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
        }
        if(this instanceof Bear || this instanceof Wolf){
            if(distanceTo(getNearestDeer()) == 1){
                target = getNearestDeer();
            }
        }
        
        if (distanceTo(target) > 1 && target != null) {
            move();
        } 
        else if (target instanceof Water) {
            drink();
        }
        else if (target instanceof Food || target instanceof Deer){
            eat(target);
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

    public void eat(Entity f) {
        //eat code
        if(f instanceof Food){
            if (hunger - ((Food)f).getNutritionVal() > 0)
                hunger -= ((Food)f).getNutritionVal();
            else
                hunger = 0;
        }
        else if(f instanceof Deer){
            if(hunger - 30 > 0)
                hunger -= 30;
            else
                hunger = 0;
        }
        f.die();
    }

    public void drink() {
        if(thirst - 10 > 0)
            thirst -= 10;
        else
            thirst = 0;
    }

    public void mate(Animal m) {
        resetReproductiveUrge();
        m.resetReproductiveUrge();
        if (this instanceof Bear){
            if (entitygrid[getX() + 1][getY()] == null){
                Bear bear = new Bear()
            }
        }
        
    }
    public Deer getNearestDeer(){
        int minDist = Integer.MAX_VALUE;
        for(Entity e : entities){
            if(e instanceof Deer && distanceTo(e) < minDist){
                d = (Deer)e;
            }
        }
        return d;
    }
}
