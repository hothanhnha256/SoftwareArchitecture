package com.softwareA.appointment.dto.response;

import com.softwareA.appointment.model.appointment.Appointment;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.appointment.model.staff.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class AppointmentDetailDTO {
    public AppointmentDetailDTO (Appointment appointment)
    {
        this.appointment = appointment;
    }
    private Appointment appointment;
    private Doctor doctor ;
    private Shift shift;
}
