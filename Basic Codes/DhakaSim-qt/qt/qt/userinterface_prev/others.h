#ifndef OTHERS_H
#define OTHERS_H

#include<iostream>
#include<string>
#include<vector>
#include<cmath>
using namespace std;


class VehicleType {
public:
    double stripWidth;
    VehicleType()
    {
        stripWidth=0.5;
    }
    double Width(int type)
    {
        if(type == 1)
            return 1.76;
        else if(type == 2 )
            return  1.78;
        else if(type == 3 )
            return  2.13;
        else if(type == 4 )
            return  2.02;
        else if(type == 5 )
            return  1.8;
        else if(type == 6 )
            return  1.3;
        else if(type == 7 )
            return  1.22;
        else if(type == 8 )
            return  0.75;
        else if(type == 9 )
            return  0.61;
        else if(type == 10 )
            return  2.46;
        else if(type == 11 )
            return  2.44;
        else
            return 1.76;
    }
    double Length(int type)
    {
        if(type == 1)
            return 4.54;
        else if(type == 2 )
            return  4.29;
        else if(type == 3 )
            return  4.47;
        else if(type == 4 )
            return  5.78;
        else if(type == 5 )
            return  5.5;
        else if(type == 6 )
            return  2.63;
        else if(type == 7 )
            return  2.51;
        else if(type == 8 )
            return  2.13;
        else if(type == 9 )
            return  1.78;
        else if(type == 10 )
            return  8.46;
        else if(type == 11 )
            return  6.7;
        else
            return 4.54;
    }
    int numStrip(int type)
    {
        double width = Width(type);
        double widthCon = width/stripWidth;
        int numStrip = (int)ceil(widthCon);
        return numStrip;
    }

};
class Strip;
class Segment;
class Node {
public:
    int nodeId;
    int nodeType;
    string nodeName;
    Node(int nodId, int nodType, string nodName)
    {
        nodeId = nodId;
        nodeType = nodType;
        nodeName = nodName;
    }

};
#include<QApplication>

class Vehicle {
public:
    int vehicleId;
    int type;
    double length;
    double width;
    double stripWidth;
    int numStrip;
    Segment* segment;
    Strip* strip;
    double distance;
    double speed;
    double acceleration;
    Vehicle* leader;
    Vehicle* follower;
    bool leaderValid;
    bool followerValid;


    Vehicle(double initSpeed,double initAccelaration,int itype, Segment*initSegment, Strip* initStrip)
    {
        static int counter=0;
        vehicleId=(counter++);
        stripWidth=0.5;
        type = itype;
        length = vehicleLength(type);
        width = vehicleWidth(type);
        numStrip = numStripOccupied(width);
        segment = initSegment;
        strip = initStrip;
        distance = 0.1;
        speed = initSpeed;
        acceleration = initAccelaration;
        stripOccupy();

    }
    double vehicleLength(int type);
    double  vehicleWidth( int type);
    int numStripOccupied(double width);
    int  getVehicleId();
    void stripOccupy();
    void setSegment(Segment *x);
    void setStrip(Strip* x);
    void setDistance(double x);
    void setSpeed(double x);
    void setAcceleration(double x);
    int getType();
    double getLength();
    double getWidth();
    Segment* getSegment();
    Strip* getStrip();
    double getDistance();
    double getSpeed();
    double getAcceleration();
    int getNumStrip();
    void moveright();
    void moveleft();
    void moveforward();
    bool segmentEnd();
    void segmentChange(Segment*sgmnt, Strip*strp);
    void increaseSpeed(double acc);
    void decreaseSpeed(double dcc);


};

class Strip {
public:
    int segmentID;
    int stripID;
    vector<Vehicle*> vehicleList;
    Strip(int segID, int strID)
    {
        segmentID = segID;
        stripID = strID;
    }
    int getStripId()
    {
        return stripID;
    }
    void addVehicle(Vehicle *v)
    {
        //qDebug("added vehicle to strip %d segment %d",stripID,segmentID);
        vehicleList.push_back(v);
    }
    void delVehicle(Vehicle *v)
    {
        for(int i=0;i<vehicleList.size();i++)
        {
            if(vehicleList[i]->getVehicleId()==v->getVehicleId())
            {
                vehicleList.erase(vehicleList.begin()+i);
            }
        }
    }
    Vehicle probableLeader(Vehicle &v)
    {
        double distance = v.getDistance();
        double min = 1000;
        Vehicle *ret=&v;
        for(int i=0; i<vehicleList.size();i++)
        {
            if(vehicleList[i]->getVehicleId()!=v.getVehicleId() && vehicleList[i]->getDistance()>distance)
            {
                double compare = vehicleList[i]->getDistance()-distance;
                if(compare<min)
                {
                    min=compare;
                    ret=vehicleList[i];
                }
            }
        }
        return *ret;
    }
    bool isGapforForwardMovement(Vehicle *v)
    {
        double thresholdDistance = 0.12;
        double upperLimit = v->getDistance() + v->getLength() + v->getSpeed()+ thresholdDistance;
        double lowerLimit = v->getDistance();
        for(int sp=0; sp<vehicleList.size(); sp++)
        {
//            if(vehicleList[sp]->getVehicleId()==v->getVehicleId())qDebug("%d %d",v,vehicleList[sp]);
            if(vehicleList[sp]->getVehicleId()!=v->getVehicleId() && (vehicleList[sp]->getDistance()>lowerLimit && vehicleList[sp]->getDistance()<upperLimit || vehicleList[sp]->getDistance()+vehicleList[sp]->getLength()>lowerLimit && vehicleList[sp]->getDistance()+vehicleList[sp]->getLength()<upperLimit))
            {
                //qDebug("%d %d %lf %lf %lf %lf",vehicleList[sp]->getVehicleId(),v->getVehicleId(),upperLimit,lowerLimit,vehicleList[sp]->getDistance(),vehicleList[sp]->getDistance()+vehicleList[sp]->getLength());
                return false;
            }
        }
        return true;

    }
    bool isGapforAddingVehicle(double vehicleLength)
    {
        double lowerLimit = 0.08;
        double upperLimit = 0.6+vehicleLength;
        for(int sp=0; sp<vehicleList.size(); sp++)
        {
            if(vehicleList[sp]->getDistance()<upperLimit && vehicleList[sp]->getDistance()>lowerLimit)
            {
                return false;
            }
        }
        return true;

    }
    bool isGapForStripChange(Vehicle *v)
    {
        double thresholdDistance = 0.1;
        double lowerLimit1 = v->getDistance() - thresholdDistance;
        double upperLimit1 = v->getDistance() + v->getLength() + thresholdDistance;
        for(int sp=0; sp<vehicleList.size(); sp++)
        {
            double lowerLimit2 = vehicleList[sp]->getDistance() - thresholdDistance;
            double upperLimit2 = vehicleList[sp]->getDistance() + vehicleList[sp]->getLength() + thresholdDistance;
            if((lowerLimit1>=lowerLimit2 && lowerLimit1<=upperLimit2 ||
                                        upperLimit1>=lowerLimit2 && upperLimit1<=upperLimit2)||
                                       (lowerLimit2>=lowerLimit1 && lowerLimit2<=upperLimit1 ||
                                        upperLimit2>=lowerLimit1 && upperLimit2<=upperLimit1))
                            return false;
        }
        return true;

    }
};

class Segment {
public:
    int linkId;
    int segmentId;
    double speedLimit;
    double freeSpeed;
    double gradient;
    int speedDensityIndex;
    double StartingPntX;
    double StartingPntY;
    double EndPntX;
    double EndPntY;
    double bulge;
    bool lastsegment;
    int stripCount;
    vector<Strip> stripList;
    Segment(int lnkId, int segID, double strtPntX, double strtPntY, double ndPntX, double ndPntY, int scount, bool lstSegment)
    {
        linkId = lnkId;
        segmentId = segID;
        StartingPntX = strtPntX;
        StartingPntY = strtPntY;
        EndPntX = ndPntX;
        EndPntY = ndPntY;
        stripCount = scount;
        lastsegment = lstSegment;
        stripIntialization();
    }
    void stripIntialization()
    {
        for(int i=0; i<stripCount; i++)
        {
            stripList.push_back(*(new Strip(segmentId, i)));
        }
    }
    Strip* getStrip(int x)
    {
        return &stripList[x];
    }
    int getStripCount()
    {
        return stripCount;
    }
    double getStartingPntX()
    {
        return StartingPntX;
    }
    double getStartingPntY()
    {
        return StartingPntY;
    }
    double getEndPntX()
    {
        return EndPntX;
    }
    double getEndPntY()
    {
        return EndPntY;
    }
    int getLinkId()
    {
        return linkId;
    }
    int getSegmentId()
    {
        return segmentId;
    }
    bool lastSegment()
    {
        return lastsegment;
    }

};
/*


*/
inline double  Vehicle::vehicleLength(int type)
{

        if(type == 1)
            return 4.54;
        else if(type == 2 )
            return  4.29;
        else if(type == 3 )
            return  4.47;
        else if(type == 4 )
            return  5.78;
        else if(type == 5 )
            return  5.5;
        else if(type == 6 )
            return  2.63;
        else if(type == 7 )
            return  2.51;
        else if(type == 8 )
            return  2.13;
        else if(type == 9 )
            return  1.78;
        else if(type == 10 )
            return  8.46;
        else if(type == 11 )
            return  6.7;
        else
            return 4.54;
}
inline double Vehicle::vehicleWidth(int type)
{
        if(type == 1)
            return 1.76;
        else if(type == 2 )
            return  1.78;
        else if(type == 3 )
            return  2.13;
        else if(type == 4 )
            return  2.02;
        else if(type == 5 )
            return  1.8;
        else if(type == 6 )
            return  1.3;
        else if(type == 7 )
            return  1.22;
        else if(type == 8 )
            return  0.75;
        else if(type == 9 )
            return  0.61;
        else if(type == 10 )
            return  2.46;
        else if(type == 11 )
            return  2.44;
        else
            return 1.76;
    }
inline int Vehicle::numStripOccupied(double width)
{

        double widthCon = width/stripWidth;
        int numStrip = (int) ceil(widthCon);
        return numStrip;
}
inline int Vehicle:: getVehicleId()
    {
        return vehicleId;
    }
inline void Vehicle::stripOccupy()
{
        int x = strip->getStripId();
        int i;
        for(i=0; i<numStrip; i++)
        {
            segment->stripList[x+i].addVehicle(this);
        }
}
inline void Vehicle::setSegment(Segment* x)
    {
        segment = x;
    }
inline void Vehicle:: setStrip(Strip *x)
{
    strip = x;
}
inline void Vehicle::setDistance(double x)
 {
     distance=x;
 }
inline  void Vehicle::setSpeed(double x)
 {
     speed=x;
 }
inline   void Vehicle::setAcceleration(double x)
    {
        acceleration=x;
        speed+=acceleration;
    }


inline  int Vehicle:: getType()
    {
        return type;
    }
inline    double Vehicle:: getLength()
    {
        return length;
    }
inline    double Vehicle:: getWidth()
    {
        return width;
    }
inline    Segment* Vehicle:: getSegment()
    {
        return segment;
    }
inline    Strip* Vehicle::getStrip()
    {
        return strip;
    }
inline    double Vehicle:: getDistance()
    {
        return distance;
    }
inline    double Vehicle:: getSpeed()
    {
        return speed;
    }
inline    double Vehicle:: getAcceleration()
    {
        return acceleration;
    }
inline    int Vehicle::getNumStrip()
    {
        return numStrip;
    }
    //Movement Functions

 inline    void  Vehicle::moveright()
    {
        int x = strip->getStripId();
        if(x+numStrip<segment->getStripCount() && segment->getStrip(x+numStrip)->isGapForStripChange(this)==true)
        {
            setStrip(&segment->stripList[x+1]);
            segment->getStrip(x)->delVehicle(this);
            segment->getStrip(x+numStrip)->addVehicle(this);
        }
        //makeLeader();

    }
inline     void  Vehicle:: moveleft()
    {
        int x = strip->getStripId();
        if(x!=0 && segment->stripList[x-1].isGapForStripChange(this)==true)
        {
            setStrip(&segment->stripList[x-1]);
            segment->getStrip(x-1)->addVehicle(this);
            segment->getStrip(x+numStrip-1)->delVehicle(this);
        }
        //makeLeader();
    }
 inline void Vehicle::moveforward()
    {
        int flag = 1;
        int x = strip->getStripId();
        for(int i=x; i<x+numStrip; i++)
        {
            if(segment->stripList[i].isGapforForwardMovement(this)== false)
                flag=0;
        }
        if(flag==1)distance+=speed;

    }
 inline   bool Vehicle:: segmentEnd()
    {
        double thresholdDistance = speed;
        double segmentLength = sqrt(pow(segment->getStartingPntX()-segment->getEndPntX(), 2)
                +pow(segment->getStartingPntY()-segment->getEndPntY(), 2));
        if(distance + length + thresholdDistance> segmentLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
 inline       void Vehicle::segmentChange(Segment* sgmnt, Strip* strp)
        {
            segment = sgmnt;
            strip = strp;
            distance = 0.1;
            stripOccupy();
            //makeLeader();
        }

    //Acceleration Functions
 inline    void Vehicle::increaseSpeed(double acc)
    {
        speed+=acc;
    }
inline     void Vehicle::decreaseSpeed(double dcc)
    {
        speed-=dcc;
    }
class Link {
public:
    int linkId;
    int linkType;
    int UpNodeId;
    int DnNodeId;
    int LinkLabelId;
    int segmentCount;
    vector<Segment> segmentList;


    Link(int lnkId, int lnkType, int upNodId, int dnNodId, int sCount)
    {
        linkId = lnkId;
        linkType = lnkType;
        UpNodeId = upNodId;
        DnNodeId = dnNodId;
        segmentCount = sCount;
    }
    void SegementInitialization(vector<Segment> sgmntList)
    {
        segmentList = sgmntList;
    }
    Segment * getSegment(int x)
    {
        return &segmentList[x];
    }
    int getSegmentCount()
    {
        return segmentCount;
    }

};

class Path{
public:
    int sourceNode;
    int destinationNode;
    vector<int>	linkIdList;
    Path(int srcNode, int dstNode)
    {
        sourceNode = srcNode;
        destinationNode = dstNode;
    }
    void addLinkId(int lnkId)
    {
        linkIdList.push_back(lnkId);
    }
    bool correctPath(int srcNode, int dstNode)
    {
        if(sourceNode == srcNode && destinationNode == dstNode)
            return true;
        else
            return false;
    }
    int pathLength()
    {
        return linkIdList.size();
    }
    int getLinkId(int x)
    {
        return linkIdList[x];
    }

};

class Demand {
public:
    int sourceNode;
    int destinationNode;
    int demand;
    int numOfPath;
    vector<Path> pathList;
    Demand(int srcNode, int dstNode, int dmand)
    {
        sourceNode = srcNode;
        destinationNode = dstNode;
        demand = dmand;
    }
    void addPath(Path path)
    {
        pathList.push_back(path);
    }
    int getSource()
    {
        return sourceNode;
    }
    int getDestination()
    {
        return destinationNode;
    }
    int getDemand()
    {
        return demand;
    }
    int getNumPath()
    {
        return pathList.size();
    }
    Path * getPath(int x)
    {
        return &pathList[x];
    }

};


#endif // OTHERS_H
