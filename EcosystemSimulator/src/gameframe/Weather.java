package gameframe;
public class Weather {
    
    //test
    String weather = ""; 
    String yesterdayWeather = "";
    int weatherCounter = 1;
    
    public void chooseWeather(){
        int chance = (int)(Math.random()*10)+1;
        //70% chance of sun, 30% chance of rain
        if (weatherCounter < 7){
            if (chance <= 7){
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
            if (x <= 3){
                weather = "flood";
            }
            //Otherwise, weather goes back to normal, and counter = 1
            else{
                weatherCounter = 1;
                int c = (int)(Math.random()*10)+1;
                if (c <= 7){
                    weather = "sunny";
                }
                else{
                    weather = "rainy";
                }
            }
        }
        else if(yesterdayWeather.equals("sunny")){
            weather = "drought";
        }
        //There's a 30% chance of drought after a day of drought
        else if (yesterdayWeather.equals("drought")){
            int a = (int)(Math.random()*10)+1;
            if (a <= 3){
                weather = "drought";
            }
            //Otherwise, weather goes back to normal, and counter = 1
            else{
                weatherCounter = 1;
                int r = (int)(Math.random()*10)+1;
                if (r <= 7){
                    weather = "sunny";
                }
                else{
                    weather = "rainy";
                }
            }
        }
        //set yesterday's weather
        yesterdayWeather = weather;      
    } 
    
    public void rain(){
        //floodwater goes away
        //tint blue
        //growth++    
    }
    
    public void sun(){
        //floodwater goes away
        //no tint
    }
    
    public void day(){
        //no tint
    }
    
    public void night(){
        //tint grey?
        //vision = vision-1;
    }
    
    public void flood(){
        //if water block + 1 pixel left, right, up, down != water, make those blocks floodwater. 
    }
    
    public void drought(){
        //tint world yellow   
    }
}

