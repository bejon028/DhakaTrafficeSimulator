#include "drawingstep.h"
#include "ui_drawingstep.h"
#include<QTimer>
#include<QGraphicsScene>
#include<QGraphicsTextItem>
#include<qmath.h>
#include"others.h"
#include<stdlib.h>
DrawingStep::DrawingStep(QWidget *parent, int strpPixelCount, int simSpeed, int simEndTime) :
    QMainWindow(parent),
    ui(new Ui::DrawingStep)
{

    ui->setupUi(this);
    canvas=new QGraphicsScene();
    ui->graphicsView->setScene(canvas);
    timer=new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(paintComponent()));
    timer->start(simSpeed);
    //setAttribute(Qt::WA_DeleteOnClose);


    NetworkParse np;
    linkList = *np.getLinkList();
    numberOfSegments = np.getNumberOfSegments();
    VehicleLoad vl;
    demandList = vl.getDemand();
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
    stripPixelCount = strpPixelCount;
    mpRatio = stripPixelCount*2;
    simulationSpeed = simSpeed;
    simulationEndTime = simEndTime;

}

DrawingStep::~DrawingStep()
{
    qDebug("deleting drawing step");
    delete p1;
    delete p2;
    if(timer->isActive())timer->stop();

    delete timer;
    delete canvas;
    delete ui;
}

void DrawingStep::drawRoadNetwork()
{
    for(int i=0; i<linkList.size(); i++)
    {

        for(int j=0; j<linkList[i].getSegmentCount(); j++)
        {
            double  x1 = linkList[i].getSegment(j)->getStartingPntX()*mpRatio;
            double  y1 = linkList[i].getSegment(j)->getStartingPntY()*mpRatio;
            double  x2 = linkList[i].getSegment(j)->getEndPntX()*mpRatio;
            double  y2 = linkList[i].getSegment(j)->getEndPntY()*mpRatio;

            int a1 = (int)(x1);
            int b1 = (int)(y1);
            int a2 = (int)(x2);
            int b2 = (int)(y2);

            int width = linkList[i].getSegment(j)->getStripCount()*stripPixelCount;

            int a3 = (int)(returnX3(x1,y1,x2,y2,width));
            int b3 = (int)(returnY3(x1,y1,x2,y2,width));
            int a4 = (int)(returnX4(x1,y1,x2,y2,width));
            int b4 = (int)(returnY4(x1,y1,x2,y2,width));
            /* for segment id
            QPainterPath path;

            QFont font;
            font.setPixelSize(10);
            font.setBold(false);
            font.setFamily("Calibri");
            QString s=QString::number(i) +","+QString::number(j);

            path.addText(a1, b1, font, s );

            canvas->addPath(path, QPen(QBrush(Qt::white), 1), QBrush(Qt::white));*/
            canvas->addLine(a1, b1, a2, b2,QPen(Qt::green));
            canvas->addLine(a3, b3, a4, b4,QPen(Qt::green));
        }
    }
}

void DrawingStep::drawVehicle(Vehicle *v)
{
    Segment* seg = v->getSegment();
    double segmentLength = qSqrt(qPow(seg->getStartingPntX()-seg->getEndPntX(), 2)+qPow(seg->getStartingPntY()-seg->getEndPntY(), 2));
    int length = (int) qCeil(v->getLength());

    double xp = (v->getDistance()*seg->getEndPntX()+(segmentLength-v->getDistance())*seg->getStartingPntX())/segmentLength*mpRatio;
    double yp = (v->getDistance()*seg->getEndPntY()+(segmentLength-v->getDistance())*seg->getStartingPntY())/segmentLength*mpRatio;
    double xq = ((v->getDistance()+length)*seg->getEndPntX()+(segmentLength-(v->getDistance()+length))*seg->getStartingPntX())/segmentLength*mpRatio;
    double yq = ((v->getDistance()+length)*seg->getEndPntY()+(segmentLength-(v->getDistance()+length))*seg->getStartingPntY())/segmentLength*mpRatio;


    int x1 = (int)(returnX3(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount));
    int y1 = (int)(returnY3(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount));
    int x2 = (int)(returnX4(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount));
    int y2 = (int)(returnY4(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount));

    int width = (int) qCeil(v->getWidth())*stripPixelCount;

    int x3 = (int)(returnX3(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount+width));
    int y3 = (int)(returnY3(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount+width));
    int x4 = (int)(returnX4(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount+width));
    int y4 = (int)(returnY4(xp,yp,xq,yq,(v->getStrip()->getStripIndex())*stripPixelCount+width));
    QPen p(Qt::red);
    int type = v->getType();
    if(type == 1)
        p.setColor(Qt::blue);
    else if(type == 2)
        p.setColor(Qt::cyan);
    else if(type == 3)
        p.setColor(Qt::darkGray);
    else if(type == 4)
        p.setColor(Qt::green);
    else if(type == 5)
        p.setColor(Qt::magenta);
    else if(type == 6)
        p.setColor(Qt::darkRed);
    else if(type == 7)
        p.setColor(Qt::darkYellow);
    else if(type == 8)
        p.setColor(Qt::red);
    else if(type == 9)
        p.setColor(Qt::white);
    else if(type == 10)
        p.setColor(Qt::yellow);
    canvas->addLine(x1,y1,x2,y2,p);
    canvas->addLine(x3,y3,x4,y4,p);
    canvas->addLine(x1,y1,x3,y3,p);
    canvas->addLine(x2,y2,x4,y4,p);
}

void DrawingStep::paintComponent(){
    simulationStep++;
    if(simulationStep==simulationEndTime){
        timer->stop();
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
        double y[] = {100,119,120};
        p1 = new plot();
        p2 = new plot();
        p1->setWindowTitle("SensorVehicleCountPlot");
        p2->setWindowTitle("SensorVehicleAvgSpeedPlot");
        p1->plotgraph(sensorVehicleCount,numberOfSegments,1);
        p2->plotgraph(sensorVehicleAvgSpeed,numberOfSegments,0);
        p1->show();
        p2->show();

        simulationStep++;
        return;
    }
    canvas->clear();

    canvas->setBackgroundBrush(QBrush(Qt::black));
    drawRoadNetwork();
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
    for(index=0;index<vehicleList.size();index++)
    {
        int randomInt = qrand();
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
            int index2=0;
            int lastEmptyStrip = 0;
            int index3=0;
            for(index2=0;index2<numberOfVehiclesToGenerate[index];index2++)
            {
                randomPath = qrand();
                randomSpeed = qrand();
                randomType = qrand();
                int pathSelected = randomPath%numPath;
                int speedSelected = (randomSpeed%9)+1;
                int typeSelected = randomType%11;
                int numStripRequired = vt->numStrip(typeSelected);
                Path* path = demandList[index].getPath(pathSelected);
                int firstLinkId = path->getLinkId(0);
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
        drawVehicle(vehicleList[index]);
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
                    int segIndex = v->getSegment()->getSegmentIndex();
                    int stripIndex = v->getStrip()->getStripIndex();
                    int index2;
                    int flag =1;
                    for(index2=0;index2<v->getNumStrip();index2++)
                    {
                        if(linkList[linkIndex].getSegment(segIndex+1)->getStrip(stripIndex+index2)->isGapforAddingVehicle(v->getLength())==false)
                            flag=0;
                    }
                   // System.out.println(flag);
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


double DrawingStep::returnX3(double x1, double y1, double x2, double y2, double distance)
{
    double x3 = ((x1-x2)*(x1*(x1-x2)+y1*(y1-y2))-(y1-y2)*(-distance*qSqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    return x3;
}

double DrawingStep::returnY3(double x1, double y1, double x2, double y2, double distance)
{
    double y3 = ((x1-x2)*(-distance*qSqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2)+(y1-y2)*(x1*(x1-x2)+y1*(y1-y2)))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    return y3;
}

double DrawingStep::returnX4(double x1, double y1, double x2, double y2, double distance)
{
    double x4 = ((x1-x2)*(x2*(x1-x2)+y2*(y1-y2))-(y1-y2)*(-distance*qSqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    return x4;
}

double DrawingStep::returnY4(double x1, double y1, double x2, double y2, double distance)
{
    double y4 = ((x1-x2)*(-distance*qSqrt((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2))-x2*y1+x1*y2)+(y1-y2)*(x2*(x1-x2)+y2*(y1-y2)))/((y1-y2)*(y1-y2)+(x1-x2)*(x1-x2));
    return y4;
}
