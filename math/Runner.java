public class Runner
{
    public static void main(String[] args) {
        double a = Math.toRadians(45);
        double r = 5;
        double g = 9.8;

        double u = physics.velocity(r, a, g);

        g /= 6;

        double t = (2 * u) / g;

        System.out.println(physics.range(u, a, g));
    }
}