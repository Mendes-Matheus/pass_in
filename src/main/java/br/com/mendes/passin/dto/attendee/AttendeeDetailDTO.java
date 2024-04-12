package br.com.mendes.passin.dto.attendee;

import java.time.LocalDateTime;

public record AttendeeDetailDTO (String id, String name, String email, LocalDateTime createdAt, LocalDateTime checkedInAt) {
}
