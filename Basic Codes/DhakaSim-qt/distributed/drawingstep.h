#ifndef DRAWINGSTEP_H
#define DRAWINGSTEP_H

#include <vector>
#include "distribute.h"
#include "others.h"
using namespace std;


class DrawingStep{

public:
    explicit DrawingStep(int simEndTime,int argc,char* argv[]);
    ~DrawingStep();
    void paintComponent();
    
private:
    distribute* distributor;
    vector<int> s;
    vector<Link> linkList;
    vector<Demand> demandList;
    vector<int> nextGenerationTime;
    vector<int> numberOfVehiclesToGenerate;
    vector<Vehicle*> vehicleList;
    vector<Vehicle*> vehicleRemovalList;
    vector<int> linkListShared;
    int simulationStep;
    int simulationEndTime;
    int numberOfSegments;
    Genetic partition;

};

#endif // DRAWINGSTEP_H
