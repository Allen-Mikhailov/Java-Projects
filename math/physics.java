public class physics {
    public static double deltax(double v0, double a, double t)
    {
        return v0*t + .5*a*Math.pow(t, 2);
    }

    public static double range(double u, double angle, double g)
    {
        return (Math.pow(u, 2) * Math.sin(angle)) / g;
    }

    public static double velocity(double r, double a, double g)
    {
        return Math.sqrt((r * g) / Math.sin(a));
    }
}
