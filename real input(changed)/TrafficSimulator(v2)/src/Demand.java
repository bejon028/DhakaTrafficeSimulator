import java.util.ArrayList;
import java.util.List;

public class Demand {


        private int sourceNode;
        private int destinationNode;
        private int demand;
        private int numOfPath;

	private List<Path> pathList= new ArrayList<Path>();

	public Demand(int srcNode, int dstNode, int dmand)
	{
                sourceNode = srcNode;
                destinationNode = dstNode;
                demand = dmand;
	}
        public void addPath(Path path)
        {
            pathList.add(path);
        }
        public int getSource()
        {
            return sourceNode;
        }
        public int getDestination()
        {
            return destinationNode;
        }
        public int getDemand()
        {
            return demand;
        }
        public int getNumPath()
        {
            return pathList.size();
        }
        public Path getPath(int x)
        {
            return pathList.get(x);
        }

}
