import java.util.ArrayList;
import java.util.List;

public class StripConnector {


        private int nextLink;
        private int nextSegment;

	private List<Pair> stripConnection= new ArrayList<Pair>();

	public StripConnector(int nxtLink, int nxtSegment, List<Pair> strpConnection)
	{
            nextLink = nxtLink;
            nextSegment = nxtSegment;
            stripConnection = strpConnection;
	}
             

}
