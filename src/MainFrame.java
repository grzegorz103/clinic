import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel mainPane;
    private JButton doctorButton, nurseButton, patientButton;
    private List<Patient> patientList = new ArrayList<>();
    private List<Doctor> doctorList = new ArrayList<>();
    private JTextArea dateArea = new JTextArea();
    private int id;

    public MainFrame() {
        this.mainPane = new JPanel(new FlowLayout());
        this.setSize(400, 250);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        try {
            readIn();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; ++i) {
            this.doctorList.add(new Doctor(i));
        }

        this.doctorButton = new JButton("Doctor");
        this.doctorButton.addActionListener((e) -> doctorPane());

        this.nurseButton = new JButton("Nurse");
        this.nurseButton.addActionListener((e) -> nursePane());

        this.patientButton = new JButton("Patient");
        this.patientButton.addActionListener((e) -> patientPane());

        this.mainPane.add(doctorButton);
        this.mainPane.add(nurseButton);
        this.mainPane.add(patientButton);

        this.add(mainPane);
    }

    private void doctorPane() {
        this.mainPane.setVisible(false);
        this.dateArea.setVisible(true);
        this.dateArea.setEditable(false);
        this.dateArea.setText("");

        int tempId;
        do {
            tempId = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter doctor's ID"));
        } while (tempId > this.doctorList.size());

        JPanel tempPane = new JPanel(new FlowLayout());

        try {
            readIn();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doctorList.get(tempId).getPatients().size() == 0) {
            dateArea.setText("No upcoming visit for doctor " + tempId);

        } else {
            for (int i = 0; i < doctorList.get(tempId).getPatients().size(); ++i) {
                for (int j = 0; j < doctorList.get(tempId).getPatients().get(i).getVisits().size(); ++j) {
                    dateArea.setText(dateArea.getText() + "Patient: " + this.doctorList.get(tempId).getPatients().get(i).getName() + " z choroba: " + this.doctorList.get(tempId).getPatients().get(i).getVisits().get(j).toString() + "\n");
                }
            }
        }

        tempPane.add(dateArea);

        JPanel retPane = returnPane();
        JButton retButton = new JButton("Return");
        retButton.addActionListener((e) -> {
            tempPane.setVisible(false);
            mainPane.setVisible(true);
        });

        retPane.add(retButton);
        tempPane.add(retPane);
        tempPane.revalidate();
        tempPane.repaint();
        this.add(tempPane);
        this.repaint();
        this.revalidate();
    }

    private void nursePane() {
        this.mainPane.setVisible(false);
        this.dateArea.setVisible(true);
        this.dateArea.setEditable(false);
        this.dateArea.setText("");

        int tempId;
        do {
            tempId = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter nurse's ID (1 or 2)"));
            --tempId;
        } while (tempId >= 2);


        try {
            readIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.patientList.size() == 0) {
            dateArea.setText("No patients");
        } else {
            for (Patient x : patientList) {
                dateArea.setText(dateArea.getText() + x.getId() + " " + x.getName() + "\n");
            }
        }

        JButton newPatientButton = new JButton("Add a new patient");
        newPatientButton.addActionListener((e) -> {
            addNewPatient();
        });

        JButton removePatientButton = new JButton("Remove a patient");
        removePatientButton.addActionListener((e) -> {
            removePatient();
        });

        JButton modifyPatientButton = new JButton("Modify a patient");
        modifyPatientButton.addActionListener((e) -> {
            modifyPatient();
        });

        JPanel tempPane = new JPanel(new FlowLayout());
        tempPane.add(dateArea);
        tempPane.add(newPatientButton);
        tempPane.add(removePatientButton);
        tempPane.add(modifyPatientButton);
        tempPane.setVisible(true);

        JPanel retPane = returnPane();
        JButton retButton = new JButton("Return");
        retButton.addActionListener((e) -> {
            tempPane.setVisible(false);
            dateArea.setVisible(false);
            mainPane.setVisible(true);
        });

        retPane.add(retButton);
        tempPane.add(retPane);
        tempPane.revalidate();
        tempPane.repaint();
        this.add(tempPane);
        this.repaint();
        this.revalidate();
    }

    private void patientPane() {

        if (this.patientList.size() == 0) {
            JOptionPane.showInputDialog("There's no patients in the clinic!");
            return;
        }
        this.mainPane.setVisible(false);
        this.dateArea.setVisible(true);
        this.dateArea.setEditable(false);

        int tempId;
        do {
            tempId = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter patient's ID"));
        } while (tempId > this.patientList.size() - 1);
        try {
            readIn();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Patient patient = this.patientList.get(tempId);
        dateArea.setText("");

        if (patient.getVisits().size() == 0) {
            dateArea.setText("No visits");
        } else {
            for (int i = 0; i < patient.getVisits().size(); ++i) {
                dateArea.setText(dateArea.getText() + "Visit " + patient.getVisits().get(i).toString() + " with doctor: " + patient.getVisits().get(i).getIdDoctor() + "\n");
            }
        }

        JButton registryButton = new JButton("Registry a new visit");
        this.id = tempId;
        registryButton.addActionListener(e -> addVisit());

        JLabel patientInfo = new JLabel();
        patientInfo.setText("Patient's name: " + patient.getName());

        JPanel pane = new JPanel();

        this.add(pane);
        this.revalidate();
        this.repaint();

        JPanel retPane = returnPane();
        JButton retButton = new JButton("Return");
        retButton.addActionListener((e) ->
        {
            pane.setVisible(false);
            mainPane.setVisible(true);
        });

        retPane.add(retButton);
        pane.add(patientInfo);
        pane.add(registryButton);
        pane.add(dateArea);
        pane.add(retPane);
        pane.revalidate();
        pane.repaint();
    }

    private void readIn() throws IOException {
        File plik = new File("patients.dat");

        if (!plik.exists())
            plik.createNewFile();

        FileInputStream fis = new FileInputStream("patients.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        try {
            this.patientList = (List<Patient>) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addNewPatient() {
        String imie = JOptionPane.showInputDialog("Entry the name");
        Patient pacjent = new Patient(patientList.size(), imie);

        this.patientList.add(pacjent);

        save();
        dateArea.setText("");

        for (Patient x : patientList) {
            dateArea.setText(dateArea.getText() + x.getId() + " " + x.getName() + "\n");
        }

    }

    private void removePatient() {
        int id = Integer.valueOf(JOptionPane.showInputDialog("Enter patient's id"));
        this.patientList.remove(id);
        for (int i = id; i < this.patientList.size(); ++i)
            patientList.get(i).decId();

        save();

        this.dateArea.setText("");
        for (Patient x : patientList) {
            dateArea.setText(dateArea.getText() + x.getId() + " " + x.getName() + "\n");
        }
    }

    private void modifyPatient() {
        int id = Integer.valueOf(JOptionPane.showInputDialog("Enter patient's id"));
        String name = JOptionPane.showInputDialog("Enter new name");

        Patient patient = patientList.get(id);
        patient.setName(name);
        this.patientList.set(id, patient);
        this.dateArea.setText("");

        for (Patient x : patientList) {
            dateArea.setText(dateArea.getText() + x.getId() + " " + x.getName() + "\n");
        }

        save();
    }

    private void save() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream("patients.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.patientList);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addVisit() {
        String description = JOptionPane.showInputDialog("Enter disease's description");
        int idDoctor = Integer.valueOf(JOptionPane.showInputDialog("Enter doctors ID"));

        Patient patient = patientList.get(id);

        patient.addVisit(description, idDoctor);
        this.patientList.set(id, patient);

        this.doctorList.get(idDoctor).addNewPacjent(patient);
        save();

        dateArea.setText("");
        for (int i = 0; i < patient.getVisits().size(); ++i) {
            dateArea.setText(dateArea.getText() + "Visit " + patient.getVisits().get(i).toString() + " with doctor: " + patient.getVisits().get(i).getIdDoctor() + "\n");
        }
    }

    private JPanel returnPane() {
        JPanel temp = new JPanel();
        temp.setSize(50, 50);

        return temp;
    }
}
