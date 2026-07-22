package dev.quaag.plummet.stats;

public final class StatEntry {
    private int hits;
    private int maceHits;
    private double bestFallDistance;
    private float bestMaceDamage;
    private float bestOtherDamage;

    public void record(double fallDistance, float damage, boolean mace) {
        hits++;
        if (fallDistance > bestFallDistance) {
            bestFallDistance = fallDistance;
        }

        if (mace) {
            maceHits++;
            if (damage > bestMaceDamage) {
                bestMaceDamage = damage;
            }
        } else if (damage > bestOtherDamage) {
            bestOtherDamage = damage;
        }
    }

    public void set(int hits, int maceHits, double bestFallDistance, float bestMaceDamage, float bestOtherDamage) {
        this.hits = hits;
        this.maceHits = maceHits;
        this.bestFallDistance = bestFallDistance;
        this.bestMaceDamage = bestMaceDamage;
        this.bestOtherDamage = bestOtherDamage;
    }

    public int hits() {
        return hits;
    }

    public int maceHits() {
        return maceHits;
    }

    public double bestFallDistance() {
        return bestFallDistance;
    }

    public float bestMaceDamage() {
        return bestMaceDamage;
    }

    public float bestOtherDamage() {
        return bestOtherDamage;
    }
}
