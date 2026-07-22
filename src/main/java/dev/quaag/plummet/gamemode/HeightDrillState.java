package dev.quaag.plummet.gamemode;

public final class HeightDrillState {
    private final double target;
    private boolean launched;
    private double baseY;
    private double peakY;
    private double best;
    private int attempts;
    private int passes;

    public HeightDrillState(double target) {
        this.target = target;
    }

    public void launch(double y) {
        launched = true;
        baseY = y;
        peakY = y;
    }

    public boolean isLaunched() {
        return launched;
    }

    public double target() {
        return target;
    }

    public Landing tick(double y, boolean onGround) {
        if (!launched) {
            return null;
        }

        if (y > peakY) {
            peakY = y;
        }

        if (!onGround) {
            return null;
        }

        launched = false;
        double gained = peakY - baseY;
        if (gained > best) {
            best = gained;
        }

        attempts++;
        boolean passed = gained >= target;
        if (passed) {
            passes++;
        }

        return new Landing(gained, best, passed, passes, attempts);
    }

    public static final class Landing {
        public final double gained;
        public final double best;
        public final boolean passed;
        public final int passes;
        public final int attempts;

        Landing(double gained, double best, boolean passed, int passes, int attempts) {
            this.gained = gained;
            this.best = best;
            this.passed = passed;
            this.passes = passes;
            this.attempts = attempts;
        }
    }
}
