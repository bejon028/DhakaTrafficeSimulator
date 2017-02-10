import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Each path in network consists of links.
 * @author USER
 */

public class Link {

        private int linkIndex;

	private int linkId;
	private int linkType;
	private int UpNodeId;    //one end,from where it starts
	private int DnNodeId;	 //other end, where it ends
	
	private int segmentCount;
        private double linkLength;  //my add for lane change
	private List<Segment> segmentList= new ArrayList<Segment>();
        public Map<Integer,Integer> nextLinkMap=new HashMap<Integer,Integer>();
	
        private boolean linkStop = false;
	
        //constructs link object
	public Link(int lnkIndex, int lnkId, int lnkType, int upNodId, int dnNodId, int sCount)
	{
                linkIndex = lnkIndex;
		linkId = lnkId;
		linkType = lnkType;
		UpNodeId = upNodId;
		DnNodeId = dnNodId;
		segmentCount = sCount;		
	}
        
        //assigns segments that constitute this link
	public void SegementInitialization(List<Segment> sgmntList)
	{
		segmentList = sgmntList;
	}
        
        
        //returns a segment in this link according to provided segment index 
	public Segment getSegment(int x)
	{
		return segmentList.get(x);
	}
        
        //returns number of segments in this link
	public int getSegmentCount()
	{
		return segmentCount;
	}
        
        public void nextLinkInitialization(Map<Integer,Integer> nxtlnkmp){
           
            nextLinkMap=nxtlnkmp;
        }
        
        public int directionOfNextLink(int nextlinkIndex){
            
            
            return nextLinkMap.get(nextlinkIndex);
            
        }
        
        public void setLinkLength(double linklength)
        {
            this.linkLength=linklength;
        }
        
        public double getLinkLength()
        {
            return linkLength;
        }
        
        //my change 
        public void stopLink()
        {
            linkStop = true;
        }
        
        public void startLink()
        {
            linkStop = false;
        }
        
        public boolean isLinkStopped()
        {
            if(linkStop == true)
                return true;
            return false;
        }
        
        public int getType()
        {
            return linkType;
        }
        
        public int getUpNodeId()
        {
            return UpNodeId;
        }
        
        public int getDnNodeId()
        {
            return DnNodeId;
        }
	
}
