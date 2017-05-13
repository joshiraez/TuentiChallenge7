import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Challenge3_BoardGames {
	public static void main (String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Number of cases
		int cases = Integer.parseInt(br.readLine());
			
		for(int caseNumber = 1; caseNumber<=cases; caseNumber++){
		
			System.out.println("Case #"+caseNumber+": "+numCartas(Integer.parseInt(br.readLine())));
		}

	}
	
	public static int numCartas(int maxNum){
		/*We only need to know his size in bits
		 * Because a bit interpretation of a number is nothing more than a sum of different numbers. (Powers of 2)
		 * We can assure that if a number is x bits long, it can be sum with that number of cards (these being powers of 2).
		 * Example: 7 = 111 - 3 bits === 3 cards: 4 + 2 + 1.
		 */
		if( maxNum == 0 )
	        return 0; // or throw exception
	    return 32 - Integer.numberOfLeadingZeros( maxNum );
	}
}
