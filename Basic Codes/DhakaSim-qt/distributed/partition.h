#ifndef PARTITION_H
#define PARTITION_H
#include <iostream>
#include <vector>
#include <ctime>
#include <cstdlib>
#include <cmath>
#include "others.h"
#define NODE 7
#define COMP 2
#define SET 20
#define CONST 3
#define GEN 10000
using namespace std;
class SetUp{
public:
    int cost;
    float sd;
    int computer[NODE];
    int node_load[COMP];
};

class Partition
{
public:
    int elite;
    bool matrix[NODE][NODE];
    SetUp old_generation[SET],new_generation[SET];
    Partition();
    void GenerateGeneration();
    void setNetwork(vector<Link> linkList);
    void doPartition();
};
Partition::Partition(){
    elite=0;
    for(int i=0;i<NODE;i++){
        for(int j=0;j<NODE;j++){
            matrix[i][j]=0;
        }
    }
}
void Partition::setNetwork(vector<Link> linkList){
    for(int i=0;i<linkList.size();i++){
        Link lnk = linkList[i];
        matrix[lnk.UpNodeId][lnk.DnNodeId] = 1;
        matrix[lnk.DnNodeId][lnk.UpNodeId] = 1;
    }
}

void Partition::doPartition(){
    int i,j,x,size,k;
    srand(time(NULL));

    //generating the first generation. one set per loop
    for(i=0;i<SET;i++){

        //initialization
        old_generation[i].cost=old_generation[i].sd=0;
        for(j=0;j<COMP;j++)
            old_generation[i].node_load[j]=0;

        //assigning zone(=comp) randomly
        for(j=0;j<NODE;j++){
            x=rand()%COMP;
            old_generation[i].computer[j]=x;
            old_generation[i].node_load[x]++;
        }

        //calculating standard deviation
        for(j=0;j<COMP;j++){
            size=old_generation[i].node_load[j];
            old_generation[i].sd+=(NODE/(COMP*1.00)-size)*(NODE/(COMP*1.00)-size);
        }

        //calculating the cost
        for(j=0;j<NODE;j++)
            for(k=0;k<NODE;k++)
                if(matrix[k][j])
                    if(old_generation[i].computer[j]!=old_generation[i].computer[k])
                        old_generation[i].cost++;

        old_generation[i].cost/=2;
        old_generation[i].sd/=COMP;
        old_generation[i].sd=sqrt(old_generation[i].sd);
    }

    //finding the elite one
    elite=0;
    for(i=1;i<SET;i++){
        if(old_generation[elite].cost+CONST*old_generation[elite].sd>old_generation[i].cost+CONST*old_generation[i].sd) //weighted calculation
            elite=i;
    }

    //generate new generations
    for(i=0;i<GEN;i++)
        GenerateGeneration();

    //give the output
    for(i=0;i<NODE;i++)
        cout<<i<<": "<<old_generation[elite].computer[i]<<endl;

}
void Partition::GenerateGeneration(){
    int i,parent1,parent2,j,k,size,x,y,new_elite;
    for(i=0;i<SET;i++){
        //select parents
        parent1=rand()%SET;
        parent2=rand()%SET;
        while(parent2==parent1)
            parent2=rand()%SET;

        //initialization
        new_generation[i].cost=new_generation[i].sd=0;
        for(j=0;j<COMP;j++)
            new_generation[i].node_load[j]=0;
        j=-1;

        //concat
        while(++j<NODE/2){
            new_generation[i].computer[j%NODE]=old_generation[parent1].computer[j%NODE];
            new_generation[i].node_load[old_generation[parent1].computer[j%NODE]]++;
        }
        new_generation[i].computer[j%NODE]=old_generation[parent2].computer[j%NODE];
        new_generation[i].node_load[old_generation[parent2].computer[j%NODE]]++;
        while(++j<NODE){
            new_generation[i].computer[j%NODE]=old_generation[parent2].computer[j%NODE];
            new_generation[i].node_load[old_generation[parent2].computer[j%NODE]]++;
        }

        //select one for random mutation and mutate
        x=rand()%NODE;
        y=rand()%COMP;
        if(new_generation[i].computer[x]==y){}
        else{
            new_generation[i].node_load[new_generation[i].computer[x]]--;
            new_generation[i].computer[x]=y;
            new_generation[i].node_load[y]++;
        }

        //calculate cost and standard deviation
        for(j=0;j<COMP;j++){
            size=new_generation[i].node_load[j];
            new_generation[i].sd+=(NODE/(COMP*1.00)-size)*(NODE/(COMP*1.00)-size);
        }
        for(j=0;j<NODE;j++)
            for(k=0;k<NODE;k++)
                if(matrix[k][j])
                    if(new_generation[i].computer[j]!=new_generation[i].computer[k])
                        new_generation[i].cost++;
        new_generation[i].cost/=2;
        new_generation[i].sd/=COMP;
        new_generation[i].sd=sqrt(new_generation[i].sd);
    }

    //select the elite one
    new_elite=0;
    for(i=1;i<SET;i++){
        if(new_generation[new_elite].cost+CONST*new_generation[new_elite].sd>new_generation[i].cost+CONST*new_generation[i].sd)
            new_elite=i;
    }
    if(new_generation[new_elite].cost+CONST*new_generation[new_elite].sd>old_generation[elite].cost+CONST*old_generation[elite].sd){
        for(i=0;i<SET;i++)
            if(i!=elite){
                old_generation[i].cost=new_generation[i].cost;
                old_generation[i].sd=new_generation[i].sd;
                for(j=0;j<NODE;j++)
                    old_generation[i].computer[j]=new_generation[i].computer[j];
                for(j=0;j<COMP;j++)
                    old_generation[i].node_load[j]=new_generation[i].node_load[j];
            }
    }
    else{
        elite=new_elite;
        for(i=0;i<SET;i++){
            old_generation[i].cost=new_generation[i].cost;
            old_generation[i].sd=new_generation[i].sd;
            for(j=0;j<NODE;j++)
                old_generation[i].computer[j]=new_generation[i].computer[j];
            for(j=0;j<COMP;j++)
                old_generation[i].node_load[j]=new_generation[i].node_load[j];
        }
    }
    // cout<<old_generation[elite].cost<<" "<<old_generation[elite].sd<<endl;


}




#endif // PARTITION_H
