package encaps;

public class Resources {
    private int dirt, timber, population;

    public Resources() {
        dirt = timber = 1000;
    }

    public void deltaDirt(int delta) {
        dirt += delta;
    }

    public void deltaTimber(int delta) {
        timber += delta;
    }

    public void deltaPopulation(int delta) {
        population += delta;
    }

    public int getDirt() {
        return dirt;
    }

    public int getTimber() {
        return timber;
    }

    public int getPopulation() {
        return population;
    }

}
