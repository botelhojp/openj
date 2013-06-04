package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendTask
* @author ontology bean generator
* @version 2013/03/31, 15:37:40
*/
public class SendTask implements AgentAction {

   /**
* Protege name: task
   */
   private Task task;
   public void setTask(Task value) { 
    this.task=value;
   }
   public Task getTask() {
     return this.task;
   }

}
