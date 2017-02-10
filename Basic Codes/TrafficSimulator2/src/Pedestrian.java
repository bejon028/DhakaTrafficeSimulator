/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author turash
 */
import java.math.*;
import java.util.ArrayList;
import java.util.List;
public class Pedestrian {
        
        private int type;
	private double length=0.5;
	private double width=0.5;
	private int numStrip=1;
	
	private Segment segment;
	private Strip strip;
	private double distance;
	
	private double speed=1;
	
        public Pedestrian(Segment initSegment, Strip initStrip ,double dstnc)
	{
		
		
		segment = initSegment;
		strip = initStrip;
		distance = dstnc;
                stripOccupy();
		
	}
        
        public void setSegment(Segment x)
	{
		segment = x;
	}
	public void setStrip(Strip x)
	{
		strip = x;
	}
        public void setDistance(double x)
        {
            distance = x;
        }
        public void setSpeed(double x)
	{
		speed=x;
	}
        public int getType()
	{
		return type;
	}
        
	public double getLength()
	{
		return length;
	}
	public double getWidth()
	{
		return width;
	}
	public Segment getSegment()
	{
		return segment;
	}
	public Strip getStrip()
	{
		return strip;
	}
	public double getDistance()
	{
		return distance;
	}
	public double getSpeed()
	{
		return speed;
	}
        public void moveright()
	{
		int x = strip.getStripIndex();
		if(x+numStrip<segment.getStripCount() && segment.getStrip(x+numStrip).isGapForStripChange(this)==true)
		{
			setStrip(segment.getStrip(x+1));
			segment.getStrip(x).delPedestrian(this);
			segment.getStrip(x+numStrip).addPedestrian(this);
		}
		
		
	}
	public void moveleft()
	{
		int x = strip.getStripIndex();
		if(x!=0 && segment.getStrip(x-1).isGapForStripChange(this)==true)
		{
			setStrip(segment.getStrip(x-1));
			segment.getStrip(x-1).addPedestrian(this);
			segment.getStrip(x+numStrip-1).delPedestrian(this);
		}
		
	}
	public void moveforward()
	{
		int flag = 1;
                double length;
		int x = strip.getStripIndex();
		for(int i=x; i<x+numStrip; i++)
		{
			if(segment.getStrip(i).isGapforForwardMovement(this)== false)
				flag=0;				
		}
                length = Math.sqrt(Math.pow(segment.getStartingPntX()-segment.getEndPntX(), 2)
				+Math.pow(segment.getStartingPntY()-segment.getEndPntY(), 2));
		if(flag==1&&distance+speed<=length)
                {
			distance+=speed;
                    
                }
				
	}
        public void movebackward()
	{
		int flag = 1;
		int x = strip.getStripIndex();
		for(int i=x; i<x+numStrip; i++)
		{
			if(segment.getStrip(i).isGapforBackwardMovement(this)== false)
				flag=0;				
		}
		if(flag==1&&distance-speed>=0)
                {
			distance-=speed;
                    
                }
				
	}
        public int getNumStrip()
        {
            return numStrip;
        }
	public void stripOccupy()
	{
		int x = strip.getStripIndex();
                int i;
		for(i=0; i<numStrip; i++)
		{ 
			segment.getStrip(x+i).addPedestrian(this);
		}
	}
        public boolean segmentEnd()
	{
		if(strip.getStripIndex()>=segment.getStripCount()-1)return true;
                return false;
	}
	

    
}