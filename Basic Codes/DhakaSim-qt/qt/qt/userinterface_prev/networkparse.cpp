#include "networkparse.h"
#include"others.h"
#include<QApplication>
NetworkParse::NetworkParse()
{
    readNetwork();
}

void NetworkParse::readNetwork()
{
    try
        {
            FILE *fp = fopen(fileName.c_str(),"r");
            //qDebug("reading network");
            //printf("reading network");
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
                Link ln(linkId,linkType,UpNodeId,DnNodeId,segmentCount);  //put link related header file on networkparse.h
                linkList.push_back(ln);
                vector<Segment> segmentList;   //put segment related header file on networkparse.h

                int segmentId,stripCount;
                double startPntX,startPntY,endPntX,endPntY;
                for(index2=0; index2<segmentCount; index2++)
                {
                    fscanf(fp,"%c",&str[0]);  //consumes the \n character
                    fscanf(fp,"%d%lf%lf%lf%lf%d",&segmentId,&startPntX,&startPntY,&endPntX,&endPntY,&stripCount);
                    bool lastSegment = false;
                    if(index2==segmentCount-1)
                    {
                        lastSegment = true;
                    }
                    Segment seg(linkId,segmentId,startPntX,startPntY,endPntX,endPntY,stripCount,lastSegment);
                    segmentList.push_back(seg);
                }
                linkList[index].SegementInitialization(segmentList);
            }
            fclose (fp);
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

vector<Node>* NetworkParse::getNodeList()
{
    return &nodeList;
}
 vector<Link>* NetworkParse::getLinkList()
{
    return &linkList;
}

bool NetworkParse::isFileExists(string name)
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



