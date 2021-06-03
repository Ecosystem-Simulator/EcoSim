
public class test {
    
   String weather = ""; 
   String yesterdayWeather = "";
   int weatherCounter = 0;
    
    
    public void chooseWeather(){
       int chance = (int)(Math.random()*10)+1;
       if (weatherCounter < 7)
            if (chance <= 7){
                weather  = "sunny";
                if (weather.equals(yesterdayWeather)){
                    weatherCounter++;
                }
                else{
                    weatherCounter = 1;
                }
                yesterdayWeather = weather;
            }
            else{ 
                weather = "rainy";
            }
       else if (yesterdayWeather.equals("rainy")){
           weather = "flood";
           yesterdayWeather = weather;
           
       }
       else if (yesterdayWeather.equals("flood")){
            int x = (int)(Math.random()*10)+1;
            if (x <= 3){
                weather = "flood";
            }
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
           yesterdayWeather = weather; 
       }
       else if(yesterdayWeather.equals("sunny")){
           weather = "drought";
           yesterdayWeather = weather;
       }
       else if (yesterdayWeather.equals("drought")){
           int a = (int)(Math.random()*10)+1;
           if (a <= 3){
               weather = "drought";
           }
           else{
               weatherCounter = 1;
               int r = (int)(Math.random()*10)+1;
               if (r<=7){
                   weather = "sunny";
               }
               else{
                   weather = "rainy";
               }
           }
           yesterdayWeather = weather;
       }
           
    }   
}
