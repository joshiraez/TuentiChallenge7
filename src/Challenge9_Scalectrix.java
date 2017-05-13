import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Challenge9_Scalectrix {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		FileWriter writer = new FileWriter("output.txt");
		
		long cases = Long.parseLong(br.readLine());
		for(long numCase=1; numCase<=cases; numCase++){
			writer.append("Case #"+numCase+": "+maxCircuito(br.readLine())+"\n");
		}
		
		writer.close();
		br.close();
	}
	
	public static int maxCircuito(String piezas){
		
		
		//The strategy is to make the most flat scalectrix. All constructions here are bound to be selfcontaining, 
		//meaning, they can be agregated to any scalectrix without interfering it, from a base of 4 curves.
		
		//We used "paired" constructs: constructions that can on both sides of the track without being offset at all.
		
		/*
		 * 4C needed for a race (to turn 360º)
		 * 
		 * ╔ (rest goes between)   ╗
		 * ╚                       ╝
		 * 
		 * 8C 1D - Fix to the program. Special case that gives us 1 piece
		 *       - Because 4C is basically a 1D, we should make sure every
		 *         place that uses 1D only has a case for x+4C (unless it's 8C or 2D, they pair on themselves.)
		 *         It doesn't work with 2R because we use R along C when offseting those pesky S. And they
		 *         only offset by 1 in both directions.
		 * ╔══
		 * ╝ ╔
		 * ╗╔╝
		 * ╚╝
		 * 
		 * 8C
		 *  ╔╗
		 * 	╝╚
		 * 	╗╔
		 * 	╚╝
		 * 
		 *  6C 2R - Fix over D1 C2 R2, where we substitute the D for the C.
		 *  		As in other cases, the 2C > 1D (2 pieces against one)  
		 * 	   ╔╗
		 *   ┌ ╝╚ ┐  
		 *   ║  ╔ ┘
		 * 	 └ ═╝
		 *  
		 * 
		 * 4C 2D
		 * ╔══
		 * ╝
		 *   ╔
		 * ══╝
		 * 
		 * 4C 1D --- Also we have  (For example, this would just be 8C like the second one, no need to fix)
		 *  ══
		 *  ╗╔
		 * 	╚╝
		 *  
		 *  R2 C4 (similar to D version)
		 *  
		 *  ╔═
		 *  ╝
		 *   ╔
		 * 	═╝
		 *  
		 *  D1 C2 R2 (this and the next are the strangest, we use the offset created by 2C) 
		 *  			(shown in examples, Thanks for the tip!!)
		 *  			(curves in initial 4C thinned out)
		 *  
		 *   ┌ ══ ┐  
		 *   ║  ╔ ┘
		 * 	 └ ═╝
		 *  
		 *  C2 R2
		 *  
		 *   ┌ ═ ┐  
		 *   ║ ╔ ┘
		 * 	 └ ╝
		 *  
		 *  D1 R2
		 *  
		 *  ══
		 *  ══
		 *  
		 *  
		 *  And R2 you can imagine it.
		 *  
		 *  They are also in terms of pieces saved. Plus, thanks to the parity, we can work assuming the pieces left are
		 *  the module of the pair.
		 *  
		 *  We just have to use a big if looking what structures can be made and sum the pieces.	
		 *  
		 *  Note: These must be ordered carefully, considering number of pieces saved and having the bigger requeriments first
		 *  For example: 4C requeriments MUST be before 2C
		 *  D is the only one that gets out of the norm, because you can make things with 1D, whereas you can NOT
		 *  with 1R or 1C
		 *  	
		 */
		
		String[] split = piezas.split(" ");
		
		int rect = Integer.parseInt(split[0]);
		int curves = Integer.parseInt(split[1]);
		int dbl = Integer.parseInt(split[2]);
		int size =0;
		
		int auxDblCount;
		int auxRectCount;
		
		
		//If there are not 4 curves for a 360º, we are done.
		if(curves>=4){
			//Put curves
			size=4;
			curves-=4;
			
			//8C 1D 
			//We should only use it as a means of using an uneven D.
			if(curves>=8 && dbl%2==1){
				curves-=8;
				dbl-=1;
				size+=9;
			}
			
			//8C
			size+=(curves/8)*8;
			curves=curves%8;
			
			//Just after it, the fix to get 6C 2R is a transformation from 1D 2C 2R.
			//If you are wondering: 4C 4R = 4C 2R + 2R so no problem. Try other combinations.
			if(curves>=6 && rect>=2){
				curves-=6;
				rect-=2;
				size+=8;
			}
			
			
			//1D 4C 
			//We put this first because it doesn't interfere with D2 C4 (D2 is valid)
			//or 1D 2C 2R (2C 2R is still valid)
			if(curves>=4){
				if(dbl%2==1){
					dbl-=1;
					curves-=4;
					size+=5;
				}else{
					//D2 C4
					if(dbl>=2){
						dbl-=2;
						curves-=4;
						size+=6;
					}
				}
			}
			
			//2D - We still have to do the module, we might had dbl%2==1 and curves <4
			size+=(dbl/2)*2;
			dbl=dbl%2;
			
			//R2 C4. Putting it before C2 requeriments
			if(curves>=4 && rect>=2){
				curves-=4;
				rect-=2;
				size+=6;
			}
			
			//2R 2C 1D
			if(dbl==1 && curves>=2 && rect>=2){
				dbl-=1;
				curves-=2;
				rect-=2;
				size+=5;
			}
			
			//2C 2R Goes before the last 1D case because we win one more piece.
			//Now i'm wondering I could just pair 2C 2R and forget about 4C 2R.
			//But remembering they play with an offset, i would avoid that and stick
			//to the structure that works.
			if(curves>=2 && rect>=2){
				curves-=2;
				rect-=2;
				size+=4;
			}
			
			//2R 1D 
			if(dbl==1 && rect>=2){
				dbl-=1;
				rect-=2;
				size+=3;
			}
			
			//2R, finally
			size+=(rect/2)*2;
			rect=rect%2; //Unused but well.
			
			
		}
		
		//Will be 0 if there are no 4C
		return size;
	}
}
