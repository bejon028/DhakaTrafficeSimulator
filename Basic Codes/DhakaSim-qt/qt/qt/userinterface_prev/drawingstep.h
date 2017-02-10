#ifndef DRAWINGSTEP_H
#define DRAWINGSTEP_H

#include <QMainWindow>
#include<QGraphicsScene>
#include<vector>
#include"others.h"
using namespace std;

namespace Ui {
class DrawingStep;
}

class DrawingStep : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit DrawingStep(QWidget *parent = 0, int strpPixelCount=1, int simSpeed=1);
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
    vector<Vehicle*> vehicleList;
    vector<Vehicle*> vehicleRemovalList;
    int ct=0;
    int stripPixelCount = 1;
    int mpRatio = 2;
    int simulationSpeed = 1440;
    int val=0;
    QTimer *timer;
    QGraphicsScene *canvas;

};

#endif // DRAWINGSTEP_H
