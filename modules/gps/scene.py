#-----------------------------------------------------------------
#   Project SimPLE (Simulation Process with Less Encode)
#-----------------------------------------------------------------
#   Lib scene.py :
#   Generical procedures and function to simplifie the comunication
#	with scene.jar aplications thats provide a graphcal 2D interface 
#	to plot several simulated or real values.
#-----------------------------------------------------------------


from module import getID;
from sys import stdout;
#from sys import stderr;

#------------- define several colors (RGBA)-------
WHITE       = [255, 255, 255, 255];
LIGHT_GRAY  = [192, 192, 192, 255];
GRAY        = [128, 128, 128, 255];
DARK_GRAY   = [64,  64,  64,  255];
BLACK       = [0,   0,   0,   255];
RED         = [255, 0,   0,   255];
PINK        = [255, 175, 175, 255];
ORANGE      = [255, 200, 0,   255];
YELLOW      = [255, 255, 0,   255];
GREEN       = [0,   255, 0,   255];
MAGENTA     = [255, 0,   255, 255];
CYAN        = [0,   255, 255, 255];
BLUE        = [0,   0,   255, 255];

def toStr(ord):
    if(ord<0):
        ord = 0;
    elif(ord>999999):
        ord = 999999;
    number = "";
    div = 100000;
    for i in range(0, 6):
        number = number + str((ord/div)%10);
        div = div/10;
    return number;

ordScene = 0;
def beginScene(ord):
    global ordScene;
    ordScene = ord;

def endScene():
    return ordScene;

def setTranslate(name, x, y):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".translate");
    print(str(x)+";"+str(y));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

#theta - the angle of rotation in radians
def setRotate(name, theta):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".rotate");
    print(str(theta));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def setStroke(name, w):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".setStroke");
    print(str(w));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def setColor(name, color):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".setColor");
    print(str(color[0])+";"+str(color[1])+";"+str(color[2])+";"+str(color[3]));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def drawRect(name, x, y, w, h):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawRect");
    print(str(x)+";"+str(y)+";"+str(w)+";"+str(h));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def fillRect(name, x, y, w, h):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".fillRect");
    print(str(x)+";"+str(y)+";"+str(w)+";"+str(h));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def drawOval(name, x, y, w, h):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawOval");
    print(str(x)+";"+str(y)+";"+str(w)+";"+str(h));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def fillOval(name, x, y, w, h):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".fillOval");
    print(str(x)+";"+str(y)+";"+str(w)+";"+str(h));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def drawString(name, string, x, y):
    if "=" in string:
        raise NameError("the string '"+string+"' contains forbid char '='");
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawString");
    print(string+";"+str(x)+";"+str(y));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def drawImage(name, path, x, y, w, h):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawImage");
    print(path+";"+str(x)+";"+str(y)+";"+str(w)+";"+str(h));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

#startAngle and arcAngle values integer and in degrees
def drawArc(name, x, y, w, h, startAngle, arcAngle):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawArc");
    print(str(x)+";"+str(y)+";"+str(w)+";"+str(h)+";"+str(startAngle)+";"+str(arcAngle));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

#startAngle and arcAngle values integer and in degrees
def fillArc(name, x, y, w, h, startAngle, arcAngle):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".fillArc");
    print(str(x)+";"+str(y)+";"+str(w)+";"+str(h)+";"+str(startAngle)+";"+str(arcAngle));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def drawLine(name, x1, y1, x2, y2):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawLine");
    print(str(x1)+";"+str(y1)+";"+str(x2)+";"+str(y2));
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def drawPolygon(name, x, y, n_points):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".drawPolygon");
    line = "";
    for i in range(0, n_points):
        line = line + str(x[i])+";"+str(y[i])+";";
    line = line + str(n_points);
    print(line);
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;

def fillPolygon(name, x, y, n_points):
    global ordScene;
    print("write.scene."+toStr(ordScene)+"."+getID()+"."+name+".fillPolygon");
    line = "";
    for i in range(0, n_points):
        line = line + str(x[i])+";"+str(y[i])+";";
    line = line + str(n_points);
    print(line);
    stdout.flush();
    ordScene = ordScene+1;
    return ordScene;


#paint a rectangle with center on (x,y) coordenate and with radiun (rx, ry) them
#fill with (fill) color and draw the border with (draw) color.
def paintRect(name, x, y, rx, ry, fill, draw):
    setColor(name, fill);
    fillRect(name, x-rx, y-ry, 2*rx, 2*ry);
    setColor(name, draw);
    return drawRect(name, x-rx, y-ry, 2*rx, 2*ry);

def paintRectExpr(name, x, y, rx, ry, fill, draw):
    setColor(name, fill);
    fillRect(name,
            "$("+str(x)+"-"+str(rx)+")", "$("+str(y)+"-"+str(ry)+")",
            "$(2*"+str(rx)+")", "$(2*"+str(ry)+")");
    setColor(name, draw);
    return drawRect(name,
            "$("+str(x)+"-"+str(rx)+")", "$("+str(y)+"-"+str(ry)+")",
            "$(2*"+str(rx)+")", "$(2*"+str(ry)+")");

#paint a oval with center on (x,y) coordenate and with radiun (rx, ry) them
#fill with (fill) color and draw the border with (draw) color.
def paintOval(name, x, y, rx, ry, fill, draw):
    setColor(name, fill);
    fillOval(name, x-rx, y-ry, 2*rx, 2*ry);
    setColor(name, draw);
    return drawOval(name, x-rx, y-ry, 2*rx, 2*ry);

def paintOvalExpr(name, x, y, rx, ry, fill, draw):
    setColor(name, fill);
    fillOval(name,
            "$("+str(x)+"-"+str(rx)+")", "$("+str(y)+"-"+str(ry)+")",
            "$(2*"+str(rx)+")", "$(2*"+str(ry)+")");
    setColor(name, draw);
    return drawOval(name,
            "$("+str(x)+"-"+str(rx)+")", "$("+str(y)+"-"+str(ry)+")",
            "$(2*"+str(rx)+")", "$(2*"+str(ry)+")");

#paint a poligon them fill with (f) color and draw the border with (d) color.
def paintPolygon(name, x, y, n_points, fill, draw):
    setColor(name, fill);
    fillPolygon(name, x, y, n_points);
    setColor(name, draw);
    return drawPolygon(name, x, y, n_points);
