#ifndef NETWORKPARSE_H
#define NETWORKPARSE_H

#include <vector>
#include <string.h>
#include <string>
#include <iostream>
#include <stdio.h>
#include"others.h"
using namespace std;

class NetworkParse
{
    vector <Node> nodeList;   //include node.h
    vector <Link> linkList;   //include link.h
    public:
        string fileName = "Network.txt";
        NetworkParse();
        void readNetwork();
        vector <Node>* getNodeList();
        vector <Link>* getLinkList();
        bool isFileExists(string fileName);
};


#endif // NETWORKPARSE_H
