/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.http.HttpSession;
import model.Doctor;
import model.Patients;
import model.Users;

/**
 *
 * @author TNO
 */
public class SessionUtils {

    /**
     * Lấy UserID từ session
     *
     * @param session HttpSession hiện tại
     * @return userId nếu có, ngược lại trả về -1
     */
    public static int getUserId(HttpSession session) {
        if (session == null) {
            return -1;
        }
        Object userObj = session.getAttribute("user");
        if (userObj instanceof Users) {
            return ((Users) userObj).getUserId();
        }
        return -1;
    }

    /**
     * Lấy DoctorID từ session
     *
     * @param session HttpSession hiện tại
     * @return doctorId nếu có, ngược lại trả về -1
     */
    public static int getDoctorId(HttpSession session) {
        if (session == null) {
            return -1;
        }
        Object doctorObj = session.getAttribute("doctor");
        if (doctorObj instanceof Doctor) {
            return ((Doctor) doctorObj).getDoctorID();
        }
        return -1;
    }

    /**
     * Lấy PatientID từ session
     *
     * @param session HttpSession hiện tại
     * @return patientId nếu có, ngược lại trả về -1
     */
    public static int getPatientId(HttpSession session) {
        if (session == null) {
            return -1;
        }
        Object patientObj = session.getAttribute("patient");
        if (patientObj instanceof Patients) {
            return ((Patients) patientObj).getPatientID();
        }
        return -1;
    }

}
