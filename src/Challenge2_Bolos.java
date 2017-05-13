import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Challenge2_Bolos {

	public static void main(String[] args) throws IOException{

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			//Number of cases
				int cases = Integer.parseInt(br.readLine());
				
			for(int caseNumber = 1; caseNumber<=cases; caseNumber++){
			
				int rolls= Integer.parseInt(br.readLine()); //Line with people number.
				int[] frameScore = new int[10];
				
				//We don't need the array of rolls, we just split the array and work with it.
				String[] split = br.readLine().split(" ");
				
				//Flag to see if it's second roll.
				boolean isSecondRoll= false;
				
				int totalScore=0;
				int currFrameScore = 0;
				int currFrame =0;
				
				for(int index=0; currFrame<10; index++){
					//We first get the score of the roll
					currFrameScore += Integer.parseInt(split[index]);
					
					//If it sums 10, we check if it was first or second shot. Then we sum the next shots.
					if(currFrameScore==10){
						//If it's first shot, is a strike.
						if(!isSecondRoll){
							currFrameScore+= Integer.parseInt(split[index+1]) + Integer.parseInt(split[index+2]);
						}else{
							//It's a Spare
							currFrameScore+= Integer.parseInt(split[index+1]);
						}
						//We finish the frame inserting the score. Then we reset the variables for a new frame.
						totalScore += currFrameScore;
						frameScore[currFrame]=totalScore;
						
						currFrame++;
						currFrameScore=0;
						isSecondRoll=false;
						
					}else{
						//If it doesn't sum 10, we just have to see if it was the second shot. If it was, we reset the frame.
						if(!isSecondRoll){
							isSecondRoll=true;
						}else{
							//We finish the frame inserting the score. Then we reset the variables for a new frame.
							totalScore += currFrameScore;
							frameScore[currFrame]=totalScore;
							currFrame++;
							currFrameScore=0;
							isSecondRoll=false;
						}
					}

				}

				StringBuffer res = new StringBuffer("Case #"+caseNumber+":");
				
				for(int numberFrame=0; numberFrame<frameScore.length; numberFrame++){
					//We format our output.
					res.append(' ').append(frameScore[numberFrame]);
				}
				
				
				System.out.println(res);
			
			}
				
		}

	

}
