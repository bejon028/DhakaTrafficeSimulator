import java.util.ArrayList;
import java.util.List;

/**
 * Paths in a network consist of links.We consider path as the route from 
 * origin position to destination through network.
 * 
 * @author USER
 */

public class Path{

        private int sourceNode;
        private int destinationNode;

	private List<Integer> linkIndexList= new ArrayList<Integer>();
        
        //constructs path from the information of source position and destination
	public Path(int srcNode, int dstNode)
	{
                sourceNode = srcNode;
                destinationNode = dstNode;

	}
        
        //adds link to the path using index
        public void addLinkIndex(int lnkIndex)
        {
            linkIndexList.add(lnkIndex);
        }
        
        //checks whether this is the correct path for a given source position and destination
        public boolean correctPath(int srcNode, int dstNode)
        {
            if(sourceNode == srcNode && destinationNode == dstNode)
                return true;
            else
                return false;
        }
        
        //returns path length on the basis of link count in the path
        public int pathLength()
        {
            return linkIndexList.size();
        }
        
        //returns a link index in the path according to the given integer value
        public int getLinkIndex(int x)
        {
            return linkIndexList.get(x);
        }
}
