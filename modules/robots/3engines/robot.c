#define ID "robot"

#include "module.h"
#include "scene.h"
#include <math.h>

//states
double state[4];
#define px 0
#define py 1
#define vel 2
#define theta 3

//controls
double control[2];
#define thrust 0
#define aileron 1


#define PI      (3.141592)
#define Amax    (1.0)
#define Vmax    (1.0)
#define Lmax    (PI/2.0)
#define G       (Amax/(Vmax*Vmax))

//in milliseconds
#define DELAY 10

void writeState();
void readControl();
void paintRobot();
void paintRobotImg(const char* path, double body_size, double fire_pos);

void nextState(double dt);
double noise(double dev);

void start(){
    //printf("[%s]\n", toStr(123));

    state[px] = 0.0;
    state[py] = 0.0;
    state[vel] = 0.0;
    state[theta] = 0.0;

    writeState();
}
int n=0;
void loop(){
    //printf("ola mundo novo %d\n", n++);

    readControl();
    double dt = time_elapsed();

    nextState(dt);

    writeState();

    //paintRobot();
    //paintRobotImg("./robots/3engines/imgs/shipC.png", 0.4, 0.25);
    //paintRobotImg("./robots/3engines/imgs/shipB.png", 0.4, 0.3);
    paintRobotImg("./robots/3engines/imgs/shipD.png", 0.4, 0.205);

    sleep_ms(DELAY);
}

void writeState(){
    writeDbl("px", state[px]);
    writeDbl("py", state[py]);
    writeDbl("vel", state[vel]);
    writeDbl("theta", state[theta]);
}
void readControl(){
    control[thrust] = readDbl("controler.thrust");
    control[aileron] = readDbl("controler.aileron");
}


void nextState(double dt){
    //deterministic transiction
    state[px] = state[px] + state[vel]*cos(state[theta])*dt + control[thrust]*cos(state[theta])*dt*dt/2.0;
    state[py] = state[py] + state[vel]*sin(state[theta])*dt + control[thrust]*sin(state[theta])*dt*dt/2.0;
    state[vel] = state[vel] + control[thrust]*dt - G*state[vel]*state[vel]*dt;
    state[theta] = state[theta] + control[aileron]*dt;

    //normalize the velocity to be between [0 , Vmax]
    state[vel] = between(state[vel], 0, Vmax);

    //noiose added
    state[px] += noise(0.02)*dt;
    state[py] += noise(0.02)*dt;
    state[vel] += noise(0.02)*dt;
    state[theta] += noise(0.02)*dt;
}
double noise(double dev){
    double r = (rand()%10001)/10000.0;
    return -dev + r*(2*dev);
}

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

void paintRobotImg(const char* path, double body_size, double fire_pos){
    //lock();
    beginScene(500000);
        double fire_factor = 0.35;
        double fire_pos_main = (body_size)/2;
        double fire_pos_seg = (body_size*5)/8;
        double fire_size = body_size*3;
        double fire_length = (body_size*2)/5;

        //set default stroke
        setStroke("body", 1.0);
        //translate to robot center
        setTranslate("body", state[px], state[py]);
        //rotate to the robot direction
        setRotate("body", state[theta]);

        //------------------ draw the fire on engines controls  -----------------
        setColor("body.engine.fire", RED);
        fillOval("body.engine.fire.main", -fire_pos_main - fire_size*control[thrust]/Amax, -fire_length/2, fire_size*control[thrust]/Amax, fire_length);
        //setColor("body.engine.fire.left", GREEN);
        if(control[aileron]<0){
            fillOval("body.engine.fire.left", -fire_pos_seg + fire_size*fire_factor*control[aileron]/Lmax, - fire_length*fire_factor/2 + fire_pos, -fire_size*fire_factor*control[aileron]/Lmax, fire_length*fire_factor);
        }else{
            fillOval("body.engine.fire.right", -fire_pos_seg - fire_size*fire_factor*control[aileron]/Lmax, - fire_length*fire_factor/2 - fire_pos, fire_size*fire_factor*control[aileron]/Lmax, fire_length*fire_factor);
        }
        //drawLine("line", 0, 0, 1, 0);
        //sleep_ms(77);

        //------------------ draw the robot body  -----------------
        setRotate("body.img", +PI/2);
        drawImage("body.img", path, -body_size, -body_size, 2*body_size, 2*body_size);
        setRotate("body.img", -PI/2);

        //rotate back to original direction
        setRotate("body", -state[theta]);
        //translate back to original place
        setTranslate("body", -state[px], -state[py]);
    endScene();
    //unlock();
}
