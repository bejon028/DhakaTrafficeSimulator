#-------------------------------------------------
#
# Project created by QtCreator 2013-12-10T15:03:41
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = userinterface
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    drawingstep.cpp \
    vehicleload.cpp \
    networkparse.cpp

HEADERS  += mainwindow.h \
    drawingstep.h \
    vehicleload.h \
    networkparse.h \
    others.h

FORMS    += mainwindow.ui \
    drawingstep.ui
