import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {

    private List<Patient> patients = new ArrayList<>();

    public Doctor(int id) {
        super(id);
    }


    public List<Patient> getPatients() {
        return patients;
    }

    public void addNewPacjent(Patient pacjent) {
        this.patients.add(pacjent);
    }

    @Override
    public String toString() {
        return "Doctors id: " + getId();
    }
}
