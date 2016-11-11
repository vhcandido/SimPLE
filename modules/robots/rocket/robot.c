#define ID "robot"

#include "module.h"
#include "scene.h"

#include <math.h>


//states
double state[7];
#define px 0
#define py 1
#define vx 2
#define vy 3
#define theta 4
#define fuel 5
#define mass 6

//controls
double control[2];
#define thrust 0
#define epsilon 1


#define FUEL_MAX 0.5
#define MASS 0.5
#define GRAVITY 0.5
#define FUEL_RATE 0.01
#define Vterm 3.0
#define TRmax 1
#define K (TRmax/(Vterm*Vterm))



//#define THmax (3.141592/2)

//in milliseconds
#define DELAY 10

void writeState();
void readControl();
void paintRobot();
void paintTrace();
void paintGround();
void groundColision();

void nextState(double dt);
double noise(double dev);

double MIN(double a, double b){
    return a<b ? a : b;
}
double MAX(double a, double b){
    return a>b ? a : b;
}

void start(){
    state[px] = 0.0;
    state[py] = -10.0;
    state[vx] = 0.0;
    state[vy] = 0.0;
    state[theta] = 0.0;
    state[fuel] = FUEL_MAX;
    state[mass] = MASS + FUEL_MAX ;
    state[vy] = 0.0;

    writeState();
}

void loop(){
    readControl();

    double dt = time_elapsed();

    nextState(dt);

    writeState();

    paintRobot();
    paintTrace();
    paintGround();

    beginScene(0);
        setCamera("camera", state[px], state[py]);
    endScene();

    sleep_ms(DELAY);
}

void writeState(){
    writeDbl("px", state[px]);
    writeDbl("py", state[py]);
    writeDbl("vx", state[vx]);
    writeDbl("vy", state[vy]);

    writeDbl("theta", state[theta]);
    writeDbl("fuel", state[fuel]);
    writeDbl("mass", state[mass]);
}
void readControl(){
    control[thrust] = readDbl("controler.thrust");
    control[epsilon] = readDbl("controler.epsilon");
}


void nextState(double dt){
    //deterministic transition
    state[px] = state[px] + state[vx]*dt;
    state[py] = state[py] + state[vy]*dt;
    if(state[fuel]>0){
        state[vx] = state[vx] + sin(state[theta])*control[thrust]*dt/state[mass];
        state[vy] = state[vy] - cos(state[theta])*control[thrust]*dt/state[mass] + GRAVITY*dt;

        if(state[vx]<0){
            state[vx] += MIN(-state[vx], K*state[vx]*state[vx]*dt);
        }else{
            state[vx] -= MIN(state[vx], K*state[vx]*state[vx]*dt);
        }
        if(state[vy]<0){
            state[vy] += MIN(-state[vy], K*state[vy]*state[vy]*dt);
        }else{
            state[vy] -= MIN(state[vy], K*state[vy]*state[vy]*dt);
        }

        state[fuel] = state[fuel] - FUEL_RATE*control[thrust]*dt;
    }else{
        state[vy] = state[vy] + GRAVITY*dt;
        if(state[vy]<0){
            state[vy] += MIN(-state[vy], K*state[vy]*state[vy]*dt);
        }else{
            state[vy] -= MIN(state[vy], K*state[vy]*state[vy]*dt);
        }

        state[fuel] = 0;
    }
    state[theta] = state[theta] + control[epsilon]*dt;
    state[mass] = MASS + state[fuel];

    //noise added
    state[px] += noiseUniform(0.02)*dt;
    state[py] += noiseUniform(0.02)*dt;
    state[vx] += noiseUniform(0.02)*dt;
    state[vy] += noiseUniform(0.02)*dt;

    state[theta] += noiseUniform(0.02)*dt;
    //state[fuel] += noiseUniform(0.02)*dt;
    //state[mass] += noiseUniform(0.02)*dt; //massa nao pode ser criada nem destruida

    groundColision();
}
#define GROUNDX (10)
#define GROUND (10)
int ground[] = {0,5,10,15,20,13,5,-4,-2,10};
void paintGround(){
    beginScene(20000);
    setStroke("ground", 8);
    setColor("ground", 185, 122, 87, 255);
    int k, i, j;
    for(k=-GROUND; k<=2*GROUND; k++){
        i = (k+GROUND)%GROUND;
        j = (k+1+GROUND)%GROUND;
        drawLine("ground", k*GROUNDX, ground[i], (k+1)*GROUNDX, ground[j]);
    }
    endScene();
}
void groundColision(){
    double sx = state[px];
    if(sx<0){
        int d = (int)(1-sx/(GROUNDX*GROUND));
        sx = sx + d*GROUNDX*GROUND;
    }
    if(sx>=GROUNDX*GROUND){
        int d = (int)(sx/(GROUNDX*GROUND));
        sx = sx - d*GROUNDX*GROUND;
    }
    int i = (int)(sx/GROUNDX);
    double lambda = sx/GROUNDX - i;
    i = i<0 ? 0 : i>=GROUND ? GROUND-1 : i;   //i in [0 .. GROUND[
    int j = (i+1) % GROUND;

    double sy = ground[i]*(1-lambda) + ground[j]*lambda;
    if(state[py]>-0.001+sy){
        state[py] = sy;
        state[vx] = 0;
        if(state[vy]>0.001){
            state[vy] = 0;
        }
    }
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
        double body_size = 0.40;
        double engine_top = (body_size)/8;
        double engine_base = (body_size)/4;
        double engine_length = (body_size*3)/4;



        double fire_pos = engine_length;
        double fire_size = 3*body_size/2;
        double fire_length = 2*engine_base;
        double fire_in = fire_size/4;// body_size/4;

        //set default stroke
        setStroke("body", 1.0);

        //translate to robot center
        setTranslate("body", state[px], state[py]);
        setRotate("body", state[theta]);



        //------------------ draw the fire on engines controls  -----------------
        if(state[fuel]>0){
            setColor("body.engine.fire", RED);
            fillOval("body.engine.fire", -fire_length/2, fire_pos-control[thrust]*fire_in/TRmax, fire_length, fire_size*control[thrust]/TRmax);
        }else{
            //ignore fire
            setColor("body.engine.fire", WHITE);
            fillOval("body.engine.fire", -0.01, -0.01, 0.02, 0.02);
        }

        //------------------ draw the robot body  -----------------
        paintOval("body", 0, 0, body_size/2, body_size/2, LIGHT_GRAY, BLACK);

        //------------------ define the main engine -----------------
        double enginesX[4], enginesY[4];
        enginesX[0] = -engine_top;    enginesY[0] = 0;
        enginesX[1] = +engine_top;    enginesY[1] = 0;
        enginesX[2] = +engine_base;   enginesY[2] = engine_length;
        enginesX[3] = -engine_base;   enginesY[3] = engine_length;

        paintPolygon("body.engine", enginesX, enginesY, 4, GRAY, BLACK);

        //------------------- update to fuel -------------------------

        if(state[fuel]>0){
            double lambda = (FUEL_MAX-state[fuel])/FUEL_MAX;
            enginesX[0] = -engine_top*(1-lambda)-engine_base*lambda;    enginesY[0] = lambda*engine_length;
            enginesX[1] = +engine_top*(1-lambda)+engine_base*lambda;    enginesY[1] = lambda*engine_length;
            paintPolygon("body.fuel", enginesX, enginesY, 4, CYAN, BLUE);
        }else{
            paintPolygon("body.fuel", enginesX, enginesY, 4, GRAY, BLACK);
        }

        paintOval("body.engine.turn", 0, 0, engine_top, engine_top, GRAY, BLACK);

        setRotate("body", -state[theta]);
        //translate back to original place
        setTranslate("body", -state[px], -state[py]);

    endScene();
    //unlock();
}
