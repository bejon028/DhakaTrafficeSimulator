
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USER
 */
public class Junction {
    private int junctionId;
    private int junctionType;
    private List<Integer> nodeIndexList;
    public List<Integer> inLinkIndexes;
    
    public Junction(int jncid, int jnctype, List<Integer> ndIndexList, List<Integer> inLnkIndx)
    {
        junctionId = jncid;
        junctionType = jnctype;
        nodeIndexList = ndIndexList;
        inLinkIndexes = inLnkIndx;
    }
    
    
}
