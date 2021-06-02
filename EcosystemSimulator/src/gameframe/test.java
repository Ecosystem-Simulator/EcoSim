/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 816491
 */
public class test {
    public static void longestStreak(String str){
	int streak = 1;
	int longest = 0;
	String letter = str.substring(0, 1);
	for(int k = 1; k < str.length(); k ++){
		if (str.substring(k-1, k).equals(str.substring(k, k+1))){
		streak ++;
                }
		else{
			streak = 1;
		}
		if (longest < streak){
			longest = streak;
			letter = str.substring(k, k+1);
		}
	}
	System.out.println(letter + " " + longest);


    }
}
