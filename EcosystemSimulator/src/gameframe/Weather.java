package gameframe;

import java.util.ArrayList;

public class Weather extends Entity{
    
    public Weather(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength ){
        super(gridX, gridY, entities, entitygrid, gridLength);
    }
    //test
    static ArrayList<Integer> waterCoordinatesR = new ArrayList(); 
    static ArrayList<Integer> waterCoordinatesC = new ArrayList();
    private static String weather = ""; 
    private static String yesterdayWeather = "";
    private static int weatherCounter = 1;
    private static boolean day = true;
    private static boolean restrictedVision;
    
    public static void chooseWeather(){
        int chance = (int)(Math.random()*10)+1;
        //70% chance of sun, 30% chance of rain
        if (weatherCounter < 2){
            if (chance <= 5){
                weather  = "sunny";      
            }
            else{ 
                weather = "rainy";    
            }
            //check how many days in a row of the same weather
            if (weather.equals(yesterdayWeather)){
                weatherCounter++;
            }
            else{
                weatherCounter = 1; 
            }
        }
        //7 days in a row of the same weather brings natural disasters
        else if (yesterdayWeather.equals("rainy")){
           weather = "flood";      
        }
        //There's a 30% chance of flood after a day of flood
        else if (yesterdayWeather.equals("flood")){
            int x = (int)(Math.random()*10)+1;
            if (x <= 8){
                weather = "flood";
            }
            //Otherwise, weather goes back to normal, and counter = 1
            else{
                weatherCounter = 1;
                    weather = "sunny";
                }
            }
        else if(yesterdayWeather.equals("sunny")){
            weather = "drought";
        }
        //There's a 80% chance of drought after a day of drought
        else if (yesterdayWeather.equals("drought")){
            int a = (int)(Math.random()*10)+1;
            if (a <= 8){
                weather = "drought";
            }
            //Otherwise, weather goes back to normal, and counter = 1
            else{
                weatherCounter = 1;
                    weather = "rainy";
            }
        }
        //set yesterday's weather
        yesterdayWeather = weather;      
    } 
    
    public static void draw(){   
    }
    
    public static String getWeather(){
        return (weather);
    }
    
    public static void rain(){
        //add water
        for (int k = waterCoordinatesR.size() - 1; k >= 0; k--){
            Water wat = new Water(waterCoordinatesR.get(k), waterCoordinatesC.get(k), GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
            waterCoordinatesR.remove(k);
            waterCoordinatesC.remove(k);
        }   
    }
    
    public static void sun(){      
        for (int k = 0; k < GameFrame.entities.size(); k++){
           if (GameFrame.entities.get(k) instanceof FloodWater){
               GameFrame.entities.get(k).die();
           }
        }
    }
    
    public static boolean time(){
        day = !day;
        return(day);
    }
    //FLOOD NOT WORKING: after running for a few minutes, it starts to miss several spots
    public static void flood(){
        for(int r = 0; r < GameFrame.entitygrid.length; r++ ){
            for (int c = 0; c < GameFrame.entitygrid.length; c++){ 
                if (GameFrame.entitygrid[r][c] instanceof Water && !(GameFrame.entitygrid[r][c] instanceof FloodWater)){
                    //left
                    if (r - 1 > -1 && !(GameFrame.entitygrid[r-1][c] instanceof Water)){
                        if (GameFrame.entitygrid[r-1][c] instanceof Entity){
                            GameFrame.entitygrid[r-1][c].setActive(false);
                            GameFrame.entitygrid[r-1][c].die();
                        }
                        FloodWater fl = new FloodWater(r-1, c, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                    //up
                    if (c - 1 > -1 && !(GameFrame.entitygrid[r][c-1] instanceof Water)){ 
                        if (GameFrame.entitygrid[r][c-1] instanceof Entity){
                            GameFrame.entitygrid[r][c-1].setActive(false);
                            GameFrame.entitygrid[r][c-1].die();
                        }
                        FloodWater fl = new FloodWater(r, c-1, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                    //right
                    if (r + 1 < GameFrame.entitygrid.length && !(GameFrame.entitygrid[r+1][c] instanceof Water)){
                        if (GameFrame.entitygrid[r + 1][c] instanceof Entity){
                            GameFrame.entitygrid[r+1][c].setActive(false);
                            GameFrame.entitygrid[r + 1][c].die();
                        }
                        FloodWater fl = new FloodWater(r + 1, c, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                    //down
                    if (c + 1 < GameFrame.entitygrid[0].length && !(GameFrame.entitygrid[r][c+1] instanceof Water)){
                       if (GameFrame.entitygrid[r][c+1] instanceof Entity){
                           GameFrame.entitygrid[r][c+1].setActive(false);
                            GameFrame.entitygrid[r][c+1].die();
                        }
                        FloodWater fl = new FloodWater(r, c+1, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                }
            }
        }
    }
    //Drought not working, after a few minutes, it pauses after 3-4 drought days
    public static void drought(){
        for(int r = 0; r < GameFrame.entitygrid.length; r++){
            for (int c = 0; c < GameFrame.entitygrid[0].length; c++){
                if (GameFrame.entitygrid[r][c] instanceof Water){
                    int rand = (int)(Math.random()*10)+1;
                    if (rand >= 5){
                        waterCoordinatesR.add(r);
                        waterCoordinatesC.add(c);
                        GameFrame.entitygrid[r][c].die();
                    }
                }
            }
        }
    }
}

