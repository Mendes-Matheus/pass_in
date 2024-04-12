package br.com.mendes.passin.dto.attendee;

import java.util.List;

public record AttendeesListResponseDTO(List<AttendeeDetailDTO> attendees) {
}
