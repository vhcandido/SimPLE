#define ID "robot"

#include "module.h"
#include "scene.h"
#include <math.h>

//states
double state[4];
#define px 0
#define py 1
#define theta 2
#define batery 3

//controls
int control[4];
#define senseL 0
#define senseR 1
#define pwmL 2
#define pwmR 3

//belt position (used only for paint)
double belt[2];

#define PI      (3.141592)
#define PWM_MIN (70)
#define V_MAX   (0.3)

//in milliseconds
#define DELAY   (10)

#define max(a,b) (a>b ? a : b)

void writeState();
void readControl();
void paintTrace();
void paintRobot();
void paintRobotImg(const char* path, double body_size, double belt_size);

void nextBelt(double dt, double size);
void nextState(double dt, double diameter);

double noise(double dev);

void start(){
    //printf("[%s]\n", toStr(123));

    state[px] = 0.0;
    state[py] = 0.0;
    state[theta] = 0.0;
    state[batery] = 100.0;

    belt[0] = 0.0;
    belt[1] = 0.0;


    writeState();
}
void loop(){
    readControl();
    double dt = time_elapsed();

    nextState(dt, 0.1);
    nextBelt(dt, 0.16);

    writeState();

    //paintRobot();
    //paintRobotImg("./robots/3engines/imgs/shipC.png", 0.4, 0.25);
    //paintRobotImg("./robots/3engines/imgs/shipB.png", 0.4, 0.3);
    paintRobotImg("./robots/belt/imgs/shipD.png", 0.1, 0.16);
    paintTrace();

    sleep_ms(DELAY);
}

void writeState(){
    writeDbl("px", state[px]);
    writeDbl("py", state[py]);
    writeDbl("theta", state[theta]);
    writeDbl("batery", state[batery]);
}
void readControl(){
    control[senseL] = readInt("controler.senseL");
    control[senseR] = readInt("controler.senseR");
    control[pwmL] = readInt("controler.pwmL");
    control[pwmR] = readInt("controler.pwmR");
}
void nextBelt(double dt, double size){
    double L = max(control[pwmL]-PWM_MIN, 0);
    double R = max(control[pwmR]-PWM_MIN, 0);

    double vl = L*V_MAX*(control[senseL]*2.0-1.0)/(255.0 - PWM_MIN);
    double vr = R*V_MAX*(control[senseR]*2.0-1.0)/(255.0 - PWM_MIN);
    belt[0] -= vl*dt;
    belt[1] -= vr*dt;
    while(belt[0]>size){
        belt[0] -= size;
    }
    while(belt[0]<0){
        belt[0] += size;
    }
    while(belt[1]>size){
        belt[1] -= size;
    }
    while(belt[1]<0){
        belt[1] += size;
    }
}
/**
let be:
vl : the control velocity on belt left
vr : the control velocity on belt rigth
D  : the robot distance from its center to its belts
so we have:
angle = (vl-vr)*dt/(4*PI*D) : the angular variation
radius = D*(vl+vr)/(vl-vr)  : the translate radium
rot = theta(t) - PI/2       : theta rotated
-------------------------------------------------------
px(t+1) = px(t) + [ cos(rot+angle) - cos(rot) ]*radius;
py(t+1) = py(t) + [ sin(rot+angle) - sin(rot) ]*radius;
theta(t+1) = theta(t) + angle;
*/
void nextState(double dt, double D){

    double pwmL_noise = max(control[pwmL]-PWM_MIN+noise(10.0)*dt, 0);
    double pwmR_noise = max(control[pwmR]-PWM_MIN+noise(10.0)*dt, 0);

    //double pwmL_noise = max(control[pwmL]-PWM_MIN, 0);
    //double pwmR_noise = max(control[pwmR]-PWM_MIN, 0);
    double vl = pwmL_noise*V_MAX*(control[senseL]*2.0-1.0)/(255.0 - PWM_MIN);
    double vr = pwmR_noise*V_MAX*(control[senseR]*2.0-1.0)/(255.0 - PWM_MIN);

    //deterministic transiction
    if(fabs(vl-vr)<0.001*V_MAX){
        double vm = (vl+vr)/2;
        state[px] = state[px] + vm*cos(state[theta])*dt;
        state[py] = state[py] + vm*sin(state[theta])*dt;
        //state[theta] = state[theta];
    }else{
        double radius = D*(vl+vr)/(vl-vr);
        double angle = dt*(vl-vr)/(4*PI*D);  //angle variation
        double rot = state[theta]-PI/2;
        state[px] = state[px] + (cos(rot+angle)-cos(rot))*radius;
        state[py] = state[py] + (sin(rot+angle)-sin(rot))*radius;
        state[theta] = state[theta] + angle;
    }
    state[batery] = state[batery] - dt*(control[pwmL]+control[pwmR])/512.0;
    state[batery] = max(state[batery], 0);



    //noiose added
    state[px] += noise(0.02)*dt;
    state[py] += noise(0.02)*dt;
    state[theta] += noise(0.02*PI)*dt;
    //state[batery] += noise(0.02)*dt;

}
double noise(double dev){
    double r = (rand()%10001)/10000.0;
    return -dev + r*(2*dev);
}
int n=0;
void paintTrace(){
    beginScene(510000);
        setColor("trace", GRAY);
    endScene();
    beginScene(510001+n);
        fillOval("trace", state[px]-0.02, state[py]-0.02, 0.04, 0.04);
    endScene();
    n = (n+1)%1000;
}

/*
void paintRobot(){
    //lock();
    beginScene(500000);
        double body_size = 0.20;
        double fire_factor = 0.5;
        double fire_pos = (body_size)/4;
        double fire_size = body_size*2;
        double fire_length = (body_size*3)/5;

        //set default stroke
        setStroke("body", 1.0);
        //translate to robot center
        setTranslate("body", state[px], state[py]);
        //rotate to the robot direction
        setRotate("body", state[theta]);

        //------------------ define the three engines -----------------
        double enginesX[3][3], enginesY[3][3];
        int i;
        for(i=0; i<3; i++){
            enginesX[i][0] = body_size/2;
            enginesY[i][0] = 0;
        }
        //main engine
        enginesX[0][1] = -(body_size)/4;  enginesY[0][1] = -(body_size*3)/8;
        enginesX[0][2] = -(body_size)/4;  enginesY[0][2] = +(body_size*3)/8;
        //right engine
        enginesX[1][1] = -(body_size)/4;  enginesY[1][1] = -(body_size*7)/8;
        enginesX[1][2] = -(body_size)/4;  enginesY[1][2] = -(body_size*5)/8;
        //left engine
        enginesX[2][1] = -(body_size)/4;  enginesY[2][1] = +(body_size*5)/8;
        enginesX[2][2] = -(body_size)/4;  enginesY[2][2] = +(body_size*7)/8;

        //------------------ draw the fire on engines controls  -----------------
        setColor("body.engine.fire", RED);
        fillOval("body.engine.fire.main", -fire_pos - fire_size*control[thrust]/Amax, -fire_length/2, fire_size*control[thrust]/Amax, fire_length);
        //setColor("body.engine.fire.left", GREEN);
        if(control[aileron]<0){
            fillOval("body.engine.fire.left", -fire_pos + fire_size*fire_factor*control[aileron]/Lmax, - fire_length*fire_factor/2 + (body_size*6)/8, -fire_size*fire_factor*control[aileron]/Lmax, fire_length*fire_factor);
        }else{
            fillOval("body.engine.fire.right", -fire_pos - fire_size*fire_factor*control[aileron]/Lmax, - fire_length*fire_factor/2 - (body_size*6)/8, fire_size*fire_factor*control[aileron]/Lmax, fire_length*fire_factor);
        }
        drawLine("line", 0, 0, 1, 0);
        //sleep_ms(77);

        //------------------ draw the the engines structure  -----------------
        for(i=0; i<3; i++){
            paintPolygon("body.engine", enginesX[i], enginesY[i], 3, GRAY, BLACK);
        }

        //------------------ draw the robot body  -----------------
        setColor("body", LIGHT_GRAY);
        fillArc("body", -body_size/2, -body_size/2, body_size, body_size, -90, 180);
        setColor("body", BLACK);   //BLACK
        drawArc("body", -body_size/2, -body_size/2, body_size, body_size, -90, 180);
        drawLine("body", 0, -body_size/2, 0, body_size/2);

        //rotate back to original direction
        setRotate("body", -state[theta]);
        //translate back to original place
        setTranslate("body", -state[px], -state[py]);
    endScene();
    //unlock();
}
*/
void paintRobotImg(const char* path, double body_size, double belt_size){
    //lock();
    beginScene(500000);
        //set default stroke
        setStroke("body", 1.0);
        //translate to robot center
        setTranslate("body", state[px], state[py]);
        //rotate to the robot direction
        setRotate("body", state[theta]);


        //sleep_ms(77);

        //------------------ draw the robot body  -----------------
        setRotate("body.img", +PI/2);
        drawImage("body.img", path, -body_size, -body_size, 2*body_size, 2*body_size);

        setColor("body.direction", RED);
        drawLine("body.direction", 0, 0, 0, -0.5);

        int i;
        double bl = belt[0];
        for(i=0; i<5; i++){
            bl = (bl+belt_size/5);
            if(bl>belt_size){
                bl -= belt_size;
            }
            drawLine("body.beltL", -body_size*0.61, bl-belt_size/2, -body_size*0.76, bl-belt_size/2);
        }
        double br = belt[1];
        for(i=0; i<5; i++){
            br = (br+belt_size/5);
            if(br>belt_size){
                br -= belt_size;
            }
            drawLine("body.beltR", body_size*0.61, br-belt_size/2, body_size*0.76, br-belt_size/2);
        }

        setRotate("body.img", -PI/2);

        //rotate back to original direction
        setRotate("body", -state[theta]);
        //translate back to original place
        setTranslate("body", -state[px], -state[py]);
    endScene();
    //unlock();
}
