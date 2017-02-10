#ifndef PLOT_H
#define PLOT_H

#include <QMainWindow>

namespace Ui {
class plot;
}

class plot : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit plot(QWidget *parent = 0);
    ~plot();
    void plotgraph(double plotdata[], int size, int which);
private:
    Ui::plot *ui;
};

#endif // PLOT_H
