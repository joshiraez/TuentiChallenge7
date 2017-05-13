import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap; 


//Autor: José Ráez
public class Challenge6_TheTower {
    public static void main(String[] args) throws IOException{

            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            FileWriter writer = new FileWriter("output.txt");

            //Number of cases
            int cases = Integer.parseInt(br.readLine());


            for(int caseNumber = 1; caseNumber<=cases; caseNumber++){
                writer.append("Case #"+caseNumber+": "+baamStory(br)+"\n");
                System.out.println(caseNumber+" done");

            }

            writer.close();
            br.close();

    }

    private static BigInteger baamStory(BufferedReader br) throws IOException{
        //Used for gathering data
        String[] split;

        //We read the tower composition.
        split = br.readLine().split(" ");
        long maxFloors = Integer.parseInt(split[0]);
        int numShortcuts = Integer.parseInt(split[1]);

        //6th version. Yey. We will use Two tree maps. First One has the shortcuts.
        //Second one has the shortest ways to a floor... in a diff manner.

        //Note that is <DestinyFloor <OriginFloor, Time>>
        TreeMap<Long, TreeMap<Long,Long>> shortcutsToFloor = new TreeMap();

    //Now we read the shortcuts. We make a loop until we read all the shortcuts.
       for (int shortcut=1; shortcut<=numShortcuts; shortcut++){
           //Data in each shortcut: originfloor, destiny floor, time
           split = br.readLine().split(" ");
           long originFloor = Integer.parseInt(split[0]);
           long destinationFloor = Integer.parseInt(split[1]);
           long testTime = Integer.parseInt(split[2]);

           //We put it in the treeMap.

           //First we check if there is already a TreeMap for that floor (are there shortcuts?)
           //if there is, we put it. //Else, we create it and put it there.
           TreeMap<Long, Long> floor = shortcutsToFloor.get(destinationFloor);
           if(floor==null){
               floor=new TreeMap<Long,Long>();
               floor.put(originFloor, testTime);
               shortcutsToFloor.put(destinationFloor, floor);

           }else{
               //The treeMap will sort it by floow on its own.
               //Also, we have to check if there is another path from the same origin.
               //In that case, we just take the smallest time.
               if(shortcutsToFloor.get(destinationFloor).containsKey(originFloor)){
                   shortcutsToFloor.get(destinationFloor).put(originFloor, Math.min(floor.get(originFloor), testTime));
               }else{
                   shortcutsToFloor.get(destinationFloor).put(originFloor,testTime);
               }
           }
       }//Notice that there can be 2500 shortcuts at max stored on the TreeMap. It shouldn't give us memory problems.

        //Now we initilize a TreeMap with which we can calculate the minimum path.
        //We will save, along the time, if the rest of floor are the same, or they are ascended normally.
        //We use two longs to avoid further complicating the program, although it should be a long/boolean.
        TreeMap<Long, BigInteger[]> minimunWayToFloor = new TreeMap();
        minimunWayToFloor.put(1L, new BigInteger[] {BigInteger.ZERO, BigInteger.ZERO}); 
        //We use the second 0 for false (shortcut ->rest of floors the same) or 1 if not shortcut -> calculate floors

        //Now we read the shortcuts. We make a loop until we read all the shortcuts.
        //Because is a TreeMap, we can be sure that these will be get out in the floor order.
        //Floor without shortcuts can be asummed to just be ascended normally (until we get a better shortcut to it).
        for(Entry<Long,TreeMap<Long,Long>> shortcutsMap : shortcutsToFloor.entrySet() ){
            //For each floor with shortcuts going to it, we iterate throught them and get the minimum way.
            long reachingFloor = shortcutsMap.getKey();
            BigInteger normalTime = pathTime(reachingFloor, minimunWayToFloor); //We use this to store the normal way. It must be beaten.

            //Now we initialize those 2 variables at the first shortcut. We use them to dispose
            //of further shortcuts with worse times than early shortcuts. They will always be bad.
            long minShortcut = shortcutsMap.getValue().firstEntry().getValue();
            BigInteger minPath = BigInteger.valueOf(minShortcut).add(pathTime(shortcutsMap.getValue().firstKey(), minimunWayToFloor));
            //Now we iterate through everyone. Note that we waste one iteration because we already checked the first key. Meh.
            //Could be better using an iterator and a while, but i'm behind in time as of now.
            for(Entry<Long,Long> shortcut: shortcutsMap.getValue().entrySet()){
                if(shortcut.getValue()<minShortcut){
                    minShortcut = shortcut.getValue();
                    //Now we check if it also beats the minPath
                    BigInteger thisShortcutTime = BigInteger.valueOf(shortcut.getValue()).add( pathTime(shortcut.getKey(), minimunWayToFloor));
                    if(thisShortcutTime.compareTo(minPath) < 0){
                        minPath=thisShortcutTime;
                    }
                }
            }

            //After that, we have to check if it beats the normalTime. If it doesn't no action is taken.
            if(minPath.compareTo(normalTime)<0){
                //If it is, we have to make a entry indicating the new time thanks to the shortcut.
                minimunWayToFloor.put(reachingFloor, new BigInteger[] {minPath, BigInteger.ZERO});

                //We also check if this path, somehow, beats the path to previous floor. 
                //If it does, we will make an entry for the last floor this path is beneficial to the time
                //That is: It would be better to get the shortcut and then descend for the best time.
                if(minPath.compareTo(pathTime(reachingFloor-1, minimunWayToFloor))<0){
                    long downToFloor=reachingFloor-1;
                    minimunWayToFloor.remove(downToFloor);
                    while(minPath.compareTo(pathTime(downToFloor-1, minimunWayToFloor))<0){
                        downToFloor--;
                        minimunWayToFloor.remove(downToFloor);
                        
                    }
                    //It will end in the last floor with bigger time than minPath.
                    //We put an entry indicating that this is a "shortcut" and every path up to next entry
                    //have the same time.
                    minimunWayToFloor.put(downToFloor, new BigInteger[] {minPath, BigInteger.ONE});
                }
            }
        }

        //Last, we return the path time to the last floor.
        return pathTime(maxFloors, minimunWayToFloor);

    }

    private static BigInteger pathTime(long toFloor,  TreeMap<Long, BigInteger[]> pathMap){
        //Index 0 : time to that point
        //Index 1 : if the floors up to "toFloor" share the same time (shortcut): 1
        //          if the floors up to "toFloor" are ascended with normal test: 0
        Entry<Long, BigInteger[]> pathData = pathMap.floorEntry(toFloor);
        
        if(pathData.getValue()[1].compareTo(BigInteger.ONE)==0){
            return pathData.getValue()[0];
        }else{
            return pathData.getValue()[0].add(normalPath(pathData.getKey(), toFloor));
        }
    }
    
    private static BigInteger normalPath(long fromFloor, long toFloor){
        //The normal path time is a arithmetic progression. We can calculate the time.
        return BigInteger.valueOf(((toFloor-fromFloor)*(toFloor+fromFloor-1))/2);
    }
}