#ifndef __asdsa
#define __asdsa

#include <vector>
#include <string.h>
#include <string>
#include <stdio.h>
#include <iostream>
#include <cstdlib>
#include<ctype.h>
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

class Pair
{
private:
    int x;
    int y;

public :
    Pair(int a, int b)
    {
        x = a;
        y = b;
    }
    int getX()
    {
        return x;
    }
    int getY()
    {
        return y;
    }
};
class StripConnector {

private:
        int nextLink;
        int nextSegment;

	vector<Pair>* stripConnection;

public:
    StripConnector(int nxtLink, int nxtSegment, vector<Pair>* strpConnection)
	{
            nextLink = nxtLink;
            nextSegment = nxtSegment;
            stripConnection = strpConnection;
	}


};
class Pedestrian{
public:
     const int type=100;
     const double length=0.5;
     const double width=0.5;
     double stripWidth = 0.5;
    const int numStrip=1;
    int pedestrianId;
        // int demandIndex;
         int pathIndex;
         int pathLinkIndex;

     Segment* segment;
    Strip* strip;
     double distance;
      //   bool passedSensor;

     double speed;
    double acceleration;

     //Vehicle* leader;
    // Vehicle* follower;
     //bool leaderValid=false;
    // bool followerValid=false;
    Pedestrian(double initSpeed,double initAccelaration, Segment* initSegment, Strip* initStrip,
                 int pthIndex, int pthLinkIndex)
    {
        static int counter=0;
        pedestrianId=(counter++);


                pathIndex = pthIndex;
                pathLinkIndex = pthLinkIndex;

        segment = initSegment;
        strip = initStrip;
        distance = 0.1;
                //passedSensor = false;

        speed = initSpeed;
        acceleration = initAccelaration;

        //stripOccupy();

    }

};
class Vehicle {
public:
     int type;
     double length;
     double width;
     double stripWidth = 0.5;
    int numStrip;
    int vehicleId;
         int demandIndex;
         int pathIndex;
         int pathLinkIndex;

     Segment* segment;
    Strip* strip;
     double distance;
         bool passedSensor;

     double speed;
    double acceleration;

     Vehicle* leader;
     Vehicle* follower;
     bool leaderValid=false;
     bool followerValid=false;

    Vehicle(double initSpeed,double initAccelaration,int itype, Segment* initSegment, Strip* initStrip,
                int dmandIndex, int pthIndex, int pthLinkIndex)
    {
        static int counter=0;
        vehicleId=(counter++);
        type = itype;
		length = vehicleLength(type);
		width = vehicleWidth(type);
		numStrip = numStripOccupied(width);

                demandIndex = dmandIndex;
                pathIndex = pthIndex;
                pathLinkIndex = pthLinkIndex;

		segment = initSegment;
		strip = initStrip;
		distance = 0.1;
                passedSensor = false;

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
    int getDemandIndex();
    int getPathIndex();
    int getPathLinkIndex();

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
    bool didPassSensor();
    void linkChange(int pthLinkIndex, Segment* sgmnt, Strip*strp);
    void findLeader();
};


class Strip {
public:
    int segmentIndex;
    int stripIndex;
    vector<Vehicle*> vehicleList;
    Strip(int segID, int strID)
    {
        segmentIndex = segID;
        stripIndex = strID;
    }
    int getStripIndex()
    {
        return stripIndex;
    }
    void addVehicle(Vehicle *v)
    {
        vehicleList.push_back(v);
    }
    void delVehicle(Vehicle *v)
    {
        for(int i=0;i<vehicleList.size();i++)
        {
            if(vehicleList[i]->getVehicleId()==v->getVehicleId())
            {
                vehicleList.erase(vehicleList.begin()+i);
                break;
            }
        }
    }
    Vehicle* probableLeader(double distance, double length)
	{
		double min = 9999;
		Vehicle* ret = NULL;
		for(int i=0; i<vehicleList.size();i++)
		{
            if(vehicleList[i]->getDistance()>distance+0.1)
			{
                double compare = vehicleList[i]->getDistance()-distance;
				if(compare<min)
				{
					min=compare;
                    ret=vehicleList[i];
				}
			}
		}
		return ret;
	}
    bool isGapforForwardMovement(Vehicle *v)
    {
        double thresholdDistance = 0.12;
        double upperLimit = v->getDistance() + v->getLength() + v->getSpeed()+ thresholdDistance;
        double lowerLimit = v->getDistance();
        for(int sp=0; sp<vehicleList.size(); sp++)
        {
            if(vehicleList[sp]->getVehicleId()!=v->getVehicleId() && ((vehicleList[sp]->getDistance()>lowerLimit && vehicleList[sp]->getDistance()<upperLimit) || (vehicleList[sp]->getDistance()+vehicleList[sp]->getLength()>lowerLimit && vehicleList[sp]->getDistance()+vehicleList[sp]->getLength()<upperLimit)))
                return false;
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
            //return false;
            double lowerLimit2 = vehicleList[sp]->getDistance() - thresholdDistance;
            double upperLimit2 = vehicleList[sp]->getDistance() + vehicleList[sp]->getLength() + thresholdDistance;
            if(((lowerLimit1>=lowerLimit2 && lowerLimit1<=upperLimit2) || (upperLimit1>=lowerLimit2 && upperLimit1<=upperLimit2))||((lowerLimit2>=lowerLimit1 && lowerLimit2<=upperLimit1) || (upperLimit2>=lowerLimit1 && upperLimit2<=upperLimit1)))
            {
                return false;
            }
        }
        return true;

    }
};

class Segment {
public:
     int linkIndex;
     int segmentIndex;

	 int segmentId;
	 double speedLimit;
	 double freeSpeed;
	 double gradient;
    double bulge;
    int speedDensityIndex;

    double StartingPntX;
    double StartingPntY;
    double EndPntX;
    double EndPntY;
    double sensor;
    int sensorVehicleCount;
    double sensorVehicleAvgSpeed;
    bool lastsegment;
    int stripCount;
    vector<Strip> stripList;
    Segment(int lnkIndex, int segIndex, int segID, double strtPntX, double strtPntY,
                double ndPntX, double ndPntY, double snsr, int scount, bool lstsegment)
    {
        linkIndex = lnkIndex;
        segmentIndex = segIndex;
		segmentId = segID;
		StartingPntX = strtPntX;
		StartingPntY = strtPntY;
		EndPntX = ndPntX;
		EndPntY = ndPntY;
                sensor = snsr;
		stripCount = scount;
                this->lastsegment = lstsegment;
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
    double getSensor()
    {
            return sensor;
    }
    int getLinkIndex()
    {
        return linkIndex;
    }
    int getSegmentIndex()
    {
        return segmentIndex;
    }
    bool lastSegment()
    {
        return lastsegment;
    }
    void updateSensorInfo(double newSpeed)
    {
        sensorVehicleCount++;
        sensorVehicleAvgSpeed = sensorVehicleAvgSpeed/sensorVehicleCount*(sensorVehicleCount-1)+
                newSpeed/sensorVehicleCount;
    }
    int getSensorVehicleCount()
    {
        return sensorVehicleCount;
    }
     double getSensorVehicleAvgSpeed()
    {
        return sensorVehicleAvgSpeed;
    }

};
/*


*/
inline int Vehicle::getDemandIndex()
        {
            return demandIndex;
        }
inline int Vehicle::getPathIndex()
        {
            return pathIndex;
        }
inline int Vehicle::getPathLinkIndex()
        {
            return pathLinkIndex;
        }
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
        int x = strip->getStripIndex();
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
        int x = strip->getStripIndex();
        if(x+numStrip<segment->getStripCount() && segment->getStrip(x+numStrip)->isGapForStripChange(this)==true)
        {
            setStrip(&segment->stripList[x+1]);
            segment->getStrip(x)->delVehicle(this);
            segment->getStrip(x+numStrip)->addVehicle(this);
        }
        findLeader();

    }
inline     void  Vehicle:: moveleft()
    {
        int x = strip->getStripIndex();
        if(x!=0 && segment->stripList[x-1].isGapForStripChange(this)==true)
        {
            setStrip(&segment->stripList[x-1]);
            segment->stripList[x-1].addVehicle(this);
            segment->stripList[x+numStrip-1].delVehicle(this);
        }
        findLeader();
    }
 inline    void Vehicle::moveforward()
    {
       int flag = 1;
		int x = strip->getStripIndex();
		for(int i=x; i<x+numStrip; i++)
		{
			if(segment->getStrip(i)->isGapforForwardMovement(this)== false)
				flag=0;
		}
		if(flag==1)
                {
			distance+=speed;
                        if(passedSensor == false)
                        {
                            if(distance>segment->getSensor())
                                passedSensor = true;
                        }
                }
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
inline bool Vehicle:: didPassSensor()
        {
            return passedSensor;
        }

 inline       void Vehicle::segmentChange(Segment* sgmnt, Strip* strp)
        {
            segment = sgmnt;
            strip = strp;
            distance = 0.1;
            passedSensor = false;
            stripOccupy();
            findLeader();
        }
inline       void Vehicle::linkChange(int pthLinkIndex, Segment* sgmnt, Strip* strp)
        {
            pathLinkIndex = pthLinkIndex;
            segmentChange(sgmnt,strp);
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
  inline void Vehicle::  findLeader()
	{
		int x = strip->getStripIndex();
		double min = 1000;
		for(int i=x; i<x+numStrip; i++)
		{
			Vehicle* temp = strip->probableLeader(distance,length);
			if(temp!=NULL)
			{
				if(temp->getDistance()<min)
				{
					min = temp->getDistance();
					leader = temp;
                    leaderValid = true;
				}
			}

		}

	}
class Link {
public:
    int linkId;
    int linkType;
    int UpNodeId;
    int DnNodeId;
    int linkIndex;
    int segmentCount;
    vector<Segment> segmentList;


    Link(int lnkIndex,int lnkId, int lnkType, int upNodId, int dnNodId, int sCount)
    {
         linkIndex = lnkIndex;
        linkId = lnkId;
        linkType = lnkType;
        UpNodeId = upNodId;
        DnNodeId = dnNodId;
        segmentCount = sCount;
    }
    void SegementInitialization(vector<Segment>* sgmntList)
    {
        segmentList = *sgmntList;
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
    int pathId;
    int sourceNode;
    int destinationNode;
    vector<int>	linkIdList;
    Path(int srcNode, int dstNode)
    {
        static int counter=0;
        pathId=(counter++);
        sourceNode = srcNode;
        destinationNode = dstNode;
    }
    void addLinkIndex(int lnkId)
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
    void addPath(Path *path)
    {
        pathList.push_back(*path);
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




class VehicleLoad
{
    vector <Path> pathList;   //include path.h
    vector <Path> pathRemovalList;
    vector <Demand> demandList;   //include demand.h file
    public:
        string filename1 = "Path.txt";
        string filename2 = "Demand.txt";
        VehicleLoad();
        void readPath();
        void readDemand();
        void addPath();
        vector<Demand> getDemand();
};

inline VehicleLoad::VehicleLoad()
{
    readPath();
    readDemand();
    addPath();
}

inline void VehicleLoad::readPath()
{
    try{
	    FILE *fp = fopen(filename1.c_str(), "r");
	    int index,numPaths,nodeId1,nodeId2;

	    fscanf(fp,"%d",&numPaths);
	    char str[50];
	    for(index = 0; index<numPaths; index++)
	    {
	        fscanf(fp,"%c",&str[0]);  //consumes the \n character
	        fscanf(fp,"%[^\n]",str);
	        char *pp;
	        pp = strtok(str," ");
	        vector<string> V;
	        while(pp!=NULL)
	        {
	            V.push_back(pp);
	            pp = strtok(NULL," ");
	        }

	        nodeId1 = atoi(V[0].c_str());
	        nodeId2 = atoi(V[1].c_str());
	        Path pt(nodeId1,nodeId2);
	        pathList.push_back(pt);

	        for(int x=2; x < V.size(); x++)
	        {
	            int linkIndex = atoi(V[x].c_str());
	            pathList[index].addLinkIndex(linkIndex);
	        }
	    }
    	fclose (fp);
    }
	catch(string s){}
}

inline void VehicleLoad::readDemand()
{
    FILE *fp = fopen(filename2.c_str(), "r");
    int index,numDemands,nodeId1,nodeId2,demand;
    fscanf(fp,"%d",&numDemands);
    char ch;
    for(index =0; index < numDemands; index++)
    {
        fscanf(fp,"%c",&ch);   //consumes the \n character
        fscanf(fp,"%d%d%d",&nodeId1,&nodeId2,&demand);
        Demand dm(nodeId1,nodeId2,demand);
        demandList.push_back(dm);
    }
    fclose(fp);
}

inline void VehicleLoad::addPath()
{
    int i,j,k;
    for(i=0; i<demandList.size(); i++)
    {
        for(j=0;j<pathList.size(); j++)
        {
            if(pathList[j].correctPath(demandList[i].getSource(),demandList[i].getDestination()))
            {
                demandList[i].addPath(&pathList[j]);
                pathRemovalList.push_back(pathList[j]);
            }
        }
        for(k=0; k<pathRemovalList.size(); k++)
        {
        	for(int r=0; r<pathList.size(); r++)
			{
				/* if it does not work then make an operator overloading of == in Path.h */
				if(pathList[r].pathId == pathRemovalList[k].pathId)
				{
                    pathList.erase(pathList.begin()+r);
                    r--;
				}
			}
        }
    }
}
inline vector <Demand> VehicleLoad::getDemand()
{
    return demandList;
}


class NetworkParse
{
    vector <Node> nodeList;   //include node.h
    vector <Link> linkList;   //include link.h
    int numberOfSegments = 0;   // newly added
    public:
        string fileName = "Network.txt";
        NetworkParse();
        void readNetwork();
        vector <Node>* getNodeList();
        vector <Link>* getLinkList();
        int getNumberOfSegments();
        bool isFileExists(string fileName);
        bool deleteFile(string filename);
};

inline NetworkParse::NetworkParse()
{
    readNetwork();
}

inline void NetworkParse::readNetwork()
{
    try
        {
            FILE *fp = fopen(fileName.c_str(),"r");
            int index,index2;
            int numNodes,nodeId,nodeType;
            char str[50];
            fscanf(fp,"%d",&numNodes);
            for(index=0; index < numNodes; index++)
            {
                fscanf(fp,"%c",&str[0]);  //consumes the \n character
                fscanf(fp,"%d%d%s",&nodeId,&nodeType,str);
                string ss(str);
                Node node(nodeId,nodeType,ss);   //put node related header file on networkparse.h
                nodeList.push_back(node);
            }
            int numLinks,linkId,linkType,UpNodeId,DnNodeId,segmentCount;
            fscanf(fp,"%c",&str[0]);  //consumes the \n character
            fscanf(fp,"%d",&numLinks);
            for(index=0; index<numLinks; index++)
            {
                fscanf(fp,"%c",&str[0]);   //consumes the \n character
                fscanf(fp,"%d%d%d%d%d",&linkId,&linkType,&UpNodeId,&DnNodeId,&segmentCount);

                numberOfSegments += segmentCount;   // newly added
                /* index argument newly added */
                Link ln(index,linkId,linkType,UpNodeId,DnNodeId,segmentCount);  //put link related header file on networkparse.h
                linkList.push_back(ln);
                vector<Segment> segmentList;   //put segment related header file on networkparse.h

                int segmentId,stripCount;
                double startPntX,startPntY,endPntX,endPntY,sensor;  // newly added sensor
                for(index2=0; index2<segmentCount; index2++)
                {
                    fscanf(fp,"%c",&str[0]);  //consumes the \n character
                    fscanf(fp,"%d%lf%lf%lf%lf%lf%d",&segmentId,&startPntX,&startPntY,&endPntX,&endPntY,&sensor,&stripCount);
                    bool lastSegment = false;
                    if(index2==segmentCount-1)
                    {
                        lastSegment = true;
                    }
                    /* newly added arguments */
                    Segment seg(index,index2,segmentId,startPntX,startPntY,endPntX,endPntY,sensor,stripCount,lastSegment);
                    segmentList.push_back(seg);
                }
                linkList[index].SegementInitialization(&segmentList);
            }
            fclose(fp);
        }
        catch(int param)
        {
            cout<<"Exception on an integer "<<param<<endl;
        }
        catch(string param)
        {
            cout<<"Exception on a string "<<param<<endl;
        }

}

inline vector<Node>* NetworkParse::getNodeList()
{
    return &nodeList;
}
inline vector<Link>* NetworkParse::getLinkList()
{
    return &linkList;
}
inline int NetworkParse::getNumberOfSegments()
{
	return numberOfSegments;
}

inline bool NetworkParse::isFileExists(string name)
{
    if (FILE *file = fopen(name.c_str(), "r"))
    {
        fclose(file);
        return true;
    }
    else
    {
        return false;
    }
}
inline bool NetworkParse::deleteFile(string filename)
{
	int result = remove(filename.c_str());
	if(!result) return true;
	else return false;
}

#endif
