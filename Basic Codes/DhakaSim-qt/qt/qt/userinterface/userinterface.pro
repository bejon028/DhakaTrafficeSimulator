#-------------------------------------------------
#
# Project created by QtCreator 2013-12-10T15:03:41
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets printsupport

TARGET = userinterface
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    drawingstep.cpp \
    qcustomplot.cpp \
    plot.cpp

HEADERS  += mainwindow.h \
    drawingstep.h \
    others.h \
    qcustomplot.h \
    plot.h

FORMS    += mainwindow.ui \
    drawingstep.ui \
    plot.ui
