#ifndef DRAWINGSTEP_H
#define DRAWINGSTEP_H

#include <QMainWindow>
#include<QGraphicsScene>
#include<vector>
#include"others.h"
#include"plot.h"
using namespace std;

namespace Ui {
class DrawingStep;
}

class DrawingStep : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit DrawingStep(QWidget *parent = 0, int strpPixelCount=1, int simSpeed=1,int simEndTime=120);
    ~DrawingStep();
    void drawRoadNetwork();
    void drawVehicle(Vehicle* v);
    double returnX3(double x1, double y1, double x2, double y2, double distance);
    double returnY3(double x1, double y1, double x2, double y2, double distance);
    double returnX4(double x1, double y1, double x2, double y2, double distance);
    double returnY4(double x1, double y1, double x2, double y2, double distance);



public slots:
    void paintComponent();
    
private:
    Ui::DrawingStep *ui;
    vector<int> s;
    vector<Link> linkList;
    vector<Demand> demandList;
    vector<int> nextGenerationTime;
    vector<int> numberOfVehiclesToGenerate;
    vector<Vehicle*> vehicleList;
    vector<Vehicle*> vehicleRemovalList;
    int simulationStep = 0;
    int simulationEndTime = 0;
    int stripPixelCount = 1;
    int mpRatio = 2;
    int simulationSpeed = 1440;
    double translateX = 0;
    double translateY = 0;
    double scale = 1;
    int referenceX;
    int referenceY;
    int numberOfSegments;
    QTimer *timer;
    QGraphicsScene *canvas;
    plot * p1=NULL,*p2=NULL;

};

#endif // DRAWINGSTEP_H
