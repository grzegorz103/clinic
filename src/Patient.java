import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Patient extends Person implements Serializable {

    private List<Test> visits = new ArrayList<>();
    private String name;

    public Patient(int id, String name) {
        super(id);
        this.name = name;
    }

    public List<Test> getVisits() {
        return this.visits;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addVisit(String description, int id) {
        this.visits.add(new Test(description, id));
    }

}
