package core.views;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.controllers.AppointmentController;
import core.controllers.UserController;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.controllers.utils.ViewUtils;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Hospitalization;
import core.models.entities.Patient;
import core.models.entities.User;
import core.models.storage.Storage;
import java.awt.Color;
import java.util.ArrayList;

public class NewJFrame11 extends javax.swing.JFrame {

    private int x, y;
    private ArrayList<User> users;
    private ArrayList<Appointment>appointments;
    private ArrayList<Hospitalization>hospitalizations;
    private User user;
    public NewJFrame11(User user, ArrayList<User>users,ArrayList<Hospitalization> hospitalizations, ArrayList<Appointment> appointments) {
        initComponents();
        this.user = user;
        this.users = users;
        this.hospitalizations = hospitalizations;
        this.appointments = appointments;
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);
        this.users = Storage.getInstance().getUsers();
        this.appointments = Storage.getInstance().getAppointments();
        this.hospitalizations = Storage.getInstance().getHospitalizations();
        loadAdminImpersonationCombos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRoot = new core.views.components.PanelRound();
        pnlTitleBar = new core.views.components.PanelRound();
        btnCloseWindow = new javax.swing.JButton();
        lblAdminTitle = new javax.swing.JLabel();
        pnlAdminContent = new core.views.components.PanelRound();
        btnOpenDoctorView = new javax.swing.JButton();
        btnOpenPatientView = new javax.swing.JButton();
        lblDoctorFirstname = new javax.swing.JLabel();
        txtDoctorFirstname = new javax.swing.JTextField();
        lblDoctorLastname = new javax.swing.JLabel();
        txtDoctorLastname = new javax.swing.JTextField();
        lblDoctorId = new javax.swing.JLabel();
        txtDoctorId = new javax.swing.JTextField();
        lblDoctorSpecialty = new javax.swing.JLabel();
        lblDoctorLicence = new javax.swing.JLabel();
        txtDoctorLicence = new javax.swing.JTextField();
        lblDoctorOffice = new javax.swing.JLabel();
        txtDoctorOffice = new javax.swing.JTextField();
        lblDoctorUsername = new javax.swing.JLabel();
        txtDoctorUsername = new javax.swing.JTextField();
        lblDoctorPassword = new javax.swing.JLabel();
        txtDoctorPassword = new javax.swing.JTextField();
        lblDoctorPasswordConfirm = new javax.swing.JLabel();
        txtDoctorPasswordConfirm = new javax.swing.JTextField();
        cmbDoctorSpecialty = new javax.swing.JComboBox<>();
        btnRegisterDoctor = new javax.swing.JButton();
        sepAdminMain = new javax.swing.JSeparator();
        cmbDoctorImpersonation = new javax.swing.JComboBox<>();
        lblDoctorImpersonation = new javax.swing.JLabel();
        lblPatientImpersonation = new javax.swing.JLabel();
        cmbPatientImpersonation = new javax.swing.JComboBox<>();
        sepAdminSide = new javax.swing.JSeparator();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnlRoot.setRadius(50);

        pnlTitleBar.setRadius(50);
        pnlTitleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlTitleBarMouseDragged(evt);
            }
        });
        pnlTitleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlTitleBarMousePressed(evt);
            }
        });

        btnCloseWindow.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCloseWindow.setText("X");
        btnCloseWindow.setBorderPainted(false);
        btnCloseWindow.setContentAreaFilled(false);
        btnCloseWindow.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCloseWindow.setFocusable(false);
        btnCloseWindow.setRequestFocusEnabled(false);
        btnCloseWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseWindowActionPerformed(evt);
            }
        });

        lblAdminTitle.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lblAdminTitle.setText("ADMIN VIEW");

        javax.swing.GroupLayout pnlTitleBarLayout = new javax.swing.GroupLayout(pnlTitleBar);
        pnlTitleBar.setLayout(pnlTitleBarLayout);
        pnlTitleBarLayout.setHorizontalGroup(
            pnlTitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTitleBarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblAdminTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCloseWindow)
                .addGap(19, 19, 19))
        );
        pnlTitleBarLayout.setVerticalGroup(
            pnlTitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnCloseWindow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAdminTitle))
        );

        btnOpenDoctorView.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnOpenDoctorView.setText("DOCTOR VIEW");
        btnOpenDoctorView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenDoctorViewActionPerformed(evt);
            }
        });

        btnOpenPatientView.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnOpenPatientView.setText("PATIENT VIEW");
        btnOpenPatientView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenPatientViewActionPerformed(evt);
            }
        });

        lblDoctorFirstname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorFirstname.setText("Firstname");

        txtDoctorFirstname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorLastname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorLastname.setText("Lastname");

        txtDoctorLastname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorId.setText("ID");

        txtDoctorId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorSpecialty.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorSpecialty.setText("Specialty");

        lblDoctorLicence.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorLicence.setText("License Number");

        txtDoctorLicence.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorOffice.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorOffice.setText("Assigned office");

        txtDoctorOffice.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorUsername.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorUsername.setText("User");

        txtDoctorUsername.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorPassword.setText("Password");

        txtDoctorPassword.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblDoctorPasswordConfirm.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorPasswordConfirm.setText("Password confirmation");

        txtDoctorPasswordConfirm.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        cmbDoctorSpecialty.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbDoctorSpecialty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "General Medicine", "Cardiology", "Pediatrics", "Neurology", "Traumatology & Orthopedics", "Gynecology & Obstetrics", "Dermatology", "Psychiatry", "Oncology", "Ophthalmology", "Internal Medicine" }));

        btnRegisterDoctor.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRegisterDoctor.setText("Save");
        btnRegisterDoctor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterDoctorActionPerformed(evt);
            }
        });

        sepAdminMain.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cmbDoctorImpersonation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbDoctorImpersonation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        lblDoctorImpersonation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDoctorImpersonation.setText("Doctor");

        lblPatientImpersonation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPatientImpersonation.setText("Patient");

        cmbPatientImpersonation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cmbPatientImpersonation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        sepAdminSide.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnLogout.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAdminContentLayout = new javax.swing.GroupLayout(pnlAdminContent);
        pnlAdminContent.setLayout(pnlAdminContentLayout);
        pnlAdminContentLayout.setHorizontalGroup(
            pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addComponent(btnRegisterDoctor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDoctorFirstname)
                                    .addComponent(lblDoctorSpecialty))
                                .addGap(18, 18, 18)
                                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addComponent(cmbDoctorSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblDoctorLicence)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDoctorLicence, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addComponent(txtDoctorFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(lblDoctorLastname)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDoctorLastname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblDoctorId)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDoctorId, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                .addComponent(lblDoctorOffice)
                                .addGap(18, 18, 18)
                                .addComponent(txtDoctorOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addComponent(lblDoctorUsername)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDoctorUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addComponent(lblDoctorPassword)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDoctorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addComponent(lblDoctorPasswordConfirm)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDoctorPasswordConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(333, 333, 333)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOpenDoctorView)
                            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(cmbDoctorImpersonation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                        .addGap(47, 47, 47)
                                        .addComponent(lblDoctorImpersonation)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(74, 74, 74))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdminContentLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout)
                        .addGap(318, 318, 318)))
                .addComponent(sepAdminMain, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOpenPatientView)
                            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(cmbPatientImpersonation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(lblPatientImpersonation)))
                .addGap(88, 88, 88))
            .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdminContentLayout.createSequentialGroup()
                    .addContainerGap(707, Short.MAX_VALUE)
                    .addComponent(sepAdminSide, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(523, 523, 523)))
        );
        pnlAdminContentLayout.setVerticalGroup(
            pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                .addComponent(sepAdminMain)
                .addContainerGap())
            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoctorFirstname)
                    .addComponent(txtDoctorFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoctorLastname)
                    .addComponent(txtDoctorLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoctorId)
                    .addComponent(txtDoctorId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDoctorSpecialty)
                    .addComponent(cmbDoctorSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDoctorLicence)
                    .addComponent(txtDoctorLicence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDoctorOffice)
                    .addComponent(txtDoctorOffice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDoctorUsername)
                            .addComponent(txtDoctorUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDoctorPassword)
                            .addComponent(txtDoctorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDoctorPasswordConfirm)
                            .addComponent(txtDoctorPasswordConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlAdminContentLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblDoctorImpersonation)
                        .addGap(18, 18, 18)
                        .addComponent(cmbDoctorImpersonation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnOpenDoctorView)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(btnRegisterDoctor)
                .addGap(123, 123, 123)
                .addComponent(btnLogout)
                .addGap(38, 38, 38))
            .addGroup(pnlAdminContentLayout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addComponent(lblPatientImpersonation)
                .addGap(18, 18, 18)
                .addComponent(cmbPatientImpersonation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnOpenPatientView)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlAdminContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlAdminContentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(sepAdminSide)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout pnlRootLayout = new javax.swing.GroupLayout(pnlRoot);
        pnlRoot.setLayout(pnlRootLayout);
        pnlRootLayout.setHorizontalGroup(
            pnlRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitleBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlAdminContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlRootLayout.setVerticalGroup(
            pnlRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRootLayout.createSequentialGroup()
                .addComponent(pnlTitleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAdminContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlTitleBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTitleBarMousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_pnlTitleBarMousePressed

    private void pnlTitleBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTitleBarMouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_pnlTitleBarMouseDragged

    private void btnCloseWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseWindowActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCloseWindowActionPerformed

    private void btnRegisterDoctorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterDoctorActionPerformed
        String specialtyDisplay = "";
        if (cmbDoctorSpecialty.getSelectedIndex() > 0) {
            specialtyDisplay = cmbDoctorSpecialty.getItemAt(cmbDoctorSpecialty.getSelectedIndex());
        }

        Response response = UserController.registerDoctor(
                txtDoctorId.getText(),
                txtDoctorFirstname.getText(),
                txtDoctorLastname.getText(),
                txtDoctorUsername.getText(),
                txtDoctorPassword.getText(),
                txtDoctorPasswordConfirm.getText(),
                specialtyDisplay,
                txtDoctorLicence.getText(),
                txtDoctorOffice.getText()
        );

        ViewUtils.showResponseMessage(response);

        if (response.getStatus() == Status.CREATED) {
            clearDoctorRegisterFields();
        }
    }//GEN-LAST:event_btnRegisterDoctorActionPerformed

    private void clearDoctorRegisterFields() {
        txtDoctorFirstname.setText("");
        txtDoctorLastname.setText("");
        txtDoctorId.setText("");
        txtDoctorLicence.setText("");
        txtDoctorOffice.setText("");
        txtDoctorUsername.setText("");
        txtDoctorPassword.setText("");
        txtDoctorPasswordConfirm.setText("");
        cmbDoctorSpecialty.setSelectedIndex(0);
    }

    private void btnOpenDoctorViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenDoctorViewActionPerformed
        long doctorId = parseIdFromCombo((String) cmbDoctorImpersonation.getSelectedItem());
        if (doctorId < 0) {
            ViewUtils.showResponseMessage(new Response("Select a valid doctor", Status.BAD_REQUEST));
            return;
        }
        Doctor selectedDoctor = findDoctorById(doctorId);
        if (selectedDoctor == null) {
            ViewUtils.showResponseMessage(new Response("Doctor not found", Status.NOT_FOUND));
            return;
        }
        ArrayList<Appointment> currentAppointments = Storage.getInstance().getAppointments();
        ArrayList<Hospitalization> currentHospitalizations = Storage.getInstance().getHospitalizations();
        NewJFrame111 doctorView = new NewJFrame111(user, selectedDoctor, users, currentHospitalizations, currentAppointments);
        this.setVisible(false);
        doctorView.setVisible(true);
    }//GEN-LAST:event_btnOpenDoctorViewActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        ViewUtils.performLogout(this);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnOpenPatientViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenPatientViewActionPerformed
        long patientId = parseIdFromCombo((String) cmbPatientImpersonation.getSelectedItem());
        if (patientId < 0) {
            ViewUtils.showResponseMessage(new Response("Select a valid patient", Status.BAD_REQUEST));
            return;
        }
        Patient selectedPatient = findPatientById(patientId);
        if (selectedPatient == null) {
            ViewUtils.showResponseMessage(new Response("Patient not found", Status.NOT_FOUND));
            return;
        }
        ArrayList<Appointment> currentAppointments = Storage.getInstance().getAppointments();
        ArrayList<Hospitalization> currentHospitalizations = Storage.getInstance().getHospitalizations();
        NewJFrame1 patientView = new NewJFrame1(user, selectedPatient, users, currentAppointments, currentHospitalizations);
        this.setVisible(false);
        patientView.setVisible(true);
    }//GEN-LAST:event_btnOpenPatientViewActionPerformed

    private void loadAdminImpersonationCombos() {
        fillComboFromResponse(cmbDoctorImpersonation, AppointmentController.getDoctorComboOptions());
        fillComboFromResponse(cmbPatientImpersonation, AppointmentController.listPatientComboOptions());
    }

    private void fillComboFromResponse(javax.swing.JComboBox<String> combo, Response response) {
        combo.removeAllItems();
        if (response.getStatus() != Status.OK || response.getData() == null) {
            combo.addItem("Select one");
            return;
        }
        Object optionsObject = response.getData().get("options");
        if (optionsObject instanceof ArrayList) {
            ArrayList<?> options = (ArrayList<?>) optionsObject;
            for (Object option : options) {
                if (option != null) {
                    combo.addItem(option.toString());
                }
            }
        } else {
            combo.addItem("Select one");
        }
    }

    private long parseIdFromCombo(String selection) {
        if (selection == null || "Select one".equals(selection)) {
            return -1;
        }
        int separatorIndex = selection.indexOf(" - ");
        if (separatorIndex > 0) {
            try {
                return Long.parseLong(selection.substring(0, separatorIndex).trim());
            } catch (NumberFormatException ex) {
                return -1;
            }
        }
        try {
            return Long.parseLong(selection.trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private Doctor findDoctorById(long doctorId) {
        for (User currentUser : users) {
            if (currentUser instanceof Doctor) {
                if (currentUser.getId() == doctorId) {
                    return (Doctor) currentUser;
                }
            }
        }
        User storedUser = Storage.getInstance().findUserById(doctorId);
        if (storedUser instanceof Doctor) {
            return (Doctor) storedUser;
        }
        return null;
    }

    private Patient findPatientById(long patientId) {
        for (User currentUser : users) {
            if (currentUser instanceof Patient) {
                if (currentUser.getId() == patientId) {
                    return (Patient) currentUser;
                }
            }
        }
        User storedUser = Storage.getInstance().findUserById(patientId);
        if (storedUser instanceof Patient) {
            return (Patient) storedUser;
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseWindow;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOpenDoctorView;
    private javax.swing.JButton btnOpenPatientView;
    private javax.swing.JButton btnRegisterDoctor;
    private javax.swing.JComboBox<String> cmbDoctorSpecialty;
    private javax.swing.JComboBox<String> cmbDoctorImpersonation;
    private javax.swing.JComboBox<String> cmbPatientImpersonation;
    private javax.swing.JLabel lblAdminTitle;
    private javax.swing.JLabel lblDoctorUsername;
    private javax.swing.JLabel lblDoctorPassword;
    private javax.swing.JLabel lblDoctorPasswordConfirm;
    private javax.swing.JLabel lblDoctorImpersonation;
    private javax.swing.JLabel lblPatientImpersonation;
    private javax.swing.JLabel lblDoctorFirstname;
    private javax.swing.JLabel lblDoctorLastname;
    private javax.swing.JLabel lblDoctorId;
    private javax.swing.JLabel lblDoctorSpecialty;
    private javax.swing.JLabel lblDoctorLicence;
    private javax.swing.JLabel lblDoctorOffice;
    private javax.swing.JSeparator sepAdminMain;
    private javax.swing.JSeparator sepAdminSide;
    private javax.swing.JTextField txtDoctorPasswordConfirm;
    private javax.swing.JTextField txtDoctorFirstname;
    private javax.swing.JTextField txtDoctorLastname;
    private javax.swing.JTextField txtDoctorId;
    private javax.swing.JTextField txtDoctorLicence;
    private javax.swing.JTextField txtDoctorOffice;
    private javax.swing.JTextField txtDoctorUsername;
    private javax.swing.JTextField txtDoctorPassword;
    private core.views.components.PanelRound pnlRoot;
    private core.views.components.PanelRound pnlTitleBar;
    private core.views.components.PanelRound pnlAdminContent;
    // End of variables declaration//GEN-END:variables
}
