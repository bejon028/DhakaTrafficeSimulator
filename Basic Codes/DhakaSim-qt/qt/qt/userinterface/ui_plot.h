/********************************************************************************
** Form generated from reading UI file 'plot.ui'
**
** Created by: Qt User Interface Compiler version 5.3.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_PLOT_H
#define UI_PLOT_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QWidget>
#include "qcustomplot.h"

QT_BEGIN_NAMESPACE

class Ui_plot
{
public:
    QWidget *centralwidget;
    QCustomPlot *widget;
    QMenuBar *menubar;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *plot)
    {
        if (plot->objectName().isEmpty())
            plot->setObjectName(QStringLiteral("plot"));
        plot->resize(555, 431);
        centralwidget = new QWidget(plot);
        centralwidget->setObjectName(QStringLiteral("centralwidget"));
        widget = new QCustomPlot(centralwidget);
        widget->setObjectName(QStringLiteral("widget"));
        widget->setGeometry(QRect(30, 20, 491, 361));
        plot->setCentralWidget(centralwidget);
        menubar = new QMenuBar(plot);
        menubar->setObjectName(QStringLiteral("menubar"));
        menubar->setGeometry(QRect(0, 0, 555, 21));
        plot->setMenuBar(menubar);
        statusbar = new QStatusBar(plot);
        statusbar->setObjectName(QStringLiteral("statusbar"));
        plot->setStatusBar(statusbar);

        retranslateUi(plot);

        QMetaObject::connectSlotsByName(plot);
    } // setupUi

    void retranslateUi(QMainWindow *plot)
    {
        plot->setWindowTitle(QApplication::translate("plot", "MainWindow", 0));
    } // retranslateUi

};

namespace Ui {
    class plot: public Ui_plot {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_PLOT_H
