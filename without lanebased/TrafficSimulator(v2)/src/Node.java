import java.util.ArrayList;
import java.util.List;

/**
 * Sources,destinations and junctions are considered as nodes.This class describes a node.
 * @author USER
 */

public class Node {
	private int nodeId;
	private int nodeType;
	private String nodeName;
        
        
        //A node is defined by its id,type and name.
	public Node(int nodId, int nodType, String nodName)
	{
		nodeId = nodId;
                //nodeType = 1 means 4 cross junction
		nodeType = nodType;	
		nodeName = nodName;
	}
        

        
        public int getType()
        {
            return nodeType;
        }
        
}
