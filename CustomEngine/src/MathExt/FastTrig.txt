//1.27323954  = 4/pi
//0.405284735 =-4/(pi^2)

/*********************************************************
 * low precision sine/cosine
 *********************************************************/

//always wrap input angle to -PI..PI
if (x < -3.14159265)
    x += 6.28318531;
else
if (x >  3.14159265)
    x -= 6.28318531;

//compute sine
if (x < 0)
    sin = 1.27323954 * x + .405284735 * x * x;
else
    sin = 1.27323954 * x - 0.405284735 * x * x;

//compute cosine: sin(x + PI/2) = cos(x)
x += 1.57079632;
if (x >  3.14159265)
    x -= 6.28318531;

if (x < 0)
    cos = 1.27323954 * x + 0.405284735 * x * x
else
    cos = 1.27323954 * x - 0.405284735 * x * x;
}

/*********************************************************
 * high precision sine/cosine
 *********************************************************/

//always wrap input angle to -PI..PI
if (x < -3.14159265)
    x += 6.28318531;
else
if (x >  3.14159265)
    x -= 6.28318531;

//compute sine
if (x < 0)
{
    sin = 1.27323954 * x + .405284735 * x * x;

    if (sin < 0)
        sin = .225 * (sin *-sin - sin) + sin;
    else
        sin = .225 * (sin * sin - sin) + sin;
}
else
{
    sin = 1.27323954 * x - 0.405284735 * x * x;

    if (sin < 0)
        sin = .225 * (sin *-sin - sin) + sin;
    else
        sin = .225 * (sin * sin - sin) + sin;
}

//compute cosine: sin(x + PI/2) = cos(x)
x += 1.57079632;
if (x >  3.14159265)
    x -= 6.28318531;

if (x < 0)
{
    cos = 1.27323954 * x + 0.405284735 * x * x

    if (cos < 0)
        cos = .225 * (cos *-cos - cos) + cos;
    else
        cos = .225 * (cos * cos - cos) + cos;
}
else
{
    cos = 1.27323954 * x - 0.405284735 * x * x;

    if (cos < 0)
        cos = .225 * (cos *-cos - cos) + cos;
    else
        cos = .225 * (cos * cos - cos) + cos;
}