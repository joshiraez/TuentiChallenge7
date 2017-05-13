import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


public class Challenge4_Pythagoras {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		FileWriter writer = new FileWriter("output.txt");
		
		//Number of cases
		int cases = Integer.parseInt(br.readLine());
			
		for(int caseNumber = 1; caseNumber<=cases; caseNumber++){
			int minTriangle = minTriangle(br.readLine());
			if(minTriangle!=0){
				writer.append("Case #"+caseNumber+": "+minTriangle+"\n");
			}else{
				writer.append("Case #"+caseNumber+": IMPOSSIBLE\n");
			}
		}
		
		
		writer.close();
		br.close();
		
	}
	
	public static int minTriangle(String sides){
		String entrada = sides;
		entrada = entrada.substring(entrada.indexOf(' ')+1);
		
		//We split the sides into an String array
		String[] sidesSplit = entrada.split(" ");
	
		//We pass it to an int array
		int[] intArray = new int[sidesSplit.length];
		for(int i = 0; i < sidesSplit.length; i++) {
		    intArray[i] = Integer.parseInt(sidesSplit[i]);
		}
		
		//We order it.
		Arrays.sort(intArray);
		
		//Now we have an array sorted in ascending order. 
		
		/*The 3 first sides would be the shortest triangle if IT'S VALID
		If it's not valid, it's because the third number > sum of the other 2.
		The other 2 are implicit because of the sort: a<= b <= c. (a+c > b <=> c >= b and b+c > a <=> c > a) 
		And because all the other numbers will be higher, there is no point on checking them, they wont be valid.

		*/ 
		
		boolean triangleFound=false;
		int sum =0; //To find the minimum
		int indexA=0; //Start at -1 to be able to update data at the beginning of the array 
		int indexPair=1;
		
		//We stop until we get to a number higher than our sum and not 0, or at the limit of the array.
		while(indexA+2<intArray.length && (sum==0 || intArray[indexA]<sum)){
			//We look for the smallest triangle for that number
			while(!triangleFound &&indexPair+1<intArray.length){
				
				triangleFound = intArray[indexA] > intArray[indexPair+1] - intArray[indexPair];
				if(!triangleFound){
					indexPair++;
				}
			}
			//If found, we see if it's lower than the others found.
			if(triangleFound){
				if(sum!=0){
					sum = Math.min(sum,  intArray[indexA]+intArray[indexPair]+intArray[indexPair+1]);
				}else{
					sum = intArray[indexA]+intArray[indexPair]+intArray[indexPair+1];
				}
			}
			//Now we check the next. 
			indexA++;
			indexPair =indexA+1;
			triangleFound = false;
		
		}
		
		return sum;
	}
}
