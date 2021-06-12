package gameframe;


public class Camera{
    private int x_center;
    private int y_center;
    public Camera(){
        x_center = 400;
        y_center = 400;
    }
    
    public void incrementX(){
        x_center += 10;
    }
    
    public void incrementY(){
        y_center += 10;
    }
    
    public void decrementX(){
        x_center -= 10;
    }
    
    public void decrementY(){
        y_center -= 10;
    }
    
    public int getX(){
        return(x_center);
    }
    
    public int getY(){
        return(y_center);
    }
}
