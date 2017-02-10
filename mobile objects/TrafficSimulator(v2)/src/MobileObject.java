/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USER
 */
public class MobileObject {

    private Segment segment;
    private int initPos;
    private double distance;
    private double speed;
    private Strip strip;
    public boolean inAccident = false;
    public MobileObject(Segment seg,Strip strp,int initpos,double sp)
    {
        segment = seg;
        strip = strp;
        initPos = initpos;
        distance = 0;
        speed = sp;
        strip.addObject(this);
    }

    public double getDistance() {
        return distance;
    }

    public double getInitPos() {
        return initPos;
    }

    public Segment getSegment() {
        return segment;
    }

    
    public void moveForward()
    {
        int x;
        //x = (int) ((distance + speed) /Strip.stripWidth);
        if(distance+speed < segment.getFpWidth())
        {
            distance += speed ;
            return;
        }
        x = segment.getFpStripCount()+ (int) ((distance-segment.getFpWidth()+ speed) /Strip.stripWidth);
        if (x < segment.getStripCount()) {
            if (segment.getStrip(x).hasGapForObject(this)) {
                distance = distance + speed;
                strip.delObject(this);
                setStrip(segment.getStrip(x));
                strip.addObject(this);
            }

        } else {
            distance = distance + speed;
            strip.delObject(this);
        }
    }

    public Strip getStrip() {
        return strip;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public void setStrip(Strip strip) {
        this.strip = strip;
    }
    

    
    public boolean hasCrossedRoad()
    {
        //System.out.println(distance + " " + segment.getSegWidth() + " " + strip.getStripIndex());
        if(distance >= segment.getSegWidth())
            return true;
        else return false;
    }
}
