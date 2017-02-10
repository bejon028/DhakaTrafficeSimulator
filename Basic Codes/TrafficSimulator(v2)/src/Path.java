import java.util.ArrayList;
import java.util.List;


public class Path{

        private int sourceNode;
        private int destinationNode;

	private List<Integer> linkIndexList= new ArrayList<Integer>();

	public Path(int srcNode, int dstNode)
	{
                sourceNode = srcNode;
                destinationNode = dstNode;

	}
        public void addLinkIndex(int lnkIndex)
        {
            linkIndexList.add(lnkIndex);
        }
        public boolean correctPath(int srcNode, int dstNode)
        {
            if(sourceNode == srcNode && destinationNode == dstNode)
                return true;
            else
                return false;
        }
        public int pathLength()
        {
            return linkIndexList.size();
        }
        public int getLinkIndex(int x)
        {
            return linkIndexList.get(x);
        }
}
