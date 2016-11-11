from __future__ import print_function
from time import time;
from time import sleep;
from sys import stdout;
from sys import stderr;
from random import uniform;

time_mark = -1;
start_time = -1;
ID = "empty";

def getID():
    return ID;

def main(name, start, loop):
    global ID, start_time, time_mark;
    ID = name;
    time_mark = -1;
    start_time = time();	
    writeInt("started", 0);
    writeInt("loops", 0);

    start();

    writeInt("started", 1);

    n=1;
    while(True):
        writeInt("loops", n);
        loop();
        n=n+1;


def sleep_ms(milliseconds):
    sleep(milliseconds/1e3);    #in seconds

def time_sec():
    return time()-start_time;

def time_elapsed():
    global time_mark;
    now = time_sec();
    elapsed = now;
    if(time_mark>=0):
        elapsed = now-time_mark;
    time_mark = now;
    return elapsed;

def between(v, Min, Max):
    if(v<Min):
        return Min;
    elif(v>Max):
        return Max;
    else:
        return v;

def noiseUniform(dev):
    return uniform(-dev, +dev);	
	
def write(name, value):
    print("write."+ID+"."+name);
    print(str(value));
    stdout.flush();

def writeInt(name, value):
    write(name, value);

def writeDbl(name, value):
    write(name, value);

def readInt(path):
    print("read."+path);
    stdout.flush();
    try:
        value = int(input());
    except ValueError:
	print("Invalid Int Number", file=stderr);
    return value;

def readDbl(path):
    print("read."+path);
    stdout.flush();
    return float(input());
    #try:
    #	value = float(input());
    #except ValueError:
    #	print("Invalid Float Number", file=stderr);
    return value;
	
def lock():
    print("lock");
    stdout.flush();

def unlock():
    printf("unlock");
    stdout.flush();
