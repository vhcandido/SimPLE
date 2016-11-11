#ifndef SCENE
#define SCENE

#include <stdio.h>

//------------- define several colors (RGBA)-------
#define WHITE       255, 255, 255, 255
#define LIGHT_GRAY  192, 192, 192, 255
#define GRAY        128, 128, 128, 255
#define DARK_GRAY   64,  64,  64,  255
#define BLACK       0,   0,   0,   255
#define RED         255, 0,   0,   255
#define PINK        255, 175, 175, 255
#define ORANGE      255, 200, 0,   255
#define YELLOW      255, 255, 0,   255
#define GREEN       0,   255, 0,   255
#define MAGENTA     255, 0,   255, 255
#define CYAN        0,   255, 255, 255
#define BLUE        0,   0,   255, 255

char number[6];
const char* toStr(int ord){
    if(ord<0){
        ord = 0;
    }else if(ord>999999){
        ord = 999999;
    }
	int i;
	int div = 100000;
	for(i=0; i<6; i++){
		number[i] = '0'+(ord/div)%10;
		div = div/10;
	}
	return &(number[0]);
}

int ordScene = 0;
void beginScene(int ord){
    ordScene = ord;
}
int endScene(){
    return ordScene;
}

int setTranslate(const char* name, double x, double y){
    printf("write.scene.%s."ID".%s.translate\n", toStr(ordScene++), name);
    printf("%lf;%lf\n", x, y);
    fflush(stdout);
    return ordScene;
}
int setCamera(const char* name, double x, double y){
    printf("write.scene.%s."ID".%s.setCamera\n", toStr(ordScene++), name);
    printf("%lf;%lf\n", x, y);
    fflush(stdout);
    return ordScene;
}
/**
theta - the angle of rotation in radians
*/
int setRotate(const char* name, double theta){
    printf("write.scene.%s."ID".%s.rotate\n", toStr(ordScene++), name);
    printf("%lf\n", theta);
    fflush(stdout);
    return ordScene;
}
int setStroke(const char* name, double with){
    printf("write.scene.%s."ID".%s.setStroke\n", toStr(ordScene++), name);
    printf("%lf\n", with);
    fflush(stdout);
    return ordScene;
}
int setColor(const char* name, int r, int g, int b, int a){
    printf("write.scene.%s."ID".%s.setColor\n", toStr(ordScene++), name);
    printf("%d;%d;%d;%d\n", r, g, b, a);
    fflush(stdout);
    return ordScene;
}

int drawRect(const char* name, double x, double y, double w, double h){
    printf("write.scene.%s."ID".%s.drawRect\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf\n", x, y, w, h);
    fflush(stdout);
    return ordScene;
}
int fillRect(const char* name, double x, double y, double w, double h){
    printf("write.scene.%s."ID".%s.fillRect\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf\n", x, y, w, h);
    fflush(stdout);
    return ordScene;
}
int drawOval(const char* name, double x, double y, double w, double h){
    printf("write.scene.%s."ID".%s.drawOval\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf\n", x, y, w, h);
    fflush(stdout);
    return ordScene;
}
int fillOval(const char* name, double x, double y, double w, double h){
    printf("write.scene.%s."ID".%s.fillOval\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf\n", x, y, w, h);
    fflush(stdout);
    return ordScene;
}
int drawString(const char* name, const char* str, double x, double y){
    //TODO
    //if "=" in string:
    //    raise NameError("the string '"+string+"' contains forbid char '='");
    printf("write.scene.%s."ID".%s.drawString\n", toStr(ordScene++), name);
    printf("%s;%lf;%lf\n", str, x, y);
    fflush(stdout);
    return ordScene;
}
int drawImage(const char* name, const char* path, double x, double y, double w, double h){
    printf("write.scene.%s."ID".%s.drawImage\n", toStr(ordScene++), name);
    printf("%s;%lf;%lf;%lf;%lf\n", path, x, y, w, h);
    fflush(stdout);
    return ordScene;
}
int drawArc(const char* name, double x, double y, double w, double h, int startAngle, int arcAngle){
    printf("write.scene.%s."ID".%s.drawArc\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf;%d;%d\n", x, y, w, h, startAngle, arcAngle);
    fflush(stdout);
    return ordScene;
}
int fillArc(const char* name, double x, double y, double w, double h, int startAngle, int arcAngle){
    printf("write.scene.%s."ID".%s.fillArc\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf;%d;%d\n", x, y, w, h, startAngle, arcAngle);
    fflush(stdout);
    return ordScene;
}
int drawLine(const char* name, double x1, double y1, double x2, double y2){
    printf("write.scene.%s."ID".%s.drawLine\n", toStr(ordScene++), name);
    printf("%lf;%lf;%lf;%lf\n", x1, y1, x2, y2);
    fflush(stdout);
    return ordScene;
}

int drawPolygon(const char* name, double x[], double y[], int n_points){
    printf("write.scene.%s."ID".%s.drawPolygon\n", toStr(ordScene++), name);
    int i;
    for(i=0; i<n_points; i++){
        printf("%lf;%lf;", x[i], y[i]);
    }
    printf("%d\n", n_points);
    fflush(stdout);
    return ordScene;
}
int fillPolygon(const char* name, double x[], double y[], int n_points){
    printf("write.scene.%s."ID".%s.fillPolygon\n", toStr(ordScene++), name);
    int i;
    for(i=0; i<n_points; i++){
        printf("%lf;%lf;", x[i], y[i]);
    }
    printf("%d\n", n_points);
    fflush(stdout);
    return ordScene;
}
/**
paint a rectangle with center on (x,y) coordenate and with radiun (rx, ry) them
fill with (f) color and draw the border with (d) color.
*/
void paintRect(const char* name, double x, double y, double rx, double ry, int fr, int fg, int fb, int fa, int dr, int dg, int db, int da){
    setColor(name, fr, fg, fb, fa);
    fillRect(name, x-rx, y-ry, 2*rx, 2*ry);
    setColor(name, dr, dg, db, da);
    drawRect(name, x-rx, y-ry, 2*rx, 2*ry);
}
/**
paint a oval with center on (x,y) coordenate and with radiun (rx, ry) them
fill with (f) color and draw the border with (d) color.
*/
void paintOval(const char* name, double x, double y, double rx, double ry, int fr, int fg, int fb, int fa, int dr, int dg, int db, int da){
    setColor(name, fr, fg, fb, fa);
    fillOval(name, x-rx, y-ry, 2*rx, 2*ry);
    setColor(name, dr, dg, db, da);
    drawOval(name, x-rx, y-ry, 2*rx, 2*ry);
}
/**
paint a poligon them fill with (f) color and draw the border with (d) color.
*/
void paintPolygon(const char* name, double x[], double y[], int n_points, int fr, int fg, int fb, int fa, int dr, int dg, int db, int da){
    setColor(name, fr, fg, fb, fa);
    fillPolygon(name, x, y, n_points);
    setColor(name, dr, dg, db, da);
    drawPolygon(name, x, y, n_points);
}
#endif
