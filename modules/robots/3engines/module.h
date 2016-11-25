/*
#-----------------------------------------------------------------
#   Project SimPLE (Simulation Process with Less Encode)
#-----------------------------------------------------------------
#   Lib module.h :
#   Generic procedures and function to simplifier the communication
#	between different applications
#-----------------------------------------------------------------
*/

#ifndef MODULE
#define MODULE

#include<stdio.h>
#include<time.h>
#include<stdlib.h>

#ifdef WIN32
#include <windows.h>
#else
#include <unistd.h>
#endif


void start();
void loop();

void sleep_ms(int milliseconds);
double time_sec();
double time_elapsed();

double between(double v, double Min, double Max);
double noiseUniform(double dev);

void writeDbl(const char* name, double value);
void writeInt(const char* name, int value);
double readDbl(const char* path);
int readInt(const char* path);
void lock();
void unlock();

double time_mark = -1;
int main(){
	time_mark = time_sec();

    writeInt("started", 0);
    writeInt("loops", 0);

    start();

    writeInt("started", 1);

    int n=1;
    while(1){
        writeInt("loops", n);
        loop();
        n++;
    }
}

void sleep_ms(int milliseconds){
    #ifdef WIN32
    Sleep(milliseconds);
    #else
    usleep(milliseconds * 1000);
    #endif // win32
}
double time_sec(){
	#ifdef WIN32
	return (double)(clock()) / CLOCKS_PER_SEC;
	#else
	struct timespec spec;
    clock_gettime(CLOCK_REALTIME, &spec);
    return spec.tv_sec+spec.tv_nsec/1e9;
	#endif
}

double time_elapsed(){
    double now = time_sec();
    double elapsed;
    if(time_mark<0){
        elapsed = 1e-6;
    }else{
        elapsed = now-time_mark;
    }
    time_mark = now;
    return elapsed;
}

double between(double v, double Min, double Max){
    return v<Min ? Min : (v>Max ? Max : v);
}
double noiseUniform(double dev){
    double r = ((double)(rand()%RAND_MAX))/RAND_MAX;
    return -dev + r*(2*dev);
}

void writeDbl(const char* name, double value){
    printf("write."ID".%s\n", name);
    printf("%lf\n", value);
    fflush(stdout);
}
void writeInt(const char* name, int value){
    printf("write."ID".%s\n", name);
    printf("%d\n", value);
    fflush(stdout);
}
double readDbl(const char* path){
    double value;
    printf("read.%s\n", path);
    fflush(stdout);
    scanf("%lf", &value);
    return value;
}
int readInt(const char* path){
    int value;
    printf("read.%s\n", path);
    fflush(stdout);
    scanf("%d", &value);
    return value;
}
void lock(){
    printf("lock\n");
    fflush(stdout);
}
void unlock(){
    printf("unlock\n");
    fflush(stdout);
}
#endif
