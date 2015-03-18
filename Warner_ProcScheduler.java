/**
* @author Logan Warner
* @description Simulation of running 100 processes with a given limit (called 
* quanta) on how much each can run continuously. Each process has a time needed
* between 10 and 90 and they arrive on average every 10 time units. 15 time 
* units are spent context-switching whenever a process completes or is
* interrupted.
*/

import java.util.ArrayDeque;
import java.util.HashMap;

public class Warner_ProcScheduler{
  private static ArrayDeque<Warner_Process> processList = 
    new ArrayDeque<Warner_Process>();
  private static HashMap<Warner_Process, Integer> remProcTimes = 
    new HashMap<Warner_Process, Integer>();
  private static int numProcs;
  private static int quantaAmount;
  private static int curTime = 1;
  private static int overheadTime = 0;
  private static int totalTurnaroundTime = 0;
  private static int totalWaitTime = 0;
  private static boolean verboseMode = false;
  
  public static void main(String[] args){
    if(!validateArguments(args))
      return;

    populateProcesses();
    
    while(processList.size() > 0){
      Warner_Process curProc = processList.removeFirst();
      if(curProc.getArrivalTime() <= curTime){
        int timeLeft = remProcTimes.get(curProc);
        int i;//need to keep this around for quanta advancement
        
        for(i = 0; timeLeft > 0; i++, timeLeft--){
          if(i == quantaAmount && timeLeft != 1){//hit quanta and process not done
            if(verboseMode)
              System.out.printf("Quanta hit with %d time left.\n", timeLeft);
            processList.addLast(curProc);//put process into end of queue
            break;
          }//if hit time limit and process not done
          if(timeLeft == 1){ 
            totalTurnaroundTime += curTime + i;
            totalTurnaroundTime -= curProc.getArrivalTime();
            totalWaitTime += curTime + i;
            totalWaitTime -= curProc.getTimeNeeded() + curProc.getArrivalTime();
          }
        }//for execute next process in queue
        //store process in queue and add time for the overhead
        switchContext(curProc, timeLeft);
        //Advance time by amount spent running process
        curTime += i;
      }else{
        curTime++;
      }//ifelse
    }//while there are processes to run
    System.out.printf("Time spent on processes: %d\n", curTime - overheadTime);
    System.out.printf("Time spent on overhead: %d\n", overheadTime);
    System.out.printf("Average wait time: %d\n", totalWaitTime/numProcs);
    System.out.printf("Average turnaround time: %d\n",
      totalTurnaroundTime/numProcs);
  }//Main
  
  static void populateProcesses(){
    for(int i = 0, curArrivalTime = 0; i < numProcs; i++){
      Warner_Process curProc = new Warner_Process(curArrivalTime);
      processList.addLast(curProc);//Add in order of when they start
      remProcTimes.put(curProc, curProc.getTimeNeeded());
      curArrivalTime += (int)(Math.random()*20);
    }//for populate processList
  }//populateProcesses
  
  static boolean validateArguments(String[] argsIn){
    if(argsIn.length < 1 || argsIn.length > 3){
      System.out.println("Usage: java Warner_ProcScheduler" + 
        "<integer time quanta> <integer number of processes> [verbose]");
      return false;
    }//if not one or two input arguments
    
    if(argsIn.length >= 2)
      try{
        numProcs = Integer.parseInt(argsIn[1]);
      }catch(NumberFormatException nfe){System.out.println("ERROR:cannot " +
        "parse specified process amount as an integer."); return false;};
    
    if(argsIn.length == 3 && argsIn[2].equalsIgnoreCase("verbose"))
      verboseMode = true;
    
    try{
      quantaAmount = Integer.parseInt(argsIn[0]);
    }catch(NumberFormatException nfe){System.out.println("ERROR:Cannot parse" +
      " specified time quanta as an integer."); return false;};
    
    if(quantaAmount <= 0){
      System.out.printf("ERROR:Quanta is too small (given %d).\n",
        quantaAmount);
      return false;
    }//if quanta too small
    
    if(numProcs <= 0){
      System.out.printf("ERROR:Number of processes is too small (given %d).\n",
        numProcs);
      return false;
    }//if quanta too small
    
    return true;
  }//validateArguments
  
  static void switchContext(Warner_Process procIn, int timeLeftIn){
    curTime += 15;
    overheadTime += 15;
    //update the remaining time of the process (cannot simply edit map entry)
    remProcTimes.remove(procIn);
    if(processList.contains(procIn))
      remProcTimes.put(procIn, timeLeftIn);
    
    if(verboseMode)
      System.out.printf("Just switched context; time is %d.\n", curTime);
  }//switchContext
}//Warner_ProcSchedDriver