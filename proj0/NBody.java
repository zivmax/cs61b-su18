public class NBody {
    public static double readRadius(String filename) {
        In in = new In(filename);
        int N = in.readInt();
        double R = in.readDouble();
        return R;
    }

    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int N = in.readInt();
        double R = in.readDouble();
        Planet[] allPlanets = new Planet[N];
        for (int i = 0; i < N; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            allPlanets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return allPlanets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double R = readRadius(filename);
        Planet[] allPlanets = readPlanets(filename);
        StdDraw.setScale(-R, R);
        StdDraw.picture(0, 0, "images/starfield.jpg");
        for (Planet p : allPlanets) {
            p.draw();
        }
        StdDraw.enableDoubleBuffering();

        long timer = 0;
        while (timer < T) {
            double[] xForces = new double[allPlanets.length];
            double[] yForces = new double[allPlanets.length];
            for (int i = 0; i < allPlanets.length; i++) {
                Planet p = allPlanets[i];
                xForces[i] = p.calcNetForceExertedByX(allPlanets);
                yForces[i] = p.calcNetForceExertedByY(allPlanets);
            }
            for (int i = 0; i < allPlanets.length; i++) {
                Planet p = allPlanets[i];
                p.update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet p : allPlanets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            timer += dt;

            StdOut.printf("%d\n", allPlanets.length);
        }
        StdOut.printf("%.2e\n", R);
        for (int i = 0; i < allPlanets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    allPlanets[i].xxPos, allPlanets[i].yyPos, allPlanets[i].xxVel,
                    allPlanets[i].yyVel, allPlanets[i].mass, allPlanets[i].imgFileName);
        }
    }
}
