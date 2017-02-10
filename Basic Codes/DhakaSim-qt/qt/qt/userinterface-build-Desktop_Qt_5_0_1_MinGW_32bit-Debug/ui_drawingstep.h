/********************************************************************************
** Form generated from reading UI file 'drawingstep.ui'
**
** Created by: Qt User Interface Compiler version 5.0.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_DRAWINGSTEP_H
#define UI_DRAWINGSTEP_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QGraphicsView>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_DrawingStep
{
public:
    QWidget *centralwidget;
    QGraphicsView *graphicsView;
    QMenuBar *menubar;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *DrawingStep)
    {
        if (DrawingStep->objectName().isEmpty())
            DrawingStep->setObjectName(QStringLiteral("DrawingStep"));
        DrawingStep->resize(1300, 690);
        DrawingStep->setMinimumSize(QSize(1300, 690));
        DrawingStep->setMaximumSize(QSize(1300, 700));
        DrawingStep->setBaseSize(QSize(15, 15));
        centralwidget = new QWidget(DrawingStep);
        centralwidget->setObjectName(QStringLiteral("centralwidget"));
        graphicsView = new QGraphicsView(centralwidget);
        graphicsView->setObjectName(QStringLiteral("graphicsView"));
        graphicsView->setGeometry(QRect(0, 0, 1300, 741));
        graphicsView->setMinimumSize(QSize(1300, 720));
        DrawingStep->setCentralWidget(centralwidget);
        menubar = new QMenuBar(DrawingStep);
        menubar->setObjectName(QStringLiteral("menubar"));
        menubar->setGeometry(QRect(0, 0, 1300, 21));
        DrawingStep->setMenuBar(menubar);
        statusbar = new QStatusBar(DrawingStep);
        statusbar->setObjectName(QStringLiteral("statusbar"));
        DrawingStep->setStatusBar(statusbar);

        retranslateUi(DrawingStep);

        QMetaObject::connectSlotsByName(DrawingStep);
    } // setupUi

    void retranslateUi(QMainWindow *DrawingStep)
    {
        DrawingStep->setWindowTitle(QApplication::translate("DrawingStep", "MainWindow", 0));
    } // retranslateUi

};

namespace Ui {
    class DrawingStep: public Ui_DrawingStep {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_DRAWINGSTEP_H
