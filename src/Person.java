import java.io.Serializable;

public abstract class Person implements Serializable {

    private int id;

    public Person(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void decId() {
        this.id--;
    }

}
