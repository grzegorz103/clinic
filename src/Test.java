import java.io.Serializable;

public class Test implements Serializable {

    private String description;
    private int idDoctor;

    public Test(String description, int idDoctor) {
        this.description = description;
        this.idDoctor = idDoctor;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public int getIdDoctor() {
        return this.idDoctor;
    }
}
