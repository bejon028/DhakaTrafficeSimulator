import java.util.ArrayList;
import java.util.List;


public class Link {

        private int linkIndex;

	private int linkId;
	private int linkType;
	private int UpNodeId;
	private int DnNodeId;	
	
	private int segmentCount;
	private List<Segment> segmentList= new ArrayList<Segment>();
	 
	
	public Link(int lnkIndex, int lnkId, int lnkType, int upNodId, int dnNodId, int sCount)
	{
                linkIndex = lnkIndex;
		linkId = lnkId;
		linkType = lnkType;
		UpNodeId = upNodId;
		DnNodeId = dnNodId;
		segmentCount = sCount;		
	}
	public void SegementInitialization(List<Segment> sgmntList)
	{
		segmentList = sgmntList;
	}
	public Segment getSegment(int x)
	{
		return segmentList.get(x);
	}
	public int getSegmentCount()
	{
		return segmentCount;
	}
	
}
