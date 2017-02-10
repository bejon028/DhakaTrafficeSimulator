#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include"drawingstep.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    
private slots:
    void on_pushButton_clicked();

private:
    Ui::MainWindow *ui;
    DrawingStep *d;
};

#endif // MAINWINDOW_H
