#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "drawingstep.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    //setAttribute(Qt::WA_DeleteOnClose);
}

MainWindow::~MainWindow()
{
    qDebug("deleting main step");
    delete d;
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{

    int x=ui->comboBox->currentIndex();
    int y=ui->comboBox_2->currentIndex();
    int z=ui->lineEdit->text().toInt();
    try{
    if(y<4)d=new DrawingStep(this,x+1,200*(4-y),z);
    else d=new DrawingStep(this,x+1,17,z);
    d->setWindowTitle("DhakaSIM");
    d->show();
    }catch(const std::bad_alloc e){
        qDebug(e.what());
    }
}
