
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class Challenge8_UniCodeNumbers {
 
    public static void main(String[] args) throws IOException{
        
        //We format the string to eliminate all whitespaces on the sides, even special ones (using \h, JDK8)
        //Then, we create the BigInteger and print it on Hex
        
        BufferedReader br = new BufferedReader(new FileReader("input8.txt"));
        FileWriter writer = new FileWriter("output8.txt");

        //Number of cases
        int cases = Integer.parseInt(br.readLine());


        for(int caseNumber = 1; caseNumber<=cases; caseNumber++){
            writer.append("Case #"+caseNumber+": "+convert(br.readLine())+"\n");
            System.out.println(caseNumber);

        }

        writer.close();
        br.close();

    }
            
    public static String convert(String fuente) {
        try{
            String a=fuente;
            a= a.replaceAll("(^\\h*)|(\\h*$)","");
            return (new BigInteger(a)).toString(16);
        }catch(NumberFormatException exc){
            return "N/A";
        }
    }

}