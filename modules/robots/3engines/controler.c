#define ID "controler"

#include <math.h>
#include "module.h"
#include "scene.h"


//in milliseconds
#define DELAY 100

#define PI      (3.141592)
#define Amax    (1.0)
#define Vmax    (1.0)
#define Lmax    (PI/2.0)
#define G       (Amax/(Vmax*Vmax))

void start(){
    writeDbl("thrust", 0.0);
    writeDbl("aileron", 0.0);
}
double norm_angle(double ang){
    while(ang>+PI){
        ang -= 2*PI;
    }
    while(ang<-PI){
        ang += 2*PI;
    }
    return ang;
}
double dist(double x1, double y1, double x2, double y2){
    return sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
}
//going to [px py] to destine [dx dy]
void control_to(double px, double py, double theta, double dx, double dy, double* thrust, double* aileron){
    double pa = norm_angle(theta);
    double VA_up = 2.0;

    double des_pa = atan2(dy-py, dx-px);

    double va = (des_pa - pa);			//velocidade angular para o objetivo
	if(va > +PI){
        va = -(2*PI - va);
	}else if(va < -PI){
        va = (2*PI + va);
	}

    va *= VA_up;
    va = between(va, -Lmax, +Lmax);

    double ac = dist(dx, dy, px, py)*G/2;

    double factor = fabs(va);
    ac = ac*(Amax-factor)/(Amax+factor);  //reduz velocidade nas curvas proporcional ao angulo
    ac = between(ac, 0, Amax);

    *thrust = ac;
    *aileron = va;
}
void paintPoint(const char* name, double x, double y){
    beginScene(100);
        paintOval(name, x, y, 0.05, 0.05, ORANGE, BLACK);
    endScene();
}

void loop(){

    double px = readDbl("robot.px");
    double py = readDbl("robot.py");
    double vel = readDbl("robot.vel");
    double theta = readDbl("robot.theta");

    //4 points with (x,y) values
    int N = 5;
    double waypoints[N][2];
    waypoints[0][0] = 0;   waypoints[0][1] = 0;
    waypoints[1][0] = 6;   waypoints[1][1] = 0;
    waypoints[2][0] = 6;   waypoints[2][1] = 4;
    waypoints[3][0] = 3;   waypoints[3][1] = 2;
    waypoints[4][0] = 0;   waypoints[4][1] = 4;

    int n,t;

    //paint route
    beginScene(50);
        setColor("route", BLUE);
        setStroke("route", 2.0);
        for(n=0; n<N; n++){
            t = (n+1)%N;
            drawLine("route", waypoints[n][0], waypoints[n][1], waypoints[t][0], waypoints[t][1]);
        }
    endScene();


    for(n=0; n<N; n++){
        for(t=0; t<100; t++){
            double lambda = t/100.0;
            //destination
            double dx = waypoints[n][0]*(1-lambda) + waypoints[(n+1)%N][0]*lambda;
            double dy = waypoints[n][1]*(1-lambda) + waypoints[(n+1)%N][1]*lambda;
            double thrust, aileron;
            control_to(px, py, theta, dx, dy, &thrust, &aileron);

            writeDbl("thrust", thrust);
            writeDbl("aileron", aileron);

            paintPoint("destine", dx, dy);

            sleep_ms(DELAY);

            px = readDbl("robot.px");
            py = readDbl("robot.py");
            vel = readDbl("robot.vel");
            theta = readDbl("robot.theta");
        }
    }
}
