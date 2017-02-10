#ifndef VEHICLELOAD_H
#define VEHICLELOAD_H

#include <vector>
#include <string.h>
#include <string>
#include <iostream>
#include <stdio.h>
#include"others.h"
using namespace std;

class VehicleLoad
{
    vector <Path> pathList;   //include path.h
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


#endif // VEHICLELOAD_H
