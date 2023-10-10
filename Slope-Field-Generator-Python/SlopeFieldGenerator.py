from graphics import *

sepy = 0
sepx = 0

print("When entering an equation, do not use variables other than x and y.")
print("If you want to input x^2, input it as xx or yyy for y^3, etc.")
print("Enter Equation (ex: x/y):")
eqn = input("dy/dx = ")
eqn = eqn.replace(" ", "")

def slope_check(equation, pnt: Point):
    slope = 1

    length = len(equation) - 1
    i = 0
    while i < length:
        if (equation[i] != '+' and equation[i] != '-' and equation[i] != '/') and (equation[i+1] != '+' and equation[i+1] != '-' and equation[i+1] != '/'):
            equation = equation[0:i+1] + '*' + equation[i+1:]
            i += 2
        else:
            i += 1
        
        length = len(equation) - 1
        

    if 'x' in equation:
        equation = str(equation.replace('x', str(pnt.getX())))
    if 'y' in equation:
        equation = str(equation.replace('y', str(pnt.getY())))
    try:    
        slope = eval(equation)
    except:
        return 'fail'
    return slope

#print(slope_check('2x+3xy', Point(3, 3)))
    
win = GraphWin()


win.setCoords(0, 0, 1000, 1000)

pt = Point(0, 0)
pt.draw(win)

for x in range(10):
    
    line = Line(Point(sepy, 0), Point(sepy, 1000))
    line.setFill('grey')
    line.draw(win)  
    sepy += 100

for y in range(10):
    line = Line(Point(0, sepx), Point(1000, sepx))
    line.setFill('grey')
    line.draw(win)  
    sepx += 100




line = Line(Point(0, 500), Point(1000, 500))
line.setFill('red')
line.draw(win)  


for k in range(0,1100,100):
    for j in range(0,1100,100):
        line.setFill('blue')
        slope1 = slope_check(eqn, Point((k/100)-5, (j/100)-5))
        
        if slope1 == 'fail':
            line = Line(Point(k,j), Point(k,j-25))
            line.draw(win)
            line = Line(Point(k,j), Point(k,j+25))
            line.draw(win)
        elif slope1 > 1:
            line = Line(Point(k,j), Point(k+(25/slope1),j+25))
            line.draw(win)
            line = Line(Point(k,j), Point(k-(25/slope1),j-25))
            line.draw(win)
        elif slope1 < 1 and slope1 > 0:
            line = Line(Point(k,j), Point(k-25,j-(25*slope1)))
            line.draw(win)
            line = Line(Point(k,j), Point(k+25,j+(25*slope1)))
            line.draw(win)
            
        elif slope1 < 0 and slope1 > -1:
            line = Line(Point(k,j), Point(k-25,j-(25*slope1)))
            line.draw(win)
            line = Line(Point(k,j), Point(k+25,j+(25*slope1)))
            line.draw(win)
            
        elif slope1 < 1 and slope1 != 0:
            line = Line(Point(k,j), Point(k-(25/slope1),j-25))
            line.draw(win)
            line = Line(Point(k,j), Point(k+(25/slope1),j+25))
            line.draw(win)
        elif slope1 == 0:
            line = Line(Point(k,j), Point(k-(25),j))
            line.draw(win)
            line = Line(Point(k,j), Point(k+25,j))
            line.draw(win)

line = Line(Point(500, 0), Point(500, 1000))
line.setFill('red')
line.draw(win)  

win.getMouse()

