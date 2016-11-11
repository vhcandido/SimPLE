#-----------------------------------------------------------------
#   Project SimPLE (Simulation Process with Less Encode)
#   Node gps - to read robot.px, robot.py values and generate
#   gps.px, gps.py values by adding a gausian white noise.
#   The frequence of operation is 100 milliseconds or 10 hertz
#-----------------------------------------------------------------

from module import *;
from scene import *;
from random import gauss;

def start():
    writeDbl("px", 0);
    writeDbl("py", 0);
    
    #beginScene(300);
    #drawString("str", "gps: ( &(gps.px) , &(gps.py) )",
    #           "$(&(robot.px)-0.3)", "$(&(robot.py)-0.5)");
    #paintOvalExpr("point", "&(gps.px)", "&(gps.py)", 0.05, 0.05, BLUE, BLACK);
    #endScene();

def paintGps(x, y, gx, gy):
    beginScene(200);
    paintOval("point", gx, gy, 0.05, 0.05, BLUE, BLACK);
    drawString("str", "gps: ( "+str(gx)+" , "+str(gy)+" )", x-0.3, y-0.5);
    endScene();

def loop():
    x = readDbl("robot.px");
    y = readDbl("robot.py");

    gx = x + gauss(0, 0.1);   #sigma = 0.1
    gy = y + gauss(0, 0.1);   #sigma = 0.1

    writeDbl("px", gx);
    writeDbl("py", gy);

    paintGps(x, y, gx, gy);
    
    sleep_ms(100);


if __name__=="__main__":
    main("gps", start, loop);
    

