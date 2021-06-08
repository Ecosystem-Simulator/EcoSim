
package gameframe;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Bear extends Animal{
    public Bear(int x, int y, ArrayList<Entity> entities, String gender, Entity[][] entitygrid, int gridLength){
        super(x, y, entities, gender, entitygrid, gridLength);
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(105, 57, 9));
        g.fillOval(getX() - getLength(), getY() - getHeight(), getLength()*2, getHeight()*2);
    }
    
    @Override
    public void act(){
        /*if 
        if age > 1000 then die
        hunger > 100 then die
        if hunger > 25, look for food, distanceTo
        if thirst > 100 then die
        if thirst > 25, look for water,
        mating, distanceTo (opposite gender, //higher attractiveness if same distance)
        */
    }
}
