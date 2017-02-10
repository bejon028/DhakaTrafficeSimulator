#include "plot.h"
#include "ui_plot.h"

plot::plot(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::plot)
{
    ui->setupUi(this);
}

plot::~plot()
{
    delete ui;
}

void plot::plotgraph(double plotdata[], int size,int which)
{
    ui->widget->xAxis->setLabel("Link No");
    if(which){
        ui->widget->yAxis->setLabel("No of Vehicle");
    }else{
        ui->widget->yAxis->setLabel("Average Speed of Vehicles");
    }

    QCPBars *myBars = new QCPBars(ui->widget->xAxis, ui->widget->yAxis);
    ui->widget->addPlottable(myBars);
    QVector<double> keyData;
    QVector<double> valueData;
    for(int i=0;i<size;i++){
        keyData << i;
        valueData << plotdata[i];
    }
    myBars->setData(keyData, valueData);
    myBars->setName("abs");
    ui->widget->rescaleAxes(true);
    ui->widget->replot();

}
