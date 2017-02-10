#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "drawingstep.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    setAttribute(Qt::WA_DeleteOnClose);
}

MainWindow::~MainWindow()
{
    delete d;
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{

    int x=ui->comboBox->currentIndex();
    int y=ui->comboBox_2->currentIndex();
    d=new DrawingStep(this,x+1,y+1);
    d->show();
}
