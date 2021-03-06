#include "drawingstep.h"
#include<cmath>
#include<stdlib.h>
#include <time.h>
#include<iostream>
using namespace std;

DrawingStep::DrawingStep(int simEndTime,int argc,char* argv[])
{

    srand (time(NULL));
    simulationStep = 0;
    simulationEndTime = 0;
    distributor = new distribute(argc,argv);

    /////////////////////////// Network parsing started //////////////////////////
    NetworkParse np;
    linkList = *np.getLinkList();
	
    numberOfSegments = np.getNumberOfSegments();
    VehicleLoad vl;
    demandList = vl.getDemand();
	
    ///////////////////////// Demand file parsing ///////////////////////
    int index;
    for(index=0;index<demandList.size();index++)
    {
        nextGenerationTime.push_back(1);
        double demand = (double)demandList[index].getDemand();
        double demandRatio = 3600/demand;
        if(demandRatio>1)
            numberOfVehiclesToGenerate.push_back(1);
        else
        {
            numberOfVehiclesToGenerate.push_back((int)(1/demandRatio));
        }

    }
    simulationEndTime = simEndTime;


    /*
    changed the code so that
    next part is done by only distributor.id 0 and sent to others
    this should be tested to see it works
    */
    
    if(distributor->id==0){
        partition.setNetwork(linkList);
        partition.doPartition();

        int sendLinkInfo[partition.output.size()][linkList.size()];
        for(int i=0;i<partition.output.size();i++){
            for(int j=0;j<partition.output.size();j++){
                sendLinkInfo[i][j]=0;
            }
        }

        for(int i=0;i<partition.output.size();i++){
            if(distributor->id==0)
                cout<<i<<" in partition "<<partition.output[i]<<endl;
            for(int j=0;j<linkList.size();j++){
                Link lnk = linkList[j];
                if(lnk.DnNodeId == i || lnk.UpNodeId == i){
                    sendLinkInfo[partition.output[i]][j]=1;
                }
            }
        }

        for(int i=1;i<COMP;i++){
            distributor->sendData(sendLinkInfo[i],linkList.size(),i);

        }
        for(int i=0;i<linkList.size();i++){
            linkListShared.push_back(sendLinkInfo[0][i]);
        }
    }
    if(distributor->id!=0){
        distributor->receiveData(linkList.size(),linkListShared);
        for(int i=0;i<linkListShared.size();i++){
            cout<<linkListShared[i]<<"  ";
        }
        cout<<endl;
    }



    /// This previous has to be done in master node and then we have to partiotion our road network
    /// after that we have to send our partitioned network to each slave node

    paintComponent();

}

DrawingStep::~DrawingStep()
{
    delete distributor;
}


void DrawingStep::paintComponent(){


    /////////////// This is the main function where everything has been calculated and simulated /////////////////

    //////////// make two portion one for slave and another for master node ///////////////////////

    /*        In master portion
     *      Drawing part to be included and in each iteration mpi_recv will be called and draw will be called to show Network
     *      in slave portion only calculation should be done and send after finishing....
     */
    while(true){
        simulationStep++;
        if(simulationStep==simulationEndTime){
   //         if(distributor->id==0){
                cout<<endl<<endl<<"In master node report:"<<endl;
                double sensorVehicleCount[numberOfSegments];
                double sensorVehicleAvgSpeed[numberOfSegments];
                int index, index2, ct = 0;
                for(index=0;index<linkList.size();index++)
                {
                    for(index2=0;index2<linkList[index].getSegmentCount();index2++)
                    {
                        sensorVehicleCount[ct]=linkList[index].getSegment(index2)->getSensorVehicleCount();
                        sensorVehicleAvgSpeed[ct]=linkList[index].getSegment(index2)->getSensorVehicleAvgSpeed();
                        ct++;
                    }
                }
                cout<<"Vehicle Count per link: "<<endl;
                for(int i=0;i<ct;i++){
                    distributor->getName();
                    cout<<i<<" th link : "<<sensorVehicleCount[i]<<endl;
                }
                cout<<"Avg speed per link: "<<endl;
                for(int i=0;i<ct;i++){
                    distributor->getName();
                    cout<<i<<" th link : "<<sensorVehicleAvgSpeed[i]<<endl;
                }
  //         }

            simulationStep++;
            return;
        }
        //drawRoadNetwork();
        ///////////////////// Removing vehicle from road network //////////////////////
        int index;
        for(index=0;index<vehicleRemovalList.size();index++)
        {
            Vehicle* v = vehicleRemovalList[index];
            int index2;
            for(index2=v->getStrip()->getStripIndex();index2<v->getStrip()->getStripIndex()+v->getNumStrip();index2++)
            {
                v->getSegment()->getStrip(index2)->delVehicle(v);
            }
            for(int i=0;i<vehicleList.size();i++)
            {
                if(vehicleList[i]->getVehicleId()==v->getVehicleId())
                {
                    vehicleList.erase(vehicleList.begin()+i);
                }
            }
        }

        ////////////// Deciding move left or right for each vehicle ///////////
        for(index=0;index<vehicleList.size();index++)
        {
            int randomInt = rand();
            if(randomInt%3==0)
                vehicleList[index]->moveright();
            else if(randomInt%3==1)
                vehicleList[index]->moveleft();
        }

        VehicleType* vt = new VehicleType();
        int randomPath, randomSpeed, randomType;
        for(index=0; index<demandList.size(); index++)
        {
            if(nextGenerationTime[index]==simulationStep)
            {
                int numPath = demandList[index].getNumPath();
                //cout<<numPath<<endl;
                //if(numPath==0)continue;
                int index2=0;
                int lastEmptyStrip = 0;
                int index3=0;
                for(index2=0;index2<numberOfVehiclesToGenerate[index];index2++)
                {
                    randomPath = rand();
                    randomSpeed = rand();
                    randomType = rand();
                    int pathSelected = randomPath%numPath;
                    int speedSelected = (randomSpeed%9)+1;
                    int typeSelected = randomType%11;
                    int numStripRequired = vt->numStrip(typeSelected);
                    Path* path = demandList[index].getPath(pathSelected);
                    int firstLinkId = path->getLinkId(0);
                    if(linkListShared[firstLinkId]==0)
                        continue;
                    Link* firstLink = &linkList[firstLinkId];

                    for(index3=lastEmptyStrip;index3+numStripRequired-1<firstLink->getSegment(0)->getStripCount();index3++)
                    {
                        int index4,flag=1;
                        for(index4=index3;index4<index3+numStripRequired;index4++)
                        {
                            if(firstLink->getSegment(0)->getStrip(index4)->isGapforAddingVehicle(vt->Length(typeSelected))==false)
                                flag=0;
                        }
                        if(flag==1)
                        {
                            Vehicle* v = new Vehicle(speedSelected,0,typeSelected,firstLink->getSegment(0),firstLink->getSegment(0)->getStrip(index3),index,pathSelected,0);
                            vehicleList.push_back(v);
                            lastEmptyStrip=index3+numStripRequired;
                            break;
                        }
                    }

                }
                int demand = demandList[index].getDemand();
                double demandRatio = 3600/demand;
                if(demandRatio>1)
                {
                    int nextTime = nextGenerationTime[index]+(int)(demandRatio);
                    nextGenerationTime[index]=nextTime;
                }
                else
                {
                    int nextTime = nextGenerationTime[index]+1;
                    nextGenerationTime[index]=nextTime;

                }
            }
        }

        for(index=0;index<vehicleList.size();index++)
        {
            ////////////// receive from vehicle list from child node //////////////////////

            //drawVehicle(vehicleList[index]);
            if(vehicleList[index]->segmentEnd() == false)
            {

                bool previous = vehicleList[index]->didPassSensor();
                vehicleList[index]->moveforward();
                bool now = vehicleList[index]->didPassSensor();
                if(previous == false && now == true)
                {
                    vehicleList[index]->getSegment()->updateSensorInfo(vehicleList[index]->getSpeed());
                }
            }
            else
            {

                if(vehicleList[index]->getSegment()->lastSegment()==true)
                {
                    Vehicle * v = vehicleList[index];
                    int demandIndex = v->getDemandIndex();
                    int pathIndex = v->getPathIndex();
                    int pathLinkIndex = v->getPathLinkIndex();

                    int lastLinkInPathIndex = demandList[demandIndex].getPath(pathIndex)->pathLength()-1;

                    //Vehicle reaching destination
                    if(pathLinkIndex == lastLinkInPathIndex)
                    {
                        vehicleRemovalList.push_back(vehicleList[index]);
                    }
                    //Vehicle to a new link
                    else
                    {
                        int newlinkIndex = demandList[demandIndex].getPath(pathIndex)->getLinkId(pathLinkIndex+1);
                        if(linkListShared[newlinkIndex]==0)
                            continue;
                        int newSegmentIndex = 0;
                        int newStripIndex = vehicleList[index]->getStrip()->getStripIndex();
                        int index2;
                        int flag =1;
                        for(index2=0;index2<v->getNumStrip();index2++)
                        {
                            if(linkList[newlinkIndex].getSegment(newSegmentIndex)->getStrip(newStripIndex+index2)->isGapforAddingVehicle(v->getLength())==false)
                                flag=0;
                        }
                        // System.out.println(flag);
                        if(flag == 1)
                        {
                            for(index2=v->getStrip()->getStripIndex();index2<v->getStrip()->getStripIndex()+v->getNumStrip();index2++)
                            {
                                v->getSegment()->getStrip(index2)->delVehicle(v);
                            }
                            v->linkChange(pathLinkIndex+1,linkList[newlinkIndex].getSegment(newSegmentIndex), linkList[newlinkIndex].getSegment(newSegmentIndex)->getStrip(newStripIndex));
                        }
                    }
                }
                else
                {
                    Vehicle * v = vehicleList[index];
                    int linkIndex = v->getSegment()->getLinkIndex();
                    if(linkListShared[linkIndex]==0)
                        continue;
                    int segIndex = v->getSegment()->getSegmentIndex();
                    int stripIndex = v->getStrip()->getStripIndex();
                    int index2;
                    int flag =1;

                    for(index2=0;index2<v->getNumStrip();index2++)
                    {
                        if(linkList[linkIndex].getSegment(segIndex+1)->getStrip(stripIndex+index2)->isGapforAddingVehicle(v->getLength())==false)
                            flag=0;
                    }

                    if(flag == 1)
                    {
                        for(index2=v->getStrip()->getStripIndex();index2<v->getStrip()->getStripIndex()+v->getNumStrip();index2++)
                        {
                            v->getSegment()->getStrip(index2)->delVehicle(v);
                        }
                        v->segmentChange(linkList[linkIndex].getSegment(segIndex+1), linkList[linkIndex].getSegment(segIndex+1)->getStrip(stripIndex));
                    }
                }

            }

        }
    }

}
