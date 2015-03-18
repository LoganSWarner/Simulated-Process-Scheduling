/**
* @author Logan Warner
* @description simple class representing a process that needs a certain amount
* of time between 10 and 90 on the CPU
*/

class Warner_Process{
  private int arrivalTime;
  private int timeNeeded = 10 + (int)(Math.random()*80);//10 to 90
  
  Warner_Process(int arrivalTime){
    this.arrivalTime = arrivalTime;
  }//constructor
  
  int getTimeNeeded(){
    return timeNeeded;
  }//getTimeNeeded
  
  int getArrivalTime(){
    return arrivalTime;
  }//getArrivalTime
}//Process
