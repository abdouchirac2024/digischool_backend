package com.digiSchool.digiSchool.notification.dto;

import java.time.LocalDateTime;
import com.digiSchool.digiSchool.notification.model.TypeNotification;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notification")
public class NotificationDto {

    @Schema(description = "ID de la notification")
    private Long id;

    @Schema(description = "Titre")
    private String titre;

    @Schema(description = "Message")
    private String message;

    @Schema(description = "Type de notification")
    private TypeNotification type;

    @Schema(description = "Lu ou non")
    private Boolean lu;

    @Schema(description = "Date de creation")
    private LocalDateTime createdAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }
    public Boolean getLu() { return lu; }
    public void setLu(Boolean lu) { this.lu = lu; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
