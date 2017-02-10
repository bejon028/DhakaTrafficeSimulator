import java.util.ArrayList;
import java.util.List;

/**
 * From a source position to destination, a certain amount of vehicle needs to move which is named as 'demand'.
 * @author USER
 */

public class Demand {


        private int sourceNode;
        private int destinationNode;
        private int demand;
        private int numOfPath;
        
        //private boolean demandDir;
        //holds the path associated with demand
	private List<Path> pathList= new ArrayList<Path>();
        
        //constructor of demand object
	public Demand(int srcNode, int dstNode, int dmand)
	{
                sourceNode = srcNode;
                destinationNode = dstNode;
                demand = dmand;
	}
        
        //adds possible paths according to demand source and destination.
        public void addPath(Path path)
        {
            pathList.add(path);
        }
        
        //returns source node id
        public int getSource()
        {
            return sourceNode;
        }
        
        //returns destination id
        public int getDestination()
        {
            return destinationNode;
        }
        
        //returns number of vehicles that should pass through the path
        public int getDemand()
        {
            return demand;
        }
        
        //returns the number of possible paths to meet the demand
        public int getNumPath()
        {
            return pathList.size();
        }
        
        //returns path
        public Path getPath(int x)
        {
            return pathList.get(x);
        }
        
//        public void setDemandDir(boolean dir)
//        {
//            demandDir = dir;
//        }
//        
//        public boolean isPositiveDemand()
//        {
//            return demandDir;
//        }
}
