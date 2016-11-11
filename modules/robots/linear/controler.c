#define ID "controler"

#include "module.h"
#include "scene.h"
//in milliseconds
#define DELAY 150

void start(){
    writeDbl("ax", 0.0);
    writeDbl("ay", 0.0);
}

void paintPoint(const char* name, double x, double y){
    beginScene(100);
        paintOval(name, x, y, 0.05, 0.05, ORANGE, BLACK);
    endScene();
}

void loop(){
    double dt = 5.0;

    //going to rigth
    double px = readDbl("robot.px");
    double py = readDbl("robot.py");
    double vx = readDbl("robot.vx");
    double vy = readDbl("robot.vy");

    paintPoint("destine", +4, 0);
    while(px <= +4){
        //px(t+1) = px(t) + vx(t)*dt + ax(t)*dt^2/2
        //1 = px(t) + vx(t)*dt +
        double ax = 2*((+4 - px)/(dt*dt) - vx/dt);
        ax = between(ax, -1, +1);

        //hold py = 0
        double ay = 2*((0.0 - py)/(dt*dt) - vy/dt);
        ay = between(ay, -1, +1);

        writeDbl("ax", ax);
        writeDbl("ay", ay);

        sleep_ms(DELAY);
        px = readDbl("robot.px");
        py = readDbl("robot.py");
        vx = readDbl("robot.vx");
        vy = readDbl("robot.vy");
    }

    //going to left
    paintPoint("destine", -4, 0);
    while(px >= -4){
        //px(t+1) = px(t) + vx(t)*dt + ax(t)*dt^2/2
        //1 = px(t) + vx(t)*dt +
        double ax = 2*((-4 - px)/(dt*dt) - vx/dt);
        ax = between(ax, -1, +1);

        //hold py = 0
        double ay = 2*((0.0 - py)/(dt*dt) - vy/dt);
        ay = between(ay, -1, +1);

        writeDbl("ax", ax);
        writeDbl("ay", ay);

        sleep_ms(DELAY);
        px = readDbl("robot.px");
        py = readDbl("robot.py");
        vx = readDbl("robot.vx");
        vy = readDbl("robot.vy");
    }




}

/*
void loop(){
    //going to rigth
    writeDbl("ax", +1.0);
    while(readDbl("robot.vx") <= +1){
        sleep_ms(DELAY);
    }

    //going to left
    writeDbl("ax", -1.0);
    while(readDbl("robot.vx") >= -1){
        sleep_ms(DELAY);
    }
}
*/
/*
void loop(){
    do{
        writeDbl("ax", +1.0);
        sleep_ms(DELAY);
    }while(readDbl("robot.vx") <= +1);

    do{
        writeDbl("ax", -1.0);
        sleep_ms(DELAY);
    }while(readDbl("robot.vx") >= -1);
}
*/
