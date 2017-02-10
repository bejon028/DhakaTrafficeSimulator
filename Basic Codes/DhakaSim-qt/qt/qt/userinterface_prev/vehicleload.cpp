#include "vehicleload.h"
#include"others.h"
#include<stdlib.h>
#include<stdio.h>
#include<QApplication>
VehicleLoad::VehicleLoad()
{
    readPath();
    readDemand();
    addPath();
}

void VehicleLoad::readPath()
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
            int linkId = atoi(V[x].c_str());
            pathList[index].addLinkId(linkId);
        }

    }
    fclose (fp);

    }catch(string s){}

}

void VehicleLoad::readDemand()
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

void VehicleLoad::addPath()
{
    int i,j;
    for(i=0; i<demandList.size(); i++)
    {
        for(j=0;j<pathList.size(); j++)
        {
            qDebug("j:%d",j);
            if(pathList[j].correctPath(demandList[i].getSource(),demandList[i].getDestination()))
            {

                demandList[i].addPath(pathList[j]);
                pathList.erase(pathList.begin()+j);
                j--;
            }
        }
    }
}
vector <Demand> VehicleLoad::getDemand()
{
    return demandList;
}





