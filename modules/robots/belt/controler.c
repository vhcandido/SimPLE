#define ID "controler"

#include <math.h>
#include "module.h"
#include "scene.h"


//in milliseconds
#define DELAY   (100)
//#define PWM_MIN (70)
//#define V_MAX   (0.1)


void start(){
    writeInt("senseL", 0);
    writeInt("senseR", 1);
    writeInt("pwmL", 255);
    writeInt("pwmR", 255);
}

void loop(){
    sleep_ms(DELAY);
}
