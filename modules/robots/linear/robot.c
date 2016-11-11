#define ID "robot"

#include "module.h"
#include "scene.h"

//states
double state[4];
#define px 0
#define py 1
#define vx 2
#define vy 3

//controls
double control[2];
#define ax 0
#define ay 1

#define Umax 1

//in milliseconds
#define DELAY 10

void writeState();
void readControl();
void paintRobot();
void paintTrace();

void nextState(double dt);
double noise(double dev);

void start(){
    //printf("[%s]\n", toStr(123));
    //printf("RAND_MAX = %d\n", RAND_MAX);
    //int i;
    //for(i=0; i<10; i++){
    //    printf("rand() = %d\n", rand());
    //}
    state[px] = 0.0;
    state[py] = 0.0;
    state[vx] = 0.0;
    state[vy] = 0.0;

    writeState();
}
//int n=0;
void loop(){
    //printf("ola mundo novo %d\n", n++);

    readControl();
    double dt = time_elapsed();

    nextState(dt);

    writeState();

    paintRobot();
    paintTrace();

    sleep_ms(DELAY);
}

void writeState(){
    writeDbl("px", state[px]);
    writeDbl("py", state[py]);
    writeDbl("vx", state[vx]);
    writeDbl("vy", state[vy]);
}
void readControl(){
    control[ax] = readDbl("controler.ax");
    control[ay] = readDbl("controler.ay");
}


void nextState(double dt){
    //deterministic transiction
    state[px] = state[px] + state[vx]*dt + control[ax]*dt*dt/2.0;
    state[py] = state[py] + state[vy]*dt + control[ay]*dt*dt/2.0;
    state[vx] = state[vx] + control[ax]*dt;
    state[vy] = state[vy] + control[ay]*dt;

    //noiose added
    state[px] += noiseUniform(0.02)*dt;
    state[py] += noiseUniform(0.02)*dt;
    state[vx] += noiseUniform(0.02)*dt;
    state[vy] += noiseUniform(0.02)*dt;
}

int n=0;
void paintTrace(){
    beginScene(10000);
        setColor("trace", GRAY);
    endScene();
    beginScene(10001+n);
        fillOval("trace", state[px]-0.02, state[py]-0.02, 0.04, 0.04);
    endScene();
    n = (n+1)%1000;
}
void paintRobot(){
    //lock();
    beginScene(500000);
        double body_size = 0.20;
        double engine_size = (body_size*3)/4;
        double fire_pos = engine_size;
        double fire_size = body_size*2;
        double fire_length = (body_size*3)/5;

        //set default stroke
        setStroke("body", 1.0);

        //translate to robot center
        setTranslate("body", state[px], state[py]);

        //------------------ define the four engines -----------------
        double enginesX[4][3], enginesY[4][3];
        int i;
        for(i=0; i<4; i++){
            enginesX[i][0] = 0;
            enginesY[i][0] = 0;
        }
        //frist engine
        enginesX[0][1] = -(body_size)/4;  enginesY[0][1] = -(body_size*3)/4;
        enginesX[0][2] = +(body_size)/4;  enginesY[0][2] = -(body_size*3)/4;
        //second engine
        enginesX[1][1] = -(body_size)/4;  enginesY[1][1] = +(body_size*3)/4;
        enginesX[1][2] = +(body_size)/4;  enginesY[1][2] = +(body_size*3)/4;
        //third engine
        enginesX[2][1] = -(body_size*3)/4;  enginesY[2][1] = -(body_size)/4;
        enginesX[2][2] = -(body_size*3)/4;  enginesY[2][2] = +(body_size)/4;
        //four engine
        enginesX[3][1] = +(body_size*3)/4;  enginesY[3][1] = -(body_size)/4;
        enginesX[3][2] = +(body_size*3)/4;  enginesY[3][2] = +(body_size)/4;

        //------------------ draw the fire on engines controls  -----------------
        setColor("body.engine", RED);
        if(control[ax]<0){
            fillOval("body.engine.fire", +fire_pos, -fire_length/2, -fire_size*control[ax]/Umax, fire_length);
        }else{
            fillOval("body.engine.fire", -fire_pos - fire_size*control[ax]/Umax, -fire_length/2, fire_size*control[ax]/Umax, fire_length);
        }
        if(control[ay]<0){
            fillOval("body.engine.fire", -fire_length/2, +fire_pos, fire_length, -fire_size*control[ay]/Umax);
        }else{
            fillOval("body.engine.fire", -fire_length/2, -fire_pos -fire_size*control[ay]/Umax, fire_length, fire_size*control[ay]/Umax);
        }
        //------------------ draw the the engines structure  -----------------
        for(i=0; i<4; i++){
            paintPolygon("body.engine", enginesX[i], enginesY[i], 3, GRAY, BLACK);
        }

        //------------------ draw the robot body  -----------------
        paintOval("body", 0, 0, body_size/2, body_size/2, LIGHT_GRAY, BLACK);

        //translate back to original place
        setTranslate("body", -state[px], -state[py]);

    endScene();
    //unlock();
}
