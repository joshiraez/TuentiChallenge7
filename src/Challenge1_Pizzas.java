import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Challenge1_Pizzas {
	public static void main (String[] args){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Number of cases
		try{
			int cases = Integer.parseInt(br.readLine());
			
			for(int caseNumber = 1; caseNumber<=cases; caseNumber++){
				try{
					int people= Integer.parseInt(br.readLine()); //Line with people number.
					
					//We don't need the arrays. We just need to sum the hunger of the people.
					long howMuchHungry=0l;
					
					if(people!=0){
						String[] split = br.readLine().split(" ");
						
						for(int index=0; index< split.length; index++){
							try{
								howMuchHungry += Integer.parseInt(split[index]);
							}catch(NumberFormatException exc){
								//Trash input
							}
						}
						
					}
					
					long pizzas = howMuchHungry/8;
					if(howMuchHungry%8!=0){ pizzas++;}
					
					System.out.println("Case #"+caseNumber+": "+pizzas);
				}catch(NumberFormatException exc){
					//Trash input
				}
			}
			
		}catch(NumberFormatException exc){
			//Thrash input.
		}catch(IOException exc){
			//Program Crash
		}
	}
}
